package com.genexus.android.controls.filepicker

import com.genexus.android.core.base.services.Services

class FilesAPIOffline {

    companion object {

        // static method to be called inside a offline procedure
        // use Short? because is needed to match with java.lang.Short by reflection.
        @JvmStatic
        fun saveToFolder(path: String, fileDestination: Short?): Boolean {
            return FilesAPIImpl.saveToFolder(
                Services.Application.getAppContext(), path,
                fileDestination?.toInt() ?: 0, false
            )
        }

        // use Short? because is needed to match with java.lang.Short by reflection.
        @JvmStatic
        fun saveToFolder(path: String, fileDestination: Short?, saveMode: Short?): Boolean {
            return FilesAPIImpl.saveToFolder(
                Services.Application.getAppContext(), path,
                fileDestination?.toInt()
                    ?: 0,
                saveMode?.toInt() == FilePickerAPI.SAVE_MODE_REPLACE
            )
        }
    }
}
