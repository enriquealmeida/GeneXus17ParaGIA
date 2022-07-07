package com.genexus.android.core.base.metadata.theme;

import java.io.Serializable;

import com.genexus.android.core.base.metadata.DimensionValue;
import com.genexus.android.core.base.serialization.INodeObject;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;

public class TransformationDefinition implements Serializable
{
	private static final long serialVersionUID = 1L;

	public enum TranslationType { TO, BY }
	public enum RelativeTo { CONTROL, PARENT, FORM }

	private final String mName;

	private final DimensionValue mAnchorPointX;
	private final DimensionValue mAnchorPointY;

	private final DimensionValue mTranslateX;
	private final DimensionValue mTranslateY;
	private final TranslationType mTranslateType;
	private final RelativeTo mTranslateRelativeTo;

	private final DimensionValue mScaleX;
	private final DimensionValue mScaleY;
	private final RelativeTo mScaleRelativeTo;

	private final DimensionValue mResizeX;
	private final DimensionValue mResizeY;
	private final RelativeTo mResizeRelativeTo;

	private final float mRotation;
	private final float mRotationX;
	private final float mRotationY;

	private TransformationDefinition()
	{
		mName = "<Empty>";
		mAnchorPointX = null;
		mAnchorPointY = null;

		mTranslateX = null;
		mTranslateY = null;
		mTranslateType = TranslationType.TO;
		mTranslateRelativeTo = RelativeTo.CONTROL;

		mScaleX = null;
		mScaleY = null;
		mScaleRelativeTo = RelativeTo.CONTROL;

		mResizeX = null;
		mResizeY = null;
		mResizeRelativeTo = RelativeTo.CONTROL;

		mRotation = 0;
		mRotationX = 0;
		mRotationY = 0;
	}

	public TransformationDefinition(INodeObject json)
	{
		mName = json.optString("Name");

		mAnchorPointX = parseValue(json, "gxtransform_anchor_point_x");
		mAnchorPointY = parseValue(json, "gxtransform_anchor_point_y");

		mTranslateX = parseValue(json, "gxtranslation_value_x");
		mTranslateY = parseValue(json, "gxtranslation_value_y");
		mTranslateType = parseTranslationType(json, "gxtransform_translation_type");
		mTranslateRelativeTo = parseRelativeTo(json, "gxtranslation_relative_to");

		mScaleX = parseValue(json, "gxscale_value_x");
		mScaleY = parseValue(json, "gxscale_value_y");
		mScaleRelativeTo = parseRelativeTo(json, "gxscale_relative_to");

		mResizeX = parseValue(json, "gxresize_value_x");
		mResizeY = parseValue(json, "gxresize_value_y");
		mResizeRelativeTo = parseRelativeTo(json, "gxresize_relative_to");

		mRotation = parseAngle(json, "gxrotate_value");
		mRotationX = parseAngle(json, "gxrotate_x_value");
		mRotationY = parseAngle(json, "gxrotate_y_value");
	}

	public static final TransformationDefinition EMPTY = new TransformationDefinition();

	private static RelativeTo parseRelativeTo(INodeObject json, String key)
	{
		final String STR_CONTROL = "current";
		final String STR_PARENT = "parent";
		final String STR_FORM = "form";

		String value = json.optString(key);
		if (STR_CONTROL.equalsIgnoreCase(value))
			return RelativeTo.CONTROL;
		else if (STR_PARENT.equalsIgnoreCase(value))
			return RelativeTo.PARENT;
		else if (STR_FORM.equalsIgnoreCase(value))
			return RelativeTo.FORM;

		return RelativeTo.CONTROL;
	}

	private static final String VALUE_TOP = "top";
	private static final String VALUE_MIDDLE = "middle";
	private static final String VALUE_BOTTOM = "bottom";
	private static final String VALUE_LEFT = "left";
	private static final String VALUE_CENTER = "center";
	private static final String VALUE_RIGHT = "right";

	private static DimensionValue parseValue(INodeObject json, String key)
	{
		String str = json.optString(key);

		if (Strings.hasValue(str))
		{
			// Special cases.
			if (VALUE_TOP.equalsIgnoreCase(str) || VALUE_LEFT.equalsIgnoreCase(str))
				return DimensionValue.percent(0);

			if (VALUE_MIDDLE.equalsIgnoreCase(str) || VALUE_CENTER.equalsIgnoreCase(str))
				return DimensionValue.percent(50);

			if (VALUE_BOTTOM.equalsIgnoreCase(str) || VALUE_RIGHT.equalsIgnoreCase(str))
				return DimensionValue.percent(100);

			// Generic, dip or percentage.
			return DimensionValue.parse(str);
		}
		else
			return null;
	}

	private static TranslationType parseTranslationType(INodeObject json, String key)
	{
		final String STR_BY = "translate_by";
		final String STR_TO = "translate_to";

		String value = json.optString(key);
		if (STR_BY.equalsIgnoreCase(value))
			return TranslationType.BY;
		else if (STR_TO.equalsIgnoreCase(value))
			return TranslationType.TO;

		return TranslationType.BY;
	}

	private static float parseAngle(INodeObject json, String key)
	{
		String jsonValue = json.optString(key);
		jsonValue = jsonValue.replace("º", "");
		return Services.Strings.tryParseFloat(jsonValue, 0);
	}

	public String getName() { return mName; }

	public DimensionValue getAnchorPointX() { return mAnchorPointX; }
	public DimensionValue getAnchorPointY() { return mAnchorPointY; }

	public DimensionValue getTranslateX() { return mTranslateX; }
	public DimensionValue getTranslateY() { return mTranslateY; }
	public TranslationType getTranslateType() { return mTranslateType; }
	public RelativeTo getTranslateRelativeTo() { return mTranslateRelativeTo; }

	public DimensionValue getScaleX() { return mScaleX; }
	public DimensionValue getScaleY() { return mScaleY; }
	public RelativeTo getScaleRelativeTo() { return mScaleRelativeTo; }

	public DimensionValue getResizeX() { return mResizeX; }
	public DimensionValue getResizeY() { return mResizeY; }
	public RelativeTo getResizeRelativeTo() { return mResizeRelativeTo; }

	public float getRotation() { return mRotation; }
	public float getRotationX() { return mRotationX; }
	public float getRotationY() { return mRotationY; }
}
