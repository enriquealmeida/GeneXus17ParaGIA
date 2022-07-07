package com.genexus.android.beacons;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.model.EntityList;
import com.genexus.android.core.utils.Cast;

class GxBeaconProximityAlert {

	private final GxBeaconRegion mRegion;
	private final boolean mNotifyOnEntry;
	private final boolean mNotifyOnExit;

	GxBeaconProximityAlert(GxBeaconRegion region, boolean notifyEntry, boolean notifyExit) {
		mRegion = region;
		mNotifyOnEntry = notifyEntry;
		mNotifyOnExit = notifyExit;
	}

	GxBeaconProximityAlert(Entity sdt) {
		mRegion = new GxBeaconRegion(Objects.requireNonNull(Cast.as(Entity.class,
				sdt.getProperty(BeaconsEntitiesFactory.PROPERTY_BEACON_REGION))));
		mNotifyOnEntry = sdt.optBooleanProperty(BeaconsEntitiesFactory.PROPERTY_NOTIFY_ON_ENTRY);
		mNotifyOnExit = sdt.optBooleanProperty(BeaconsEntitiesFactory.PROPERTY_NOTIFY_ON_EXIT);
	}

	static List<GxBeaconProximityAlert> newCollection(EntityList collection) {
		ArrayList<GxBeaconProximityAlert> list = new ArrayList<>();
		for (Entity item : collection)
			list.add(new GxBeaconProximityAlert(item));

		return list;
	}

	String getRegionId() {
		return mRegion.getId();
	}

	GxBeaconRegion getRegion() {
		return mRegion;
	}

	boolean shouldNotifyOnEntry() {
		return mNotifyOnEntry;
	}

	boolean shouldNotifyOnExit() {
		return mNotifyOnExit;
	}
}
