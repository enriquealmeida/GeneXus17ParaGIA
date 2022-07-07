package com.genexus.android.controls.filepicker

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AlertDialog
import com.genexus.android.core.actions.ActionExecution
import com.genexus.android.core.actions.ApiAction
import com.genexus.android.core.base.metadata.expressions.Expression
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.externalapi.ExternalApiResult
import com.genexus.android.core.externalapi.superapps.SuperAppExternalApi

class FilePickerAPI(action: ApiAction) : SuperAppExternalApi(action) {

    private val chooseFile = object : IMethodInvokerWithActivityResult {
        override fun invoke(parameters: MutableList<Any>?): ExternalApiResult {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            if (parameters != null && parameters.count() >= 1) {
                val mimeTypes = parameters[0].toString().split(',').toTypedArray()
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            }
            try {
                startActivityForResult(Intent.createChooser(intent, "Select a File"), FILE_SELECT_CODE)
                return ExternalApiResult.SUCCESS_WAIT
            } catch (e: ActivityNotFoundException) {
                Services.Log.debug("FilePicker", "Chooser not found: $e")
                return ExternalApiResult.FAILURE
            }
        }

        override fun handleActivityResult(requestCode: Int, resultCode: Int, result: Intent?): ExternalApiResult {
            if (resultCode == RESULT_OK)
                return ExternalApiResult.success(result?.data.toString())
            else
                return ExternalApiResult.FAILURE
        }
    }

    private var fileUri = ""
    private var fileDestination = 0

    private val saveFile = object : IMethodInvoker {
        override fun invoke(parameters: MutableList<Any>?): ExternalApiResult {
            if (parameters != null && parameters.count() >= 2) {
                fileUri = parameters[0].toString()
                fileDestination = parameters[1].toString().toInt()

                // default saveMode is rename. Not replace by default.
                var replaceFile = false
                // ask to user
                var askUser = false
                if (parameters.count() >= 3) {
                    val saveMode = parameters[2].toString().toInt()
                    if (saveMode == SAVE_MODE_REPLACE)
                        replaceFile = true
                    if (saveMode == SAVE_MODE_ASK_USER)
                        askUser = true
                }

                if (askUser) {
                    // if askUser and Exists show dialog.
                    if (FilesAPIImpl.fileExits(context, fileUri, fileDestination)) {
                        // ask user if replace
                        val fileName = FilesAPIImpl.getFileName(fileUri)
                        Services.Device.runOnUiThread { confirmReplace(fileName) }
                        return ExternalApiResult.SUCCESS_WAIT
                    }
                }
                return ExternalApiResult.success(FilesAPIImpl.saveToFolder(context, fileUri, fileDestination, replaceFile))
            }
            return ExternalApiResult.success(false)
        }
    }

    private val folderAvailable = object : IMethodInvoker {
        override fun invoke(parameters: MutableList<Any>?): ExternalApiResult {
            return ExternalApiResult.success(true)
        }
    }

    private fun confirmReplace(fileName: String) {
        val currentActivity = activity
        if (currentActivity == null || currentActivity.isFinishing) {
            // cannot show dialog, cancel event.
            ActionExecution.cancelCurrent(action)
            return
        }

        val builder = AlertDialog.Builder(currentActivity)
        builder.setCancelable(false)
        builder.setMessage(String.format(Services.Strings.getResource(R.string.GXM_FileQuestion), fileName))
        builder.setPositiveButton(R.string.GXM_FileReplace) { _, _ -> onClickReplace() }
        builder.setNegativeButton(R.string.GXM_FileRename) { _, _ -> onClickRename() }
        builder.show()
    }

    private fun onClickReplace() {
        // ok replace
        val saveResult = FilesAPIImpl.saveToFolder(context, fileUri, fileDestination, true)

        // Set result and continue.
        setResultAndContinue(saveResult)
    }

    private fun onClickRename() {
        // cancel, rename duplicate
        val saveResult = FilesAPIImpl.saveToFolder(context, fileUri, fileDestination, false)

        // Set result and continue.
        setResultAndContinue(saveResult)
    }

    private fun setResultAndContinue(saveResult: Boolean) {
        // Set result to True.
        if (action != null && action.hasOutput()) action.setOutputValue(
            Expression.Value.newBoolean(saveResult)
        )
        // By default, continue execution.
        ActionExecution.continueCurrent(activity, false, action)
    }

    init {
        addMethodHandler(METHOD_CHOOSE_FILE, 0, chooseFile)
        addMethodHandler(METHOD_CHOOSE_FILE, 1, chooseFile)

        val permissions: MutableList<String> = ArrayList()
        // pre Q devices need write permission to write device folders.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        addMethodHandlerRequestingPermissions(METHOD_SAVE_TO_FOLDER, 2, permissions.toTypedArray(), saveFile)
        addMethodHandlerRequestingPermissions(METHOD_SAVE_TO_FOLDER, 3, permissions.toTypedArray(), saveFile)

        addMethodHandler(METHOD_FOLDER_AVAILABLE, 1, folderAvailable)
    }

    companion object {
        const val OBJECT_NAME = "GeneXus.SD.Media.Files"
        private const val FILE_SELECT_CODE = 324
        private const val METHOD_CHOOSE_FILE = "ChooseFile"
        private const val METHOD_SAVE_TO_FOLDER = "SaveToFolder"
        private const val METHOD_FOLDER_AVAILABLE = "IsDeviceFolderAvailable"

        const val SAVE_MODE_REPLACE = 2
        private const val SAVE_MODE_ASK_USER = 3
    }
}
