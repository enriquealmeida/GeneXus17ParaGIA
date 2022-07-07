package com.genexus.android.beacons;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.Region;

import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.utils.Cast;

class GxBeaconRegion {

	private final String mId;
	private final GxBeaconInfo mBeaconMatch;

	static final int STATE_UNKNOWN = 0;
	static final int STATE_INSIDE = 1;
	static final int STATE_OUTSIDE = 2;

	GxBeaconRegion(String id, GxBeaconInfo beaconMatch) {
		mId = id;
		mBeaconMatch = beaconMatch;
	}

	GxBeaconRegion(Region region) {
		mId = region.getUniqueId();

		String beaconUuid = (region.getId1() != null ? region.getId1().toString() : Strings.EMPTY);
		int beaconGroupId = (region.getId2() != null ? region.getId2().toInt() : 0);
		int beaconId = (region.getId3() != null ? region.getId3().toInt() : 0);
		mBeaconMatch = new GxBeaconInfo(beaconUuid, beaconGroupId, beaconId);
	}

	GxBeaconRegion(Entity sdt) {
		mId = sdt.optStringProperty(BeaconsEntitiesFactory.PROPERTY_IDENTIFIER);
		mBeaconMatch = new GxBeaconInfo(Objects.requireNonNull(Cast.as(Entity.class,
				sdt.getProperty(BeaconsEntitiesFactory.PROPERTY_BEACON_MATCH))));
	}

	static List<GxBeaconRegion> newCollection(Collection<Region> regions) {
		ArrayList<GxBeaconRegion> list = new ArrayList<>();
		for (Region region : regions)
			list.add(new GxBeaconRegion(region));

		return list;
	}

	String getId() {
		return mId;
	}

	GxBeaconInfo getBeaconMatch() {
		return mBeaconMatch;
	}

	Region toRegion() {
		Identifier id1 = null;
		if (Strings.hasValue(mBeaconMatch.getUuid()))
			id1 = Identifier.parse(mBeaconMatch.getUuid());

		Identifier id2 = null;
		if (mBeaconMatch.getGroupId() != 0)
			id2 = Identifier.fromInt(mBeaconMatch.getGroupId());

		Identifier id3 = null;
		if (mBeaconMatch.getId() != 0)
			id3 = Identifier.fromInt(mBeaconMatch.getId());

		return new Region(mId, id1, id2, id3);
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof GxBeaconRegion))
			return false;

		GxBeaconRegion other = (GxBeaconRegion) o;
		return mId.equals(other.mId);
	}

	@Override
	public int hashCode() {
		return mId.hashCode();
	}
}
