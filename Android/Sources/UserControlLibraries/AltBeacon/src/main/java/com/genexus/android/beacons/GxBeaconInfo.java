package com.genexus.android.beacons;

import org.altbeacon.beacon.Beacon;

import com.genexus.android.core.base.model.Entity;

class GxBeaconInfo {

	private final String mUuid;
	private final int mGroupId;
	private final int mId;

	public GxBeaconInfo() {
		mUuid = "";
		mGroupId = 0;
		mId = 0;
	}

	GxBeaconInfo(String uuid, int groupId, int id) {
		mUuid = uuid;
		mGroupId = groupId;
		mId = id;
	}

	GxBeaconInfo(Beacon beacon) {
		mUuid = beacon.getId1().toString();
		mGroupId = beacon.getId2().toInt();
		mId = beacon.getId3().toInt();
	}

	GxBeaconInfo(Entity sdt) {
		mUuid = sdt.optStringProperty(BeaconsEntitiesFactory.PROPERTY_UUID);
		mGroupId = sdt.optIntProperty(BeaconsEntitiesFactory.PROPERTY_GROUP_ID);
		mId = sdt.optIntProperty(BeaconsEntitiesFactory.PROPERTY_ID);
	}

	String getUuid() {
		return mUuid;
	}

	int getGroupId() {
		return mGroupId;
	}

	int getId() {
		return mId;
	}
}
