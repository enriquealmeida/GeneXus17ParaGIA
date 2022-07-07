package com.genexus.android.core.usercontrols.gauge;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Color;

import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.utils.ThemeUtils;

@SuppressWarnings("checkstyle:MemberName")
public class GaugeSpecification {

	private static final Integer DEFAULT_COLOR = Color.RED;

	public int Height;
	public int Thickness;
	public String Type;
	public String Title;
	public float MaxValue;
	public float MinValue;
	public float CurrentValue;
	public boolean ShowMinMax;
	public boolean ShowValue;

	public ArrayList<RangeSpec> Ranges = new ArrayList<>();

	public void deserialize(String value) {
		/*
		{"Title":0,"Height":10,"MaxValue":80.0,"MinValue":60.0,"Value":75.0,"ShowMinMax":false,"Ranges":[{"Color":"#0000FF","Name":"Baja","Length":7.0},{"Color":"#008000","Name":"Media","Length":7.0},{"Color":"#660000","Name":"Alta","Length":6.0}]}
		*/
		try {
			JSONObject obj = new JSONObject(value);
			Height = (int) obj.optDouble("Height");
			Thickness = (int) obj.optDouble("Thickness");
			Type = obj.optString("Type");
			Title = obj.optString("Title");
			MaxValue = (float) obj.optDouble("MaxValue", 100);
			MinValue = (float) obj.optDouble("MinValue", 0);
			CurrentValue = (float) obj.optDouble("Value", 50);
			ShowMinMax = obj.optBoolean("ShowMinMax");
			ShowValue = obj.optBoolean("ShowValue");

			JSONArray ranges = obj.optJSONArray("Ranges");
			if (ranges == null)
				return;

			for (int i = 0; i < ranges.length() ; i++) {
				JSONObject range = ranges.getJSONObject(i);
				RangeSpec rangeSpec = new RangeSpec();
				rangeSpec.Name = range.optString("Name");
				rangeSpec.Length = (float) range.optDouble("Length");
				String colorStr = range.optString("Color", "red");
				if (!colorStr.startsWith("#"))
					colorStr = "#" + colorStr;

				Integer colorId = ThemeUtils.getColorId(colorStr);
				rangeSpec.Color = colorId != null ? colorId : DEFAULT_COLOR;
				Ranges.add(rangeSpec);
			}
		} catch (JSONException e) {
			MaxValue = 100;
			MinValue = 0;
			CurrentValue = 0;
			Height = 10;
			RangeSpec rangeSpec = new RangeSpec();
			rangeSpec.Color = DEFAULT_COLOR;
			rangeSpec.Length = 100;
			rangeSpec.Name = value.length() != 0 ? "Wrong Json Format" : Strings.EMPTY;
			Ranges.add(rangeSpec);
		}
	}
}
