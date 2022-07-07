package com.genexus.android.core.controls.maps

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.location.Location
import android.os.IBinder
import com.genexus.android.core.base.services.ILocationProvider
import com.genexus.android.core.base.services.Services

abstract class LocationProvider(protected val context: Context) : ILocationProvider {

    private var service: LocationUpdatesService? = null
    private var pendingLocationRequest: LocationRequestParameters? = null
    private var currentlyBinding = false

    override fun requestLocationUpdates(requestParameters: LocationRequestParameters): Boolean {
        if (service != null)
            service?.requestLocationUpdates(requestParameters)
        else if (currentlyBinding)
            pendingLocationRequest = requestParameters

        return isReady()
    }

    override fun removeLocationUpdates(): Boolean {
        service?.apply {
            removeLocationUpdates()
            try { context.unbindService(serviceConnection) } catch (ignored: IllegalArgumentException) { }
            stopSelf()
            service = null
        }
        return true
    }

    override fun getLastKnownLocation(): Location? {
        return service?.getLastKnownLocation() ?: calculateCurrentLocation()
    }

    private fun calculateCurrentLocation(): Location? {
        if (Services.Application.lifecycle.isForeground()) {
            val oneTimeIntent = getService().apply { putExtra(KEY_ONE_TIME, true) }
            context.startService(oneTimeIntent)
        }

        return null // Location will be received through Services.Location.onLocationChanged
    }

    override fun bind() {
        currentlyBinding = context.bindService(getService(), serviceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun isReady(): Boolean {
        return service != null || currentlyBinding
    }

    private val serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, binder: IBinder) {
            service = (binder as LocationUpdatesService.LocalBinder).service
            currentlyBinding = false
            pendingLocationRequest?.let {
                requestLocationUpdates(it)
                pendingLocationRequest = null
            }

            // Unbind from the service. This signals to the service that this activity is no longer
            // in the foreground, and the service can respond by promoting itself to a foreground service.
            if (service?.isForeground == true)
                context.unbindService(this)
        }

        override fun onServiceDisconnected(name: ComponentName) {
            service = null
        }
    }

    private fun getService(): Intent {
        return Intent(context, getServiceClass())
    }

    protected abstract fun getServiceClass(): Class<out ILocationUpdatesService>

    companion object {
        const val KEY_ONE_TIME = "ONETIME"
    }
}
