package com.genexus.android.beacons;

import androidx.annotation.NonNull;

import com.genexus.android.core.actions.ApiAction;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.model.EntityList;
import com.genexus.android.core.externalapi.ExternalApi;
import com.genexus.android.core.externalapi.ExternalApiResult;

import java.util.List;

public class BeaconsAPI extends ExternalApi {

	static final String OBJECT_NAME = "GeneXus.SD.Beacons";

	private static final String PROPERTY_RANGING_AVAILABLE = "RangingAvailable";
	private static final String PROPERTY_PROXIMITY_ALERTS_AVAILABLE = "BeaconProximityAlertsAvailable";
	private static final String PROPERTY_SERVICE_ENABLED = "ServiceEnabled";
	private static final String PROPERTY_AUTHORIZATION_STATUS = "AuthorizationStatus";

	private static final String METHOD_ADD_PROXIMITY_ALERT = "AddBeaconProximityAlert";
	private static final String METHOD_ADD_PROXIMITY_ALERTS = "AddBeaconProximityAlerts";
	private static final String METHOD_GET_PROXIMITY_ALERTS = "GetBeaconProximityAlerts";
	private static final String METHOD_REMOVE_PROXIMITY_ALERT = "RemoveBeaconProximityAlert";
	private static final String METHOD_CLEAR_PROXIMITY_ALERTS = "ClearBeaconProximityAlerts";
	private static final String METHOD_GET_REGION_STATE = "GetBeaconRegionState";
	private static final String METHOD_START_RANGING_REGION = "StartRangingBeaconRegion";
	private static final String METHOD_GET_RANGING_REGIONS = "GetRangedBeaconRegions";
	private static final String METHOD_STOP_RANGING_REGION = "StopRangingBeaconRegion";
	private static final String METHOD_GET_BEACONS_IN_RANGE = "GetBeaconsInRange";
	private static final String METHOD_START_AS_BEACON = "StartAsBeacon";
	private static final String METHOD_STOP_AS_BEACON = "StopAsBeacon";

	private static GxBeaconManager mBeaconManager;

	public BeaconsAPI(ApiAction action) {
		super(action);

		mBeaconManager = GxBeaconManager.getInstance(getActivity());

		addMethodHandlerRequestingPermissions(METHOD_ADD_PROXIMITY_ALERT, 1, GxBeaconManager.getRangingPermissions(), mAddProximityAlertMethod);
		addMethodHandlerRequestingPermissions(METHOD_ADD_PROXIMITY_ALERTS, 1, GxBeaconManager.getRangingPermissions(), mAddProximityAlertsMethod);
		addMethodHandlerRequestingPermissions(METHOD_START_RANGING_REGION, 1, GxBeaconManager.getRangingPermissions(), mStartRangingRegionMethod);
		addMethodHandlerRequestingPermissions(METHOD_START_AS_BEACON, 1, GxBeaconManager.getAdvertisingPermissions(), mStartAsBeaconMethod);
		addReadonlyPropertyHandler(PROPERTY_RANGING_AVAILABLE, mGetRangingAvailableProperty);
		addReadonlyPropertyHandler(PROPERTY_PROXIMITY_ALERTS_AVAILABLE, mGetProximityAlertsAvailableProperty);
		addReadonlyPropertyHandler(PROPERTY_AUTHORIZATION_STATUS, mGetAuthorizationStatusProperty);
		addReadonlyPropertyHandler(PROPERTY_SERVICE_ENABLED, mGetServiceEnabledProperty);
		addMethodHandler(METHOD_GET_PROXIMITY_ALERTS, 0, mGetProximityAlertsMethod);
		addMethodHandler(METHOD_REMOVE_PROXIMITY_ALERT, 1, mRemoveProximityAlertMethod);
		addMethodHandler(METHOD_CLEAR_PROXIMITY_ALERTS, 0, mClearProximityAlertsMethod);
		addMethodHandler(METHOD_GET_REGION_STATE, 1, mGetRegionStateMethod);
		addMethodHandler(METHOD_GET_RANGING_REGIONS, 0, mGetRangingRegionsMethod);
		addMethodHandler(METHOD_STOP_RANGING_REGION, 1, mStopRangingRegionMethod);
		addMethodHandler(METHOD_GET_BEACONS_IN_RANGE, 1, mGetBeaconsInRangeMethod);
		addMethodHandler(METHOD_STOP_AS_BEACON, 0, mStopAsBeaconMethod);
	}

	private final IMethodInvoker mGetRangingAvailableProperty = parameters ->
			ExternalApiResult.success(mBeaconManager.isRangingAvailable());

	private final IMethodInvoker mGetProximityAlertsAvailableProperty = parameters ->
			ExternalApiResult.success(mBeaconManager.areProximityAlertsAvailable());

