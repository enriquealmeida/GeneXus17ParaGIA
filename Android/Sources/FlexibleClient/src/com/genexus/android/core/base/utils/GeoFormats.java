package com.genexus.android.core.base.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.location.Location;
import android.util.Pair;

import com.genexus.android.core.base.metadata.DataItem;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.controls.maps.common.MapLayer;

public class GeoFormats {
	private static final String TYPE_GEOPOINT = "GeoPoint";
	private static final String TYPE_GEOLINE = "GeoLine";
	private static final String TYPE_GEOPOLYGON = "GeoPolygon";
	private static final String TYPE_GEOGRAPHY = "Geography";
	private static final String TYPE_GEOLOCATION = "Geolocation";

	private static final String GEOLOCATION_PATTERN = "^\\s*(-?([0-8]?[0-9]\\.{1}\\d+|90\\.{1}0+)\\s*,\\s*-?([0-9]{1,2}\\.{1}\\d+|1[0-7][0-9]\\.{1}\\d+|180\\.{1}0+)\\s*)?$";

	public static @NonNull String convertToInternal(String externalGeo, DataItem definition) {
		if (!Strings.hasValue(externalGeo))
			return Strings.EMPTY;

		// Internal format is same as GEOLOCATION. Convert from GEOPOINT format if necessary.
		String type = definition.getType();
		MapLayer.FeatureType featureType = guessFeatureType(externalGeo);
		if (TYPE_GEOPOINT.equalsIgnoreCase(type) || featureType == MapLayer.FeatureType.Point) {
			Pair<Double, Double> latLon = parseGeopoint(externalGeo);
			if (latLon != null)
				return buildGeolocation(latLon.first, latLon.second);
		}

		return externalGeo;
	}

	public static @NonNull String convertToExternal(String internalGeo, DataItem definition) {
		if (!Strings.hasValue(internalGeo))
			return Strings.EMPTY;

		// Internal format is same as GEOLOCATION. Convert to GEOPOINT format if necessary.
		String type = definition.getType();
		MapLayer.FeatureType featureType = guessFeatureType(internalGeo);
		if (TYPE_GEOPOINT.equalsIgnoreCase(type) || (featureType == MapLayer.FeatureType.Point && !type.equals("character"))) {
			Pair<Double, Double> latLon = parseGeolocation(internalGeo);
			if (latLon != null)
				return buildGeopoint(latLon.first, latLon.second);
		}

		return internalGeo;
	}

	public static @Nullable MapLayer.FeatureType guessFeatureType(String geolocation) {
		String[] split = Services.Strings.split(geolocation, '(');
		if (split.length == 1 && geolocation.matches(GEOLOCATION_PATTERN))
			return MapLayer.FeatureType.Point;

		String type = split[0].trim();
		switch (type) {
			case "POINT":
				return MapLayer.FeatureType.Point;
			case "LINESTRING":
				return MapLayer.FeatureType.Polyline;
			case "POLYGON":
				return MapLayer.FeatureType.Polygon;
			default:
				return null;
		}
	}

	public static @Nullable List<Pair<Double, Double>> tryParse(String geo) {
		// We don't know the format here. Just guess.
		MapLayer.FeatureType featureType = guessFeatureType(geo);
		if (featureType == null)
			return null;

		switch (featureType) {
			case Point:
				Pair<Double, Double> coordinates = parseGeolocation(geo);
				if (coordinates == null)
					coordinates = parseGeopoint(geo);

				List<Pair<Double, Double>> point = new ArrayList<>();
				point.add(coordinates);
				return point;
			case Polyline:
				return GeoFormats.parseGeoline(geo);
			case Polygon:
				return GeoFormats.parseGeoPolygon(geo);
			default:
				return null;
		}
	}

	public static @NonNull String buildGeolocation(double latitude, double longitude) {
		// Format is "<latitude>, <longitude>"
		return coordinateToString(latitude) + ',' + coordinateToString(longitude);
	}

	public static @Nullable Pair<Double, Double> parseGeolocation(String geolocation) {
		if (!Strings.hasValue(geolocation))
			return null;

		// Format is "<latitude>, <longitude>"
		String[] latLon = Services.Strings.split(geolocation, ',');
		if (latLon.length == 2) {
			try {
				double lat = Double.valueOf(latLon[0].trim());
				double lon = Double.valueOf(latLon[1].trim());
				return new Pair<>(lat, lon);
			} catch (NumberFormatException ignored) { }
		}

		return null;
	}

	public static @NonNull String buildGeopoint(double latitude, double longitude) {
		// Format is "POINT (<longitude> <latitude>)"
		return "POINT (" + coordinateToString(longitude) + " " + coordinateToString(latitude) + ")";
	}

