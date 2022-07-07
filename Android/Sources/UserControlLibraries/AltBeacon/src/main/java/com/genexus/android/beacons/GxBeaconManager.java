package com.genexus.android.beacons;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.BeaconTransmitter;
import org.altbeacon.beacon.BleNotAvailableException;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Build;

import com.genexus.android.core.actions.ApiAction;
import com.genexus.android.core.actions.ExternalObjectEvent;
import com.genexus.android.ApiAuthorizationStatus;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.MultiMap;
import com.genexus.android.core.base.utils.NameMap;
import com.genexus.android.core.base.utils.Strings;

class GxBeaconManager {

	private final Context mContext;
	private final BeaconManager mBeaconManager;
	private static GxBeaconManager sInstance;
	private BeaconTransmitter mBeaconTransmitter;

	private Boolean mAreProximityAlertsAvailable;
	private final ProximityAlertsDatabase mProximityAlerts;

	private static final String EVENT_ENTER_BEACON_REGION = "EnterBeaconRegion";
	private static final String EVENT_EXIT_BEACON_REGION = "ExitBeaconRegion";
	private static final String EVENT_CHANGE_BEACONS_IN_REGION = "ChangeBeaconsInRange";

	private static final String IBEACON_LAYOUT = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24";

	private OnChangeRegionListener mEnterRegionListener;
	private OnChangeRegionListener mExitRegionListener;
	private OnChangeBeaconsListener mChangeBeaconsInRangeListener;

	private final NameMap<Integer> mRegionStates;
	private final MultiMap<String, GxBeaconState> mCurrentBeacons;

	static synchronized GxBeaconManager getInstance(Activity activity) {
		if (sInstance == null) {
			sInstance = new GxBeaconManager(activity);
			sInstance.initialize();
		}
		
		return sInstance;
	}
	
