package com.genexus.android.ar

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.PixelCopy
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.genexus.android.ar.databinding.PreviewActivityBinding
import com.genexus.android.core.base.services.Services
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.assets.RenderableSource
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import org.apache.commons.lang.NullArgumentException
import java.io.IOException
import java.util.Locale
import kotlin.concurrent.thread

@RequiresApi(Build.VERSION_CODES.N)
class PreviewActivity : AppCompatActivity() {
    private lateinit var arFragment: ArFragment
    private lateinit var folderName: String
    private var renderable: ModelRenderable? = null
    private var doCaptureScreenInNextFrame = 0
    private lateinit var binding: PreviewActivityBinding

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PreviewActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        arFragment = supportFragmentManager.findFragmentById(R.id.ux_fragment) as ArFragment

        val assetUri = intent.getStringExtra(EXTRA_ASSET_URI)
        folderName = intent.getStringExtra(EXTRA_FOLDER_NAME) ?: throw NullArgumentException("It should never be null since it comes from newIntent")

        val builder = ModelRenderable.builder()
        if (assetUri != null && assetUri.lowercase(Locale.US).endsWith(".gltf")) {
            builder.setSource(
                this,
                RenderableSource.builder()
                    .setSource(this, Uri.parse(assetUri), RenderableSource.SourceType.GLTF2)
                    .setRecenterMode(RenderableSource.RecenterMode.ROOT)
                    .build()
            )
        } else {
            builder.setSource(this, Uri.parse(assetUri))
        }
        builder.build()
            .thenAccept { r -> renderable = r }
            .exceptionally { throwable ->
                Services.Log.error("Unable to load renderable", throwable)
                null
            }

        arFragment.setOnTapArPlaneListener { hitResult, _, _ ->
            if (renderable == null)
                return@setOnTapArPlaneListener

            // Create the Anchor.
            val anchor = hitResult.createAnchor()
            val anchorNode = AnchorNode(anchor)
            anchorNode.setParent(arFragment.arSceneView.scene)

            // Create the transformable asset and add it to the anchor.
            val andy = TransformableNode(arFragment.transformationSystem)
            andy.setParent(anchorNode)
            andy.renderable = renderable
            andy.select()

            renderable = null // Add it only once
        }

        subscribeForCaptureScreen()

        binding.shutter.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(WRITE_EXTERNAL_STORAGE), 0) // Don't take picture, just ask for permission
            } else {
                captureScreen()
            }
        }
    }

    private fun subscribeForCaptureScreen() {
        val arView = arFragment.arSceneView
        val context = this
        arView.scene.addOnUpdateListener {
            if (doCaptureScreenInNextFrame > 0) {
                doCaptureScreenInNextFrame--
                if (doCaptureScreenInNextFrame == 0) {
                    val bitmap = Bitmap.createBitmap(arView.measuredWidth, arView.measuredHeight, Bitmap.Config.ARGB_8888)
                    PixelCopy.request(arView, bitmap, { copyResult ->
                        if (copyResult == PixelCopy.SUCCESS) {
                            thread {
                                val id = addSavingNotification()
                                try {
                                    val uri = ImageHelper.saveImage(bitmap, context, folderName)
                                    setSavedNotification(id, uri)
                                } catch (e: IOException) {
                                    Services.Log.error(e)
                                    setErrorNotification(id)
                                }
                            }

                            // restore selection circle and plane indicators
                            arFragment.transformationSystem.selectionVisualizer.applySelectionVisual(arFragment.transformationSystem.selectedNode)
                            arFragment.arSceneView.planeRenderer.isVisible = true

                            // signal the user the operation was correctly triggered
                            showFlash()
                        } else {
                            Services.Log.error("Error capturing screen " + copyResult)
                        }
                    }, Handler(Looper.getMainLooper()))
                }
            }
        }
    }

    private fun captureScreen() {
        // remove selection circle and plane indicators
        arFragment.transformationSystem.selectionVisualizer.removeSelectionVisual(arFragment.transformationSystem.selectedNode)
        arFragment.arSceneView.planeRenderer.isVisible = false

        doCaptureScreenInNextFrame = 2 // addOnUpdateListener is called before scene is drawn, so do the capture in the 2nd listener call
    }

    private fun showFlash() {
        binding.flash.visibility = View.VISIBLE
        val fade = AlphaAnimation(1f, 0f)
        fade.duration = 300
        fade.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) { }
            override fun onAnimationRepeat(animation: Animation?) { }
            override fun onAnimationEnd(animation: Animation?) {
                binding.flash.visibility = View.GONE
            }
        })
        binding.flash.startAnimation(fade)
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= 26) {
            val name = "Augmented Reality" // Channel Name
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(AR_CHANNEL_ID, name, importance).apply {
                setSound(null, null)
                setShowBadge(false)
            }

            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun addSavingNotification(): Int {
        createNotificationChannel()

        val builder = NotificationCompat.Builder(this, AR_CHANNEL_ID)
            .setPriority(NotificationCompat.PRIORITY_HIGH) // Use high to show a Heads-up notification
            .setSmallIcon(R.drawable.appicon)
            .setContentTitle(Services.Strings.getResource(R.string.GXM_Saving))
            .setAutoCancel(true)

        val id = NOTIFICATION_ID // notification id is a unique int for each notification that you must define
        NotificationManagerCompat.from(this).notify(id, builder.build())
        return id
    }

    private fun setSavedNotification(id: Int, uri: Uri) {
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.setDataAndType(uri, "image/*")
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(this, AR_CHANNEL_ID)
            .setSmallIcon(R.drawable.appicon)
            .setContentTitle("Save completed")
            .addAction(NotificationCompat.Action.Builder(R.drawable.appicon, "Open", pendingIntent).build())
            .setAutoCancel(true)

        NotificationManagerCompat.from(this).notify(id, builder.build())
    }

    private fun setErrorNotification(id: Int) {
        val builder = NotificationCompat.Builder(this, AR_CHANNEL_ID)
            .setSmallIcon(R.drawable.appicon)
            .setContentTitle("Error, save failed")
            .setAutoCancel(true)

        NotificationManagerCompat.from(this).notify(id, builder.build())
    }

    companion object {
        private const val AR_CHANNEL_ID = "AugmentedReality"
        private const val NOTIFICATION_ID = 164253
        private const val EXTRA_ASSET_URI = "AssetUri"
        private const val EXTRA_FOLDER_NAME = "FolderName"

        fun newIntent(context: Context, uri: String, folderName: String): Intent {
            val intent = Intent(context, PreviewActivity::class.java)
            intent.putExtra(EXTRA_ASSET_URI, uri)
            intent.putExtra(EXTRA_FOLDER_NAME, folderName)
            return intent
        }
    }
}
