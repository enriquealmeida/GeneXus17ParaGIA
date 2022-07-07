package com.genexus.android.controls.maps.common.kml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.graphics.Color;

import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.controls.maps.common.IMapLocation;
import com.genexus.android.core.controls.maps.common.IMapsProvider;
import com.genexus.android.core.controls.maps.common.MapLayer;
import com.genexus.android.core.controls.maps.common.IKmlDeserializer;

public class KmlDeserializerImpl implements IKmlDeserializer
{
	private IMapsProvider mMapsProvider;

	@Override @SuppressWarnings("checkstyle:IllegalCatch")
	public MapLayer deserialize(IMapsProvider mapsProvider, InputStream is)
	{
		try
		{
			mMapsProvider = mapsProvider;
			MapLayer mapLayer = new MapLayer();

			// Use the KML parser to deserialize the KML object model.
			com.ekito.simpleKML.Serializer serializer = new com.ekito.simpleKML.Serializer();
			com.ekito.simpleKML.model.Kml kml = serializer.read(is);

			if (kml != null && kml.getFeature() != null)
				processFeature(new DocumentContext(), kml.getFeature(), mapLayer);

			return mapLayer;
		}
		catch (Exception e)
		{
			Services.Log.warning("KmlDeserializerImpl", "Error deserializing KML", e);
			return null;
		}
	}

	private void processFeature(DocumentContext context, com.ekito.simpleKML.model.Feature feature, MapLayer mapLayer)
	{
		// The feature can be a single Placemark, or a container of other features (e.g. Document, Folder).
		if (feature instanceof com.ekito.simpleKML.model.Document)
		{
			// A document contains styles and other features. Initialize a new DocumentContext with the new style set.
			com.ekito.simpleKML.model.Document document = (com.ekito.simpleKML.model.Document)feature;
			DocumentContext newContext = new DocumentContext(context, document);
			processFeatureList(newContext, document.getFeatureList(), mapLayer);
		}
		else if (feature instanceof com.ekito.simpleKML.model.Folder)
		{
			// A folder contains other features (but not styles). Keep using the same context.
			com.ekito.simpleKML.model.Folder folder = (com.ekito.simpleKML.model.Folder)feature;
			processFeatureList(context, folder.getFeatureList(), mapLayer);
		}
		else if (feature instanceof com.ekito.simpleKML.model.Placemark)
		{
			// Process the geometry of the placemark.
			com.ekito.simpleKML.model.Placemark placemark = (com.ekito.simpleKML.model.Placemark)feature;
			MapLayer.MapFeature mapFeature = newFeature(context, placemark);
			if (mapFeature != null)
				mapLayer.features.add(mapFeature);
		}
	}

	private static class DocumentContext
	{
		private final DocumentContext mParentContext;
		private final HashMap<String, com.ekito.simpleKML.model.StyleSelector> mStyleSelectors;

		private DocumentContext()
		{
			mParentContext = null;
			mStyleSelectors = new HashMap<String, com.ekito.simpleKML.model.StyleSelector>();
		}

		private DocumentContext(DocumentContext parentContext, com.ekito.simpleKML.model.Document document)
		{
			mParentContext = parentContext;
			mStyleSelectors = new HashMap<String, com.ekito.simpleKML.model.StyleSelector>();
			for (com.ekito.simpleKML.model.StyleSelector styleSelector : document.getStyleSelector())
			{
				if (Strings.hasValue(styleSelector.getId()))
					mStyleSelectors.put(styleSelector.getId(), styleSelector);
			}
		}

		public com.ekito.simpleKML.model.StyleSelector getStyleSelector(String name)
		{
			com.ekito.simpleKML.model.StyleSelector styleSelector = mStyleSelectors.get(name);
			if (styleSelector == null && mParentContext != null)
				styleSelector = mParentContext.getStyleSelector(name);

			return styleSelector;
		}
	}

	private void processFeatureList(DocumentContext context, List<com.ekito.simpleKML.model.Feature> featureList, MapLayer mapLayer)
	{
		if (featureList != null)
		{
			for (com.ekito.simpleKML.model.Feature feature : featureList)
				processFeature(context, feature, mapLayer);
		}
	}

