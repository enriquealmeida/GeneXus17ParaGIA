package com.genexus.android.core.controls.maps.common;

import com.genexus.android.core.base.utils.Strings;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Definition of a Map Layer .
 * Contains a number of MapFeatures, which can currently be polygons or polylines (LineString).
 */
public class MapLayer {
	public String id;
	public boolean visible;
	public final List<MapFeature> features;

	public MapLayer() {
		this(Strings.EMPTY);
	}

	public MapLayer(String id) {
		if (id.trim().isEmpty())
			this.id = UUID.randomUUID().toString();
		else
			this.id = id;

		features = new ArrayList<>();
		visible = true;
	}

	public enum FeatureType {Point, Polyline, Polygon}

	public abstract static class MapFeature {
		public final FeatureType type;

		public String id;
		public String name;
		public String description;
		public String themeClass;
		public boolean isClosed;

		// Place to store the actual "map implementation object"
		// (e.g. com.google.android.gms.maps.model.Polygon for polygons in Google Maps API V2).
		public Object mapObject;

		public MapItemBase itemData;

		protected MapFeature(FeatureType type) {
			this(type, Strings.EMPTY);
		}

		protected MapFeature(FeatureType type, String id) {
			if (id == null || id.trim().isEmpty())
				this.id = UUID.randomUUID().toString();
			else
				this.id = id;

			this.type = type;
			isClosed = false;
			themeClass = Strings.EMPTY;
		}

		public abstract List<IMapLocation> getPoints();

		public abstract void setPoints(List<IMapLocation> pointList);
	}

	public static class Polygon extends MapFeature {
		public List<IMapLocation> outerBoundary;
		public List<List<IMapLocation>> holes;

		public Integer strokeColor;
		public Float strokeWidth;
		public Integer fillColor;
		public int[] dashPattern;

		public Polygon() {
			super(FeatureType.Polygon);
			outerBoundary = new ArrayList<>();
			holes = new ArrayList<>();
		}

		public Polygon(String id) {
			super(FeatureType.Polygon, id);
			outerBoundary = new ArrayList<>();
			holes = new ArrayList<>();
		}

		@Override
		public List<IMapLocation> getPoints() {
			return outerBoundary;
		}

		@Override
		public void setPoints(List<IMapLocation> pointList) {
			outerBoundary = pointList;
		}
	}

	public static class Polyline extends MapFeature {
		public List<IMapLocation> points;
		public Integer strokeColor;
		public Float strokeWidth;
		public int[] dashPattern;

		public Polyline() {
			super(FeatureType.Polyline);
			points = new ArrayList<>();
		}

		public Polyline(String id) {
			super(FeatureType.Polyline, id);
			points = new ArrayList<>();
		}

		@Override
		public List<IMapLocation> getPoints() {
			return points;
		}

		@Override
		public void setPoints(List<IMapLocation> pointList) {
			points = pointList;
		}
	}

	public static class Point extends MapFeature {
		public List<IMapLocation> coordinates;
		public Integer color;

		public Point() {
			super(FeatureType.Point);
			coordinates = new ArrayList<>();
		}

		public Point(String id) {
			super(FeatureType.Point, id);
			coordinates = new ArrayList<>();
		}

		@Override
		public List<IMapLocation> getPoints() {
			return coordinates;
		}

		@Override
		public void setPoints(List<IMapLocation> pointList) {
			if (pointList.size() != 1)
				throw new IllegalArgumentException("Point list must have exactly one location");

			coordinates = pointList;
		}
	}
}
