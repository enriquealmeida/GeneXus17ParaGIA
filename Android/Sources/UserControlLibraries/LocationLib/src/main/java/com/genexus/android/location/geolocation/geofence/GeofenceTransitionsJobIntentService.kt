package com.genexus.android.location.geolocation.geofence

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import androidx.core.app.JobIntentService
import com.genexus.android.core.activities.ActivityHelper
import com.genexus.android.core.activities.IntentFactory
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.base.utils.Strings
import com.genexus.android.location.geolocation.db.ProximityAlert
import com.genexus.android.location.geolocation.db.ProximityAlertsSQLiteHelper
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent

class GeofenceTransitionsJobIntentService : JobIntentService() {

    override fun onHandleWork(intent: Intent) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        if (geofencingEvent.hasError()) {
            val errorMessage = "Geofence ErrorMessages code: " + geofencingEvent.errorCode
            Services.Log.error(TAG, errorMessage)
            return
        }

        // Get the transition type.
        val geofenceTransition = geofencingEvent.geofenceTransition

        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
            geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT
        ) {

            // Get the geofences that were triggered. A single event can trigger
            // multiple geofences.
            val triggeringGeofences = geofencingEvent.triggeringGeofences
            Services.Log.debug("geofence onHandleIntent ")

            // Get the transition details as a String.
            val geofenceTransitionDetails = getGeofenceTransitionDetails(
                geofenceTransition,
                triggeringGeofences
            )
            val proximityAlert = getGeofenceTransitionProximityAlert(triggeringGeofences)

            // Send notification and log the transition details.
            Services.Log.debug(geofenceTransitionDetails)
            if (proximityAlert != null) {
                Services.Location.setCurrentProximityAlert(proximityAlert.id)
                sendNotification(proximityAlert)
            } else {
                Services.Log.error("Proximity alert not found")
            }
        } else {
            // Log the error.
            Services.Log.error(TAG, "geofence_transition_invalid_type $geofenceTransition")
        }
    }

    /**
     * Gets transition details and returns them as a formatted string.
     *
     * @param geofenceTransition The ID of the geofence transition.
     * @param triggeringGeofences The geofence(s) triggered.
     * @return The transition details formatted as String.
     */
    private fun getGeofenceTransitionDetails(geofenceTransition: Int, triggeringGeofences: List<Geofence>): String {
        val geofenceTransitionString = getTransitionString(geofenceTransition)

        // Get the Ids of each geofence that was triggered.
        val triggeringGeofencesIdsList = ArrayList<String>()
        for (geofence in triggeringGeofences) {
            Services.Log.debug("Geofence Id: " + geofence.requestId)
            triggeringGeofencesIdsList.add(geofence.requestId)
        }
        val triggeringGeofencesIdsString = TextUtils.join(", ", triggeringGeofencesIdsList)
        return "$geofenceTransitionString: $triggeringGeofencesIdsString"
    }

    private fun getGeofenceTransitionProximityAlert(triggeringGeofences: List<Geofence>): ProximityAlert? {
        val db = ProximityAlertsSQLiteHelper(Services.Application.appContext)

        // Get the Ids of each geofence that was triggered.
        for (geofence in triggeringGeofences) {
            val requestId = geofence.requestId.toInt()
            Services.Log.debug("Geofence Id: $requestId")
            val proximityAlert = db.getProximityAlerts(requestId)
            if (proximityAlert != null)
                return proximityAlert
        }

        return null
    }

    /**
     * Maps geofence transition types to their human-readable equivalents.
     *
     * @param transitionType A transition type constant defined in Geofence
     * @return A String indicating the type of transition
     */
    private fun getTransitionString(transitionType: Int): String {
        return when (transitionType) {
            Geofence.GEOFENCE_TRANSITION_ENTER -> "geofence_transition_entered"
            Geofence.GEOFENCE_TRANSITION_EXIT -> "geofence_transition_exited"
            else -> "unknown_geofence_transition"
        }
    }

    /**
     * Posts a notification in the notification bar when a transition is detected.
     * If the user clicks the notification, control goes to the MainActivity.
     */
    private fun sendNotification(proximityAlert: ProximityAlert) {
        Services.Log.debug("proximityAlert " + proximityAlert.name + " , " + proximityAlert.description + " , " + proximityAlert.actionName)

        // if has action dispatch silent action
        if (Services.Strings.hasValue(proximityAlert.actionName)) {
            Services.Notifications.executeNotificationAction(ActivityHelper.getCurrentActivity(), proximityAlert.actionName, Strings.EMPTY, Strings.EMPTY)
        } else {
            // create standard notification with title, payload, action, parameters
            val actionIntent = IntentFactory.createNotificationActionIntent(this, Strings.EMPTY, Strings.EMPTY, Strings.EMPTY, false)
            Services.Notifications.showNotification(-1, proximityAlert.name, proximityAlert.description, actionIntent)
        }
    }

    companion object {
        private const val TAG = "GeofenceTransitionsJIS"
        private const val JOB_ID = 573

        fun enqueueWork(context: Context, intent: Intent) {
            enqueueWork(context, GeofenceTransitionsJobIntentService::class.java, JOB_ID, intent)
        }
    }
}