	private MapLayer.MapFeature newFeature(DocumentContext context, com.ekito.simpleKML.model.Placemark placemark)
	{
		MapLayer.MapFeature mapFeature = null;

		if (placemark.getGeometry() instanceof com.ekito.simpleKML.model.Polygon)
		{
			mapFeature = newPolygon((com.ekito.simpleKML.model.Polygon)placemark.getGeometry(), resolveStyle(context, placemark));
		}
		else if (placemark.getGeometry() instanceof com.ekito.simpleKML.model.LineString)
		{
			mapFeature = newPolyline((com.ekito.simpleKML.model.LineString)placemark.getGeometry(), resolveStyle(context, placemark));
		}

		if (mapFeature != null)
			readBaseFeature(mapFeature, placemark);

		return mapFeature;
	}

	private MapLayer.Polygon newPolygon(com.ekito.simpleKML.model.Polygon data, com.ekito.simpleKML.model.Style style)
	{
		MapLayer.Polygon polygon = new MapLayer.Polygon();

		// Outer boundary (polygon points).
		if (data.getOuterBoundaryIs() != null)
			polygon.outerBoundary.addAll(newMapLocationList(data.getOuterBoundaryIs()));

		// Inner boundaries (holes).
		if (data.getInnerBoundaryIsList() != null)
		{
			for (com.ekito.simpleKML.model.Boundary innerBoundary : data.getInnerBoundaryIsList())
				polygon.holes.add(newMapLocationList(innerBoundary));
		}

		if (style != null)
		{
			if (style.getLineStyle() != null)
			{
				polygon.strokeWidth = style.getLineStyle().getWidth();
				polygon.strokeColor = parseColor(style.getLineStyle().getColor());
			}

			if (style.getPolyStyle() != null)
			{
				polygon.fillColor = parseColor(style.getPolyStyle().getColor());

				Integer boolFill = style.getPolyStyle().getFill();
				if (boolFill != null && boolFill == 0)
					polygon.fillColor = Color.TRANSPARENT;

				Integer boolOutline = style.getPolyStyle().getOutline();
				if (boolOutline != null && boolOutline == 0)
					polygon.strokeWidth = 0f;
			}
		}

		return polygon;
	}

	private void readBaseFeature(MapLayer.MapFeature mapFeature, com.ekito.simpleKML.model.Feature data)
	{
		mapFeature.id = data.getId();
		mapFeature.name = data.getName();
		mapFeature.description = data.getDescription();
	}

	private MapLayer.Polyline newPolyline(com.ekito.simpleKML.model.LineString data, com.ekito.simpleKML.model.Style style)
	{
		MapLayer.Polyline polyline = new MapLayer.Polyline();

		// Outer boundary (polygon points).
		polyline.points.addAll(newMapLocationList(data));

		if (style != null)
		{
			if (style.getLineStyle() != null)
			{
				polyline.strokeWidth = style.getLineStyle().getWidth();
				polyline.strokeColor = parseColor(style.getLineStyle().getColor());
			}
		}

		return polyline;
	}

	private List<IMapLocation> newMapLocationList(com.ekito.simpleKML.model.Boundary data)
	{
		ArrayList<IMapLocation> list = new ArrayList<IMapLocation>();

		if (data.getLinearRing() != null &&
			data.getLinearRing().getCoordinates() != null &&
			data.getLinearRing().getCoordinates().getList() != null)
		{
			for (com.ekito.simpleKML.model.Coordinate coordinate : data.getLinearRing().getCoordinates().getList())
				list.add(newMapLocation(coordinate));
		}

		return list;
	}

	private List<IMapLocation> newMapLocationList(com.ekito.simpleKML.model.LineString data)
	{
		ArrayList<IMapLocation> list = new ArrayList<IMapLocation>();

		if (data.getCoordinates() != null &&
			data.getCoordinates().getList() != null)
		{
			for (com.ekito.simpleKML.model.Coordinate coordinate : data.getCoordinates().getList())
				list.add(newMapLocation(coordinate));
		}

		return list;
	}