	private GxBeaconManager(Activity activity) {
		BeaconManager.setDebug(BuildConfig.DEBUG);

		mContext = activity.getApplicationContext();
		mBeaconManager = BeaconManager.getInstanceForApplication(mContext);

		mProximityAlerts = new ProximityAlertsDatabase(mContext);
		mRegionStates = new NameMap<>();
		mCurrentBeacons = new MultiMap<>();

		mBeaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(IBEACON_LAYOUT));
		mBeaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(BeaconParser.ALTBEACON_LAYOUT));

		mBeaconManager.addMonitorNotifier(mMonitorNotifier);
		mBeaconManager.addRangeNotifier(mRangeNotifier);
	}

	private void initialize() {
		final ExternalObjectEvent enterBeaconRegion = new ExternalObjectEvent(BeaconsAPI.OBJECT_NAME, EVENT_ENTER_BEACON_REGION);
		final ExternalObjectEvent exitBeaconRegion = new ExternalObjectEvent(BeaconsAPI.OBJECT_NAME, EVENT_EXIT_BEACON_REGION);
		final ExternalObjectEvent changeBeaconsInRange = new ExternalObjectEvent(BeaconsAPI.OBJECT_NAME, EVENT_CHANGE_BEACONS_IN_REGION);

		this.setOnEnterRegionListener(region -> enterBeaconRegion
				.fire(Collections.singletonList(BeaconsEntitiesFactory.regionToEntity(region))));
		this.setOnExitRegionListener(region -> exitBeaconRegion
				.fire(Collections.singletonList(BeaconsEntitiesFactory.regionToEntity(region))));
		this.setOnChangeBeaconsInRangeListener((region, beacons) -> changeBeaconsInRange
				.fire(Arrays.asList(BeaconsEntitiesFactory.regionToEntity(region),
						BeaconsEntitiesFactory.beaconStatesToEntityList(beacons))));

		this.restoreSavedProximityAlerts();
	}

	private void setOnEnterRegionListener(OnChangeRegionListener listener) {
		mEnterRegionListener = listener;
	}

	private void setOnExitRegionListener(OnChangeRegionListener listener) {
		mExitRegionListener = listener;
	}

	private void setOnChangeBeaconsInRangeListener(OnChangeBeaconsListener listener) {
		mChangeBeaconsInRangeListener = listener;
	}

	boolean isServiceEnabled() {
		final BluetoothManager bluetoothManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
		BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
		return bluetoothAdapter != null && bluetoothAdapter.isEnabled() && Services.Location.isEnabled();
	}

	boolean isRangingAvailable() {
		try {
			return mBeaconManager.checkAvailability();
		} catch (BleNotAvailableException ex) {
			Services.Log.error("Ranging Service not available:", ex);
			return false;
		}
	}

	boolean areProximityAlertsAvailable() {
		if (mAreProximityAlertsAvailable == null) {
			mAreProximityAlertsAvailable = true;
		}
		return mAreProximityAlertsAvailable;
	}

	int getAuthorizationStatus() {
		boolean authorizationStatus = isAuthorized() && Services.Location.isEnabled();
		return authorizationStatus ? ApiAuthorizationStatus.AUTHORIZED : ApiAuthorizationStatus.DENIED;
	}

	private boolean isAuthorized() {
		return ApiAuthorizationStatus.getStatus(mContext, getBasicPermissions()) == ApiAuthorizationStatus.AUTHORIZED;
	}

	private static String[] getBasicPermissions() {
		ArrayList<String> permissions = new ArrayList<>();
		permissions.add(Manifest.permission.BLUETOOTH);
		permissions.add(Manifest.permission.BLUETOOTH_ADMIN);
		permissions.add(Manifest.permission.RECEIVE_BOOT_COMPLETED);
		permissions.addAll(Arrays.asList(Services.Location.getRequestPermissions()));
		return permissions.toArray(new String[0]);
	}

	static String[] getRangingPermissions() {
		ArrayList<String> permissions = new ArrayList<>(Arrays.asList(getBasicPermissions()));
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
			permissions.add(Manifest.permission.BLUETOOTH_SCAN);
			permissions.add(Manifest.permission.BLUETOOTH_CONNECT);
		}

		return permissions.toArray(new String[0]);
	}

	static String[] getAdvertisingPermissions() {
		ArrayList<String> permissions = new ArrayList<>(Arrays.asList(getRangingPermissions()));
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
			permissions.add(Manifest.permission.BLUETOOTH_ADVERTISE);

		return permissions.toArray(new String[0]);
	}

	boolean addProximityAlert(GxBeaconProximityAlert alert) {
		ArrayList<GxBeaconProximityAlert> alerts = new ArrayList<>();
		alerts.add(alert);
		return addProximityAlerts(alerts);
	}

	boolean addProximityAlerts(List<GxBeaconProximityAlert> alerts) {
		for (GxBeaconProximityAlert alert : alerts) {
			mProximityAlerts.addProximityAlert(alert);
			mBeaconManager.startMonitoring(alert.getRegion().toRegion());
		}
		return true;
	}

	List<GxBeaconProximityAlert> getProximityAlerts() {
		return mProximityAlerts.getProximityAlerts();
	}

	void removeProximityAlert(String regionId) {
		mProximityAlerts.removeProximityAlert(regionId);
		mBeaconManager.stopMonitoring(new Region(regionId, null, null, null));
		mRegionStates.remove(regionId);
	}

	void clearProximityAlerts() {
		for (Region region : mBeaconManager.getMonitoredRegions())
			removeProximityAlert(region.getUniqueId());

		mProximityAlerts.clearProximityAlerts();
		mRegionStates.clear();
	}

	int getRegionState(String regionId) {
		Integer state = mRegionStates.get(regionId);
		if (state != null)
			return state;
		else
			return GxBeaconRegion.STATE_UNKNOWN;
	}

	boolean startRangingRegion(GxBeaconRegion region) {
		mBeaconManager.startRangingBeacons(region.toRegion());
		return true;
	}

	void stopRangingRegion(String regionId) {
		Region region = new Region(regionId, null, null, null);
		mBeaconManager.stopRangingBeacons(region);
	}

	List<GxBeaconRegion> getRangedRegions() {
		return GxBeaconRegion.newCollection(mBeaconManager.getRangedRegions());
	}

	List<GxBeaconState> getBeaconsInRange(String regionId) {
		ArrayList<GxBeaconState> beacons = new ArrayList<>();

		if (Strings.hasValue(regionId))
			beacons.addAll(mCurrentBeacons.get(regionId));
		else
			beacons.addAll(mCurrentBeacons.values());

		return beacons;
	}

	boolean startAsBeacon(GxBeaconInfo beaconInfo, ApiAction apiAction) {
		if (BeaconTransmitter.checkTransmissionSupported(mContext) != BeaconTransmitter.SUPPORTED) {
			return false;
		} else {
			Beacon beacon = BeaconsEntitiesFactory.gxBeaconInfoToBeacon(beaconInfo);
			BeaconParser beaconParser = new BeaconParser().setBeaconLayout(BeaconParser.ALTBEACON_LAYOUT);
			mBeaconTransmitter = new BeaconTransmitter(mContext, beaconParser);
			mBeaconTransmitter.startAdvertising(beacon, new BeaconCallback(apiAction));
			return true;
		}
	}

	boolean stopAsBeacon() {
		if (mBeaconTransmitter != null) {
			mBeaconTransmitter.stopAdvertising();
			mBeaconTransmitter = null;
		}
		return true;
	}

	private final MonitorNotifier mMonitorNotifier = new MonitorNotifier() {
		@Override
		public void didEnterRegion(Region region) {
			Services.Log.debug("didEnterRegion " + region.getUniqueId());
			GxBeaconProximityAlert alert = mProximityAlerts.getProximityAlert(region.getUniqueId());
			if (alert != null && alert.shouldNotifyOnEntry()) {
				Services.Log.debug("Alert is not null and should be notified on entry");
				if (mEnterRegionListener != null)
					mEnterRegionListener.onChangeRegion(new GxBeaconRegion(region));
			}
		}

		@Override
		public void didExitRegion(Region region) {
			Services.Log.debug("didExitRegion " + region.getUniqueId());
			GxBeaconProximityAlert alert = mProximityAlerts.getProximityAlert(region.getUniqueId());
			if (alert != null && alert.shouldNotifyOnExit()) {
				Services.Log.debug("Alert is not null and should be notified on exit");
				if (mExitRegionListener != null)
					mExitRegionListener.onChangeRegion(new GxBeaconRegion(region));
			}
		}

		@Override
		public void didDetermineStateForRegion(int state, Region region) {
			int gxState = GxBeaconRegion.STATE_UNKNOWN;
			if (state == MonitorNotifier.INSIDE)
				gxState = GxBeaconRegion.STATE_INSIDE;
			else if (state == MonitorNotifier.OUTSIDE)
				gxState = GxBeaconRegion.STATE_OUTSIDE;

			mRegionStates.put(region.getUniqueId(), gxState);
			Services.Log.debug("didDetermineStateForRegion " + gxState + " " + region.getUniqueId());
		}
	};

	private final RangeNotifier mRangeNotifier = new RangeNotifier() {
		@Override
		public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
			Services.Log.debug("didRangeBeaconsInRegion " + beacons.toString() + " " + region.getUniqueId());
			GxBeaconRegion gxRegion = new GxBeaconRegion(region);

			// Special case: Although the base API reports on every scan when ranging is enabled,
			// we DON'T fire events whenever we had 0 beacons in range and we still have 0.
			// This is done (according to the spec) to prevent "useless" events.
			if (beacons.size() == 0 && mCurrentBeacons.get(gxRegion.getId()).size() == 0)
				return;

			List<GxBeaconState> gxBeacons = GxBeaconState.newCollection(beacons);

			mCurrentBeacons.clear(gxRegion.getId());
			mCurrentBeacons.putAll(gxRegion.getId(), gxBeacons);

			if (mChangeBeaconsInRangeListener != null)
				mChangeBeaconsInRangeListener.onChangeBeacons(gxRegion, gxBeacons);
		}
	};

	interface OnChangeRegionListener {
		void onChangeRegion(GxBeaconRegion region);
	}

	interface OnChangeBeaconsListener {
		void onChangeBeacons(GxBeaconRegion region, List<GxBeaconState> beacons);
	}

	private void restoreSavedProximityAlerts() {
		List<GxBeaconProximityAlert> savedAlerts = mProximityAlerts.getProximityAlerts();
		addProximityAlerts(savedAlerts);
	}
}