	public static @Nullable Pair<Double, Double> parseGeopoint(String geopoint) {
		if (!Strings.hasValue(geopoint))
			return null;

		// Format is "POINT(<longitude> <latitude>)"
		if (geopoint.startsWith("POINT")) {
			String unwrapped = geopoint.substring(5).trim();
			if (unwrapped.startsWith("(") && unwrapped.endsWith(")")) {
				unwrapped = unwrapped.substring(1, unwrapped.length() - 1).trim();
				String[] lonLat = Services.Strings.split(unwrapped, ' ');
				if (lonLat.length == 2) {
					try {
						double lat = Double.valueOf(lonLat[1].trim());
						double lon = Double.valueOf(lonLat[0].trim());
						return new Pair<>(lat, lon);
					} catch (NumberFormatException ignored) { }
				}
			}
		}

		return null;
	}

	public static @NonNull String coordinateToString(double value) {
		final int DECIMAL_PLACES = 7; // Maximum precision.
		return BigDecimal.valueOf(value).setScale(DECIMAL_PLACES, BigDecimal.ROUND_HALF_EVEN).toPlainString();
	}

	// list pair (lat, lng)
	public static @NonNull String buildGeoline(List<Pair<Double, Double>> path) {
		// Format is "LINESTRING (<longitude> <latitude>, <longitude> <latitude>, <longitude> <latitude>)"
		StringBuilder builder = new StringBuilder();
		builder.append("LINESTRING (");

		boolean isFirstPoint = true;
		// decode overview polyline (List<LatLng>) and add it to the path.
		for (Pair<Double, Double> coordinate : path) {
			if (!isFirstPoint)
				builder.append(", ");
			else
				isFirstPoint = false;

			builder.append(GeoFormats.coordinateToString(coordinate.second)).append(" ").append(GeoFormats.coordinateToString(coordinate.first));
		}
		builder.append(")");

		return builder.toString();
	}

	public static @Nullable List<Pair<Double, Double>> parseGeoline(String geoline) {
		if (!Strings.hasValue(geoline))
			return null;

		// Format is "LINESTRING(<longitude> <latitude>)"
		if (geoline.startsWith("LINESTRING")) {
			String unwrapped = geoline.substring(10).trim();
			return parseCommaSeparatedCoords(unwrapped);
		}

		Services.Log.warning(String.format("Unexpected geoline format in '%s'.", geoline));
		return null;
	}

	/*
	 * parseMultiGeoline accepts MULTILINESTRING and LINESTRING for MapRouteLayer convenience
	 */
	public static @Nullable List<List<Pair<Double, Double>>> parseMultiGeoline(String multiGeoline) {
		if (!Strings.hasValue(multiGeoline))
			return null;

		// Format is "MULTILINESTRING((<longitude> <latitude>), (<longitude> <latitude>))"
		if (multiGeoline.startsWith("MULTILINESTRING")) {
			String unwrapped = multiGeoline.substring(15).trim();
			if (unwrapped.startsWith("(") && unwrapped.endsWith(")")) {
				unwrapped = unwrapped.substring(1, unwrapped.length() - 1).trim();
				String[] lines = Services.Strings.split(unwrapped, "),");
				if (lines.length > 0) {
					List<List<Pair<Double, Double>>> result = new ArrayList<>();
					for (String line : lines) {
						String unwrappedLine = line.replace(")", "").replace("(","");
						List<Pair<Double, Double>> aLine = parseCommaSeparatedCoords(unwrappedLine);
						if (aLine != null)
							result.add(aLine);
					}
					return result;
				}
			}
		} else if (multiGeoline.startsWith("LINESTRING")) {
			List<List<Pair<Double, Double>>> result = new ArrayList<>();
			List<Pair<Double, Double>> line = parseGeoline(multiGeoline);
			if (line != null)
				result.add(line);

			return result;
		}

		Services.Log.warning(String.format("Unexpected multiGeoline format in '%s'.", multiGeoline));
		return null;
	}

	private static List<Pair<Double, Double>> parseCommaSeparatedCoords(String linePoints) {
		String unwrapped = linePoints;
		if (unwrapped.startsWith("(") && unwrapped.endsWith(")"))
			unwrapped = unwrapped.substring(1, unwrapped.length() - 1).trim();

		String[] points = Services.Strings.split(unwrapped, ',');
		if (points.length > 1) {
			List<Pair<Double, Double>> result = new ArrayList<>();
			for (String point : points) {
				String[] lonLat = Services.Strings.split(point.trim(), ' ');
				if (lonLat.length == 2) {
					try {
						double lat = Double.parseDouble(lonLat[1].trim());
						double lon = Double.parseDouble(lonLat[0].trim());
						result.add(new Pair<>(lat, lon));
					} catch (NumberFormatException exception) {
						Services.Log.error(exception);
					}
				}
			}
			return result;
		}

		return null;
	}