	private IMapLocation newMapLocation(com.ekito.simpleKML.model.Coordinate data)
	{
		return mMapsProvider.newMapLocation(data.getLatitude(), data.getLongitude());
	}

	private com.ekito.simpleKML.model.Style resolveStyle(DocumentContext context, com.ekito.simpleKML.model.Feature feature)
	{
		if (feature.getStyleSelector() != null && feature.getStyleSelector().size() != 0)
		{
			// Inline style (or style map). We only support one.
			return resolveStyle(context, feature.getStyleSelector().get(0));
		}
		else if (Strings.hasValue(feature.getStyleUrl()))
		{
			// Style (or style map) reference. We only support references inside the same document.
			return resolveStyle(context, feature.getStyleUrl());
		}

		return null;
	}

	private com.ekito.simpleKML.model.Style resolveStyle(DocumentContext context, com.ekito.simpleKML.model.StyleSelector styleSelector)
	{
		if (styleSelector instanceof com.ekito.simpleKML.model.Style)
			return (com.ekito.simpleKML.model.Style)styleSelector;

		if (styleSelector instanceof com.ekito.simpleKML.model.StyleMap)
		{
			com.ekito.simpleKML.model.StyleMap styleMap = (com.ekito.simpleKML.model.StyleMap)styleSelector;
			com.ekito.simpleKML.model.Pair selectedMapEntry = null;

			if (styleMap.getPairList() != null)
			{
				if (styleMap.getPairList().size() > 1)
				{
					final String STYLE_KEY_NORMAL = "normal";

					// If more than one Style in this StyleMap, search for "normal".
					for (com.ekito.simpleKML.model.Pair pair : styleMap.getPairList())
					{
						if (STYLE_KEY_NORMAL.equalsIgnoreCase(pair.getKey()))
						{
							selectedMapEntry = pair;
							break;
						}
					}
				}

				if (selectedMapEntry == null && styleMap.getPairList().size() >= 1)
				{
					// Only one, or no "normal" found. Use first one, whichever it is.
					selectedMapEntry = styleMap.getPairList().get(0);
				}
			}

			if (selectedMapEntry != null)
			{
				// Inline substyle o reference?
				if (selectedMapEntry.getStyle() != null)
					return selectedMapEntry.getStyle();
				else
					return resolveStyle(context, selectedMapEntry.getStyleUrl());
			}
		}

		return null;
	}

	private com.ekito.simpleKML.model.Style resolveStyle(DocumentContext context, String styleUrl)
	{
		// Only local references are supported.
		if (Strings.hasValue(styleUrl) && styleUrl.startsWith("#"))
		{
			com.ekito.simpleKML.model.StyleSelector referenced = context.getStyleSelector(styleUrl.substring(1));
			if (referenced != null)
				return resolveStyle(context, referenced);
		}

		return null;
	}

	private static Integer parseColor(String colorString)
	{
		if (!Strings.hasValue(colorString))
			return null;

		if (colorString.startsWith("#"))
			colorString = colorString.substring(1);

		//KML color format is ABGR
		String alpha = Strings.EMPTY;
		String bb = Strings.EMPTY;
		String gg = Strings.EMPTY;
		String rr = Strings.EMPTY;
		if (colorString.length() == 6) {
			bb = colorString.substring(0, 2);
			gg = colorString.substring(2, 4);
			rr = colorString.substring(4, 6);
		} else if (colorString.length() == 8) {
			alpha = colorString.substring(0, 2);
			bb = colorString.substring(2, 4);
			gg = colorString.substring(4, 6);
			rr = colorString.substring(6, 8);
		}

		//Expected color format is ARGB
		colorString = "#" + alpha + rr + gg + bb;

		try
		{
			return Color.parseColor(colorString);
		}
		catch (IllegalArgumentException e)
		{
			Services.Log.warning("KmlDeserializerImpl", "Invalid color: " + colorString, e);
			return null;
		}
	}
}

