package com.genexus.android.core.externalobjects.permissions

import android.Manifest
import android.os.Build
import com.genexus.android.core.base.services.Services

internal class Permissions(permissionDomainList: List<String>) : ArrayList<String>() {
    constructor(permissionDomain: String) : this(listOf(permissionDomain))

    init {
        for (p in permissionDomainList)
            addPermission(p)
    }

    private fun addPermission(permissionDomain: String?) {
        when (permissionDomain) {
            null -> {}
            BLUETOOTH -> {
                add(Manifest.permission.BLUETOOTH)
                add(Manifest.permission.BLUETOOTH_ADMIN)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                    add(Manifest.permission.BLUETOOTH_CONNECT)
            }
            CALENDAR -> {
                add(Manifest.permission.READ_CALENDAR)
                add(Manifest.permission.WRITE_CALENDAR)
            }
            CAMERA -> add(Manifest.permission.CAMERA)
            CONTACTS_READ_WRITE -> {
                add(Manifest.permission.READ_CONTACTS)
                add(Manifest.permission.WRITE_CONTACTS)
            }
            LOCATION_BACKGROUND -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                    add(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
            }
            LOCATION_COARSE -> add(Manifest.permission.ACCESS_COARSE_LOCATION)
            LOCATION_FINE -> add(Manifest.permission.ACCESS_FINE_LOCATION)
            MICROPHONE -> add(Manifest.permission.RECORD_AUDIO)
            STORAGE_READ_WRITE -> {
                add(Manifest.permission.READ_EXTERNAL_STORAGE)
                add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
            else -> Services.Log.error(String.format("Permission request for '%s' not supported", permissionDomain))
        }
    }

    companion object {
        private const val BLUETOOTH = "Bluetooth"
        private const val CALENDAR = "Calendar"
        private const val CAMERA = "Camera"
        private const val CONTACTS_READ_WRITE = "Contacts"
        private const val LOCATION_BACKGROUND = "LocationBackground"
        private const val LOCATION_COARSE = "LocationCoarse"
        private const val LOCATION_FINE = "LocationFine"
        private const val MICROPHONE = "Microphone"
        private const val STORAGE_READ_WRITE = "Storage"
    }
}