	@NonNull
	private static String buildGeoPolygon(List<Pair<Double, Double>> path) {
		// Format is "POLYGON ((<longitude> <latitude>, <longitude> <latitude>, <longitude> <latitude>))"
		StringBuilder builder = new StringBuilder();
		builder.append("POLYGON ((");

		boolean isFirstPoint = true;
		Pair<Double, Double> firstPoint = new Pair<>(0D, 0D);

		for (Pair<Double, Double> coordinate : path) {
			if (!isFirstPoint)
				builder.append(", ");
			else {
				firstPoint = coordinate;
				isFirstPoint = false;
			}

			builder.append(GeoFormats.coordinateToString(coordinate.second)).append(" ").append(GeoFormats.coordinateToString(coordinate.first));
		}

		builder.append(", ");
		builder.append(GeoFormats.coordinateToString(firstPoint.second)).append(" ").append(GeoFormats.coordinateToString(firstPoint.first));
		builder.append("))");

		return builder.toString();
	}

	@Nullable
	public static List<Pair<Double, Double>> parseGeoPolygon(String geopolygon) {
		if (!Strings.hasValue(geopolygon))
			return null;

		// Format is "POLYGON ((<longitude> <latitude>, <longitude> <latitude>, <longitude> <latitude>))"
		if (geopolygon.startsWith("POLYGON")) {
			//7 is the POLYGON word length
			String unwrapped = geopolygon.substring(7).trim();
			if (unwrapped.startsWith("(") && unwrapped.endsWith(")")) {
				unwrapped = unwrapped.substring(2, unwrapped.length() - 2).trim();
				String[] points = Services.Strings.split(unwrapped, ',');
				if (points.length > 2) {
					List<Pair<Double, Double>> result = new ArrayList<>();

					for (String point : points) {
						String[] lonLat = Services.Strings.split(point.trim(), ' ');
						if (lonLat.length == 2) {
							try {
								double lat = Double.valueOf(lonLat[1].trim());
								double lon = Double.valueOf(lonLat[0].trim());
								result.add(new Pair<>(lat, lon));
							} catch (NumberFormatException exception) {
								Services.Log.error(exception);
							}
						}
					}
					return result;
				}
			}
		}
		Services.Log.warning(String.format("Unexpected GeoPolygon format in '%s'.", geopolygon));
		return null;
	}

	public static String buildGeography(List<Pair<Double, Double>> points, MapLayer.FeatureType featureType) {
		switch (featureType) {
			case Point:
				return buildGeopoint(points.get(0).first, points.get(0).second);
			case Polyline:
				return buildGeoline(points);
			case Polygon:
				return buildGeoPolygon(points);
			default:
				return Strings.EMPTY;
		}
	}

	public static @NonNull Location getLocationFromString(String locationString) {
		double latitude = 0;
		double longitude = 0;
		if (Strings.hasValue(locationString)) {
			String[] valuesLocation = Services.Strings.split(locationString, ',');
			try {
				if (valuesLocation.length > 0)
					latitude = Double.parseDouble(valuesLocation[0]);
				if (valuesLocation.length > 1)
					longitude = Double.parseDouble(valuesLocation[1]);
			} catch (NumberFormatException ex) { /* return 0 as default */ }
		}

		Location location = new Location("POINT_LOCATION");
		location.setLatitude(latitude);
		location.setLongitude(longitude);
		return location;
	}

	public static @NonNull Double getGeopointLatitude(String geopoint) {
		Pair<Double, Double> latLong = parseGeopoint(geopoint);
		Double value;
		if (latLong == null)
			value = 0D;
		else
			value = latLong.first;

		return value;
	}

	public static @NonNull Double getGeopointLongitude(String geopoint) {
		Pair<Double, Double> latLong = parseGeopoint(geopoint);
		Double value;
		if (latLong == null)
			value = 0D;
		else
			value = latLong.second;

		return value;
	}

	public static @NonNull String geopointToGeolocation(String geopoint) {
		Pair<Double, Double> latLon = parseGeopoint(geopoint);
		if (latLon != null)
			return buildGeolocation(latLon.first, latLon.second);

		return Strings.EMPTY;
	}

	public static @NonNull String getLatitudeFromLocation(String locationString) {
		Location location = GeoFormats.getLocationFromString(locationString);
		return String.valueOf(location.getLatitude());
	}

	public static @NonNull String getLongitudeFromLocation(String locationString) {
		Location location = GeoFormats.getLocationFromString(locationString);
		return String.valueOf(location.getLongitude());
	}

	public static int getDistanceFromLocations(String locationString1, String locationString2) {
		Location location = GeoFormats.getLocationFromString(locationString1);
		Location location2 = GeoFormats.getLocationFromString(locationString2);
		return Math.round(location.distanceTo(location2));
	}

	public static String getLocationString(Location location) {
		return location != null ? location.getLatitude() + Strings.COMMA + location.getLongitude() : Strings.EMPTY;
	}
}