	private final IMethodInvoker mGetServiceEnabledProperty = parameters ->
			ExternalApiResult.success(mBeaconManager.isServiceEnabled());

	private final IMethodInvoker mGetAuthorizationStatusProperty = parameters ->
			ExternalApiResult.success(mBeaconManager.getAuthorizationStatus());

	private final IMethodInvoker mAddProximityAlertMethod = new IMethodInvoker() {
		@Override
		public @NonNull ExternalApiResult invoke(List<Object> parameters) {
			GxBeaconProximityAlert alert = new GxBeaconProximityAlert((Entity) parameters.get(0));
			boolean result = mBeaconManager.addProximityAlert(alert);
			return ExternalApiResult.success(result);
		}
	};

	private final IMethodInvoker mAddProximityAlertsMethod = new IMethodInvoker() {
		@Override
		public @NonNull ExternalApiResult invoke(List<Object> parameters) {
			List<GxBeaconProximityAlert> alerts = GxBeaconProximityAlert.newCollection((EntityList) parameters.get(0));
			boolean result = mBeaconManager.addProximityAlerts(alerts);
			return ExternalApiResult.success(result);
		}
	};

	private final IMethodInvoker mGetProximityAlertsMethod = new IMethodInvoker() {
		@Override
		public @NonNull ExternalApiResult invoke(List<Object> parameters) {
			List<GxBeaconProximityAlert> alerts = mBeaconManager.getProximityAlerts();
			EntityList result = BeaconsEntitiesFactory.proximityAlertsToEntityList(alerts);
			return ExternalApiResult.success(result);
		}
	};

	private final IMethodInvoker mRemoveProximityAlertMethod = new IMethodInvoker() {
		@Override
		public @NonNull ExternalApiResult invoke(List<Object> parameters) {
			String regionId = parameters.get(0).toString();
			mBeaconManager.removeProximityAlert(regionId);
			return ExternalApiResult.SUCCESS_CONTINUE;
		}
	};

	private final IMethodInvoker mClearProximityAlertsMethod = new IMethodInvoker() {
		@Override
		public @NonNull ExternalApiResult invoke(List<Object> parameters) {
			mBeaconManager.clearProximityAlerts();
			return ExternalApiResult.SUCCESS_CONTINUE;
		}
	};

	private final IMethodInvoker mGetRegionStateMethod = new IMethodInvoker() {
		@Override
		public @NonNull ExternalApiResult invoke(List<Object> parameters) {
			String regionId = parameters.get(0).toString();
			int result = mBeaconManager.getRegionState(regionId);
			return ExternalApiResult.success(result);
		}
	};

	private final IMethodInvoker mStartRangingRegionMethod = new IMethodInvoker() {
		@Override
		public @NonNull ExternalApiResult invoke(List<Object> parameters) {
			GxBeaconRegion region = new GxBeaconRegion((Entity) parameters.get(0));
			boolean result = mBeaconManager.startRangingRegion(region);
			return ExternalApiResult.success(result);
		}
	};

	private final IMethodInvoker mGetRangingRegionsMethod = new IMethodInvoker() {
		@Override
		public @NonNull ExternalApiResult invoke(List<Object> parameters) {
			List<GxBeaconRegion> regions = mBeaconManager.getRangedRegions();
			EntityList result = BeaconsEntitiesFactory.regionsToEntityList(regions);
			return ExternalApiResult.success(result);
		}
	};

	private final IMethodInvoker mStopRangingRegionMethod = new IMethodInvoker() {
		@Override
		public @NonNull ExternalApiResult invoke(List<Object> parameters) {
			String regionId = parameters.get(0).toString();
			mBeaconManager.stopRangingRegion(regionId);
			return ExternalApiResult.SUCCESS_CONTINUE;
		}
	};

	private final IMethodInvoker mGetBeaconsInRangeMethod = new IMethodInvoker() {
		@Override
		public @NonNull ExternalApiResult invoke(List<Object> parameters) {
			String regionId = parameters.get(0).toString();
			EntityList result = BeaconsEntitiesFactory.beaconStatesToEntityList(mBeaconManager.getBeaconsInRange(regionId));
			return ExternalApiResult.success(result);
		}
	};

	private final IMethodInvoker mStartAsBeaconMethod = new IMethodInvoker() {
		@Override
		public @NonNull ExternalApiResult invoke(List<Object> parameters) {
			GxBeaconInfo beaconInfo = new GxBeaconInfo((Entity) parameters.get(0));
			return mBeaconManager.startAsBeacon(beaconInfo, getAction()) ? ExternalApiResult.SUCCESS_WAIT : ExternalApiResult.success(false);
		}
	};

	private final IMethodInvoker mStopAsBeaconMethod = new IMethodInvoker() {
		@Override
		public @NonNull ExternalApiResult invoke(List<Object> parameters) {
			return ExternalApiResult.success(mBeaconManager.stopAsBeacon());
		}
	};
}
