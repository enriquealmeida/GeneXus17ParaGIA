package com.genexus.android.maps

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.view.animation.LinearInterpolator
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.controls.maps.GxMapViewDefinition
import com.genexus.android.core.controls.maps.common.IMapLocation
import kotlin.math.abs
import kotlin.math.atan

abstract class IMarkerAnimation<Marker>(val marker: Marker) {
    private lateinit var startPosition: IMapLocation
    private lateinit var endPosition: IMapLocation
    private var handler: Handler? = null
    private var valueAnimator: ValueAnimator? = null
    private var animationDuration = 0
    private var endBehavior = 0

    fun startAnimation(endPositionValue: IMapLocation, durationValue: Int, endBehaviorValue: Int) {
        endPosition = endPositionValue
        animationDuration = durationValue
        endBehavior = endBehaviorValue
        startPosition = getMarkerPosition()
        Services.Log.debug("Starting Animation - duration $animationDuration, to $endPosition, from $startPosition")
        if (startPosition == endPositionValue) {
            Services.Log.debug("Animation cancelled - same position")
            return
        }

        cancelAnimation()
        handler = Handler(Looper.getMainLooper())
        handler?.post(runnableAnimation)
    }

    private fun cancelAnimation() {
        handler?.removeCallbacks(runnableAnimation)
        if (endBehavior == GxMapViewDefinition.ANIMATION_REPEAT)
            valueAnimator?.cancel()
    }

    @SuppressLint("Recycle")
    private val runnableAnimation: Runnable = object : Runnable {
        override fun run() {
            setMarkerVisible(true)
            valueAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
                duration = animationDuration.toLong()
                interpolator = LinearInterpolator()
                addUpdateListener { valueAnimatorVar ->
                    val v = valueAnimatorVar.animatedFraction
                    val lng = v * endPosition.longitude + (1 - v) * startPosition.longitude
                    val lat = v * endPosition.latitude + (1 - v) * startPosition.latitude
                    setMarkerPosition(lat, lng)
                    setMarkerAnchor(0.5f, 0.5f)
                    Services.Maps.newMapLocation(lat, lng)?.let {
                        setMarkerRotation(getBearing(startPosition, it))
                    }
                }
            }
            valueAnimator?.start()
            if (endBehavior == GxMapViewDefinition.ANIMATION_REPEAT)
                handler?.postDelayed(this, animationDuration.toLong())
            if (endBehavior == GxMapViewDefinition.ANIMATION_DISAPPEAR)
                handler?.postDelayed(runnableDisappear, animationDuration.toLong())
        }
    }

    private val runnableDisappear = Runnable { setMarkerVisible(false) }

    private fun getBearing(begin: IMapLocation, end: IMapLocation): Float {
        val lat = abs(begin.latitude - end.latitude)
        val lng = abs(begin.longitude - end.longitude)
        val toDegrees = Math.toDegrees(atan(lng / lat))
        return if (begin.latitude < end.latitude && begin.longitude < end.longitude)
            toDegrees.toFloat()
        else if (begin.latitude >= end.latitude && begin.longitude < end.longitude)
            (90 - toDegrees + 90).toFloat()
        else if (begin.latitude >= end.latitude && begin.longitude >= end.longitude)
            (toDegrees + 180).toFloat()
        else /* if (begin.latitude < end.latitude && begin.longitude >= end.longitude) */
            (90 - toDegrees + 270).toFloat()
    }

    abstract fun getMarkerPosition(): IMapLocation
    abstract fun setMarkerPosition(lat: Double, lng: Double)
    abstract fun setMarkerVisible(visible: Boolean)
    abstract fun setMarkerRotation(rotation: Float)
    abstract fun setMarkerAnchor(i: Float, k: Float)
}
