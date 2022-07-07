package com.genexus.android.beacons;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.altbeacon.beacon.Beacon;

class GxBeaconState {

	private final GxBeaconInfo mBeacon;
	private final double mDistance;
	private final int mSignal;

	private static final int PROXIMITY_UNKNOWN = 0;
	private static final int PROXIMITY_IMMEDIATE = 1;
	private static final int PROXIMITY_NEAR = 2;
	private static final int PROXIMITY_FAR = 3;

	private static final double MAX_DISTANCE_IMMEDIATE = 0.3;
	private static final double MAX_DISTANCE_NEAR = 3;

	private GxBeaconState(Beacon beacon) {
		mBeacon = new GxBeaconInfo(beacon);
		mDistance = beacon.getDistance();
		mSignal = beacon.getRssi();
	}

	static List<GxBeaconState> newCollection(Collection<Beacon> beacons) {
		ArrayList<GxBeaconState> list = new ArrayList<>();
		for (Beacon beacon : beacons)
			list.add(new GxBeaconState(beacon));

		return list;
	}

	GxBeaconInfo getBeacon() {
		return mBeacon;
	}

	double getDistance() {
		return mDistance;
	}

	int getProximity() {
		if (mDistance <= 0)
			return PROXIMITY_UNKNOWN;
		else if (mDistance < MAX_DISTANCE_IMMEDIATE)
			return PROXIMITY_IMMEDIATE;
		else if (mDistance < MAX_DISTANCE_NEAR)
			return PROXIMITY_NEAR;
		else
			return PROXIMITY_FAR;
	}

	int getSignal() {
		return mSignal;
	}
}
