package com.genexus.android.beacons;

import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.model.EntityFactory;
import com.genexus.android.core.base.model.EntityList;

import org.altbeacon.beacon.Beacon;

import java.util.Collections;
import java.util.List;

class BeaconsEntitiesFactory {

	private static final String SDT_BEACON_PROXIMITY_ALERT = "GeneXus.SD.BeaconProximityAlert";
	private static final String SDT_BEACON_REGION = "GeneXus.SD.BeaconRegion";
	private static final String SDT_BEACON_STATE = "GeneXus.SD.BeaconState";
	private static final String SDT_BEACON_INFO = "GeneXus.SD.BeaconInfo";
	private static final String PROPERTY_BEACON = "Beacon";
	private static final String PROPERTY_PROXIMITY = "Proximity";
	private static final String PROPERTY_DISTANCE = "Distance";
	private static final String PROPERTY_SIGNAL = "Signal";
	static final String PROPERTY_BEACON_REGION = "BeaconRegion";
	static final String PROPERTY_NOTIFY_ON_ENTRY = "NotifyOnEntry";
	static final String PROPERTY_NOTIFY_ON_EXIT = "NotifyOnExit";
	static final String PROPERTY_BEACON_MATCH = "BeaconMatch";
	static final String PROPERTY_IDENTIFIER = "Identifier";
	static final String PROPERTY_UUID = "UUID";
	static final String PROPERTY_GROUP_ID = "GroupId";
	static final String PROPERTY_ID = "Id";

	static EntityList proximityAlertsToEntityList(List<GxBeaconProximityAlert> alerts) {
		EntityList list = new EntityList();
		for (GxBeaconProximityAlert alert : alerts)
			list.add(proximityAlertToEntity(alert));

		return list;
	}

	private static Entity proximityAlertToEntity(GxBeaconProximityAlert alert) {
		Entity entity = EntityFactory.newSdt(SDT_BEACON_PROXIMITY_ALERT);
		entity.setProperty(PROPERTY_BEACON_REGION, regionToEntity(alert.getRegion()));
		entity.setProperty(PROPERTY_NOTIFY_ON_ENTRY, alert.shouldNotifyOnEntry());
		entity.setProperty(PROPERTY_NOTIFY_ON_EXIT, alert.shouldNotifyOnExit());
		return entity;
	}

	static EntityList regionsToEntityList(List<GxBeaconRegion> regions) {
		EntityList list = new EntityList();
		for (GxBeaconRegion region : regions)
			list.add(regionToEntity(region));

		return list;
	}

	static Entity regionToEntity(GxBeaconRegion region) {
		Entity entity = EntityFactory.newSdt(SDT_BEACON_REGION);
		entity.setProperty(PROPERTY_IDENTIFIER, region.getId());
		entity.setProperty(PROPERTY_BEACON_MATCH, beaconToEntity(region.getBeaconMatch()));
		return entity;
	}

	static EntityList beaconStatesToEntityList(List<GxBeaconState> states) {
		EntityList list = new EntityList();
		for (GxBeaconState state : states)
			list.add(beaconStateToEntity(state));

		return list;
	}

	private static Entity beaconStateToEntity(GxBeaconState state) {
		Entity entity = EntityFactory.newSdt(SDT_BEACON_STATE);
		entity.setProperty(PROPERTY_BEACON, beaconToEntity(state.getBeacon()));
		entity.setProperty(PROPERTY_PROXIMITY, state.getProximity());
		entity.setProperty(PROPERTY_DISTANCE, state.getDistance());
		entity.setProperty(PROPERTY_SIGNAL, state.getSignal());
		return entity;
	}

	private static Entity beaconToEntity(GxBeaconInfo beacon) {
		Entity entity = EntityFactory.newSdt(SDT_BEACON_INFO);
		entity.setProperty(PROPERTY_UUID, beacon.getUuid());
		entity.setProperty(PROPERTY_GROUP_ID, beacon.getGroupId());
		entity.setProperty(PROPERTY_ID, beacon.getId());
		return entity;
	}

	static Beacon gxBeaconInfoToBeacon(GxBeaconInfo beaconInfo) {
		return new Beacon.Builder()
				.setId1(beaconInfo.getUuid())
				.setId2(String.valueOf(beaconInfo.getGroupId()))
				.setId3(String.valueOf(beaconInfo.getId()))
				.setManufacturer(0x004C)
				.setTxPower(-56)
				.setDataFields(Collections.singletonList(0L))
				.build();
	}
}
