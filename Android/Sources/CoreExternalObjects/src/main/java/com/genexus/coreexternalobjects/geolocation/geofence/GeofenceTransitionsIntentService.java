package com.genexus.coreexternalobjects.geolocation.geofence;

import java.util.ArrayList;
import java.util.List;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.artech.activities.ActivityHelper;
import com.artech.activities.IntentFactory;
import com.artech.application.MyApplication;
import com.artech.base.services.Services;
import com.artech.base.utils.Strings;
import com.genexus.coreexternalobjects.geolocation.ProximityAlertsHelper;
import com.genexus.coreexternalobjects.geolocation.db.ProximityAlert;
import com.genexus.coreexternalobjects.geolocation.db.ProximityAlertsSQLiteHelper;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;


public class GeofenceTransitionsIntentService extends IntentService {

	protected static final String TAG = "GeofenceTransitionsIS";

	private static ProximityAlertsHelper mProximityAlertsHelper;

	/**
	 * This constructor is required, and calls the super IntentService(String)
	 * constructor with the name for a worker thread.
	 */
	public GeofenceTransitionsIntentService() {
		// Use the TAG to name the worker thread.
		super(TAG);
		mProximityAlertsHelper = ProximityAlertsHelper.getInstance();
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
		if (geofencingEvent.hasError()) {
			String errorMessage = "Geofence ErrorMessages code: " + geofencingEvent.getErrorCode();
			Services.Log.error(TAG, errorMessage);
			return;
		}

		// Get the transition type.
		int geofenceTransition = geofencingEvent.getGeofenceTransition();

		// Test that the reported transition was of interest.
		if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
			geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {

			// Get the geofences that were triggered. A single event can trigger
			// multiple geofences.
			List triggeringGeofences = geofencingEvent.getTriggeringGeofences();

			Services.Log.debug("geofence onHandleIntent ");

			// Get the transition details as a String.
			String geofenceTransitionDetails = getGeofenceTransitionDetails(
				this,
				geofenceTransition,
				triggeringGeofences
			);
			ProximityAlert proximityAlert = getGeofenceTransitionProximityAlert(triggeringGeofences);

			// Send notification and log the transition details.
			Services.Log.debug(geofenceTransitionDetails);
			if (proximityAlert != null) {
				mProximityAlertsHelper.setCurrentProximityAlert(proximityAlert);
				sendNotification(proximityAlert);
			} else {
				Services.Log.error("Proximity alert not found");
			}
		} else {
			// Log the error.
			Services.Log.error(TAG, "geofence_transition_invalid_type " + geofenceTransition);
		}
	}

	/**
	 * Gets transition details and returns them as a formatted string.
	 *
	 * @param context             The app context.
	 * @param geofenceTransition  The ID of the geofence transition.
	 * @param triggeringGeofences The geofence(s) triggered.
	 * @return The transition details formatted as String.
	 */
	private String getGeofenceTransitionDetails(Context context, int geofenceTransition, List<Geofence> triggeringGeofences) {

		String geofenceTransitionString = getTransitionString(geofenceTransition);

		// Get the Ids of each geofence that was triggered.
		ArrayList triggeringGeofencesIdsList = new ArrayList();
		for (Geofence geofence : triggeringGeofences) {
			Services.Log.debug("Geofence Id: " + geofence.getRequestId());
			triggeringGeofencesIdsList.add(geofence.getRequestId());
		}
		String triggeringGeofencesIdsString = TextUtils.join(", ", triggeringGeofencesIdsList);
		return geofenceTransitionString + ": " + triggeringGeofencesIdsString;
	}

	private ProximityAlert getGeofenceTransitionProximityAlert(List<Geofence> triggeringGeofences) {
		ProximityAlert proximityAlert = null;

		ProximityAlertsSQLiteHelper db = new ProximityAlertsSQLiteHelper(MyApplication.getAppContext());

		// Get the Ids of each geofence that was triggered.
		for (Geofence geofence : triggeringGeofences) {
			Services.Log.debug("Geofence Id: " + geofence.getRequestId());
			proximityAlert = db.getProximityAlerts(Integer.parseInt(geofence.getRequestId()));
			if (proximityAlert != null) {
				return proximityAlert;
			}
		}
		return proximityAlert;
	}

	/**
	 * Maps geofence transition types to their human-readable equivalents.
	 *
	 * @param transitionType A transition type constant defined in Geofence
	 * @return A String indicating the type of transition
	 */
	private String getTransitionString(int transitionType) {
		switch (transitionType) {
			case Geofence.GEOFENCE_TRANSITION_ENTER:
				return "geofence_transition_entered";
			case Geofence.GEOFENCE_TRANSITION_EXIT:
				return "geofence_transition_exited";
			default:
				return "unknown_geofence_transition";
		}
	}


	/**
	 * Posts a notification in the notification bar when a transition is detected.
	 * If the user clicks the notification, control goes to the MainActivity.
	 */
	private void sendNotification(ProximityAlert proximityAlert) {
		Services.Log.debug("proximityAlert " + proximityAlert.getName() + " , " + proximityAlert.getDescription() + " , " + proximityAlert.getActionName());

		// if has action dispatch silent action
		if (Services.Strings.hasValue(proximityAlert.getActionName())) {
			Services.Notifications.executeNotificationAction(ActivityHelper.getCurrentActivity(), proximityAlert.getActionName(), Strings.EMPTY);
		} else {
			// create standard notification with title, payload, action, parameters
			Intent actionIntent = IntentFactory.createNotificationActionIntent(this, Strings.EMPTY, Strings.EMPTY, false);
			Services.Notifications.showNotification(-1, proximityAlert.getName(), proximityAlert.getDescription(), actionIntent);
		}
	}
}
