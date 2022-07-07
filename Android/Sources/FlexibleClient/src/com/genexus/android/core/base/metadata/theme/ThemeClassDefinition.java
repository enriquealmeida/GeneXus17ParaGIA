package com.genexus.android.core.base.metadata.theme;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

import com.genexus.android.core.base.metadata.DimensionValue;
import com.genexus.android.core.base.metadata.Properties;
import com.genexus.android.core.base.metadata.enums.Alignment;
import com.genexus.android.core.base.metadata.enums.ImageScaleType;
import com.genexus.android.core.base.metadata.enums.MeasureUnit;
import com.genexus.android.core.base.metadata.loader.MetadataLoader;
import com.genexus.android.core.base.model.PropertiesObject;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.NameMap;
import com.genexus.android.core.base.utils.Strings;

import org.jetbrains.annotations.Nullable;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;
import static android.text.Layout.JUSTIFICATION_MODE_NONE;
import static com.genexus.android.core.base.metadata.theme.ThemeDefinitionUtilKt.getImportedProperty;
import static com.genexus.android.core.base.metadata.theme.ThemeDefinitionUtilKt.getImportedPropertyWithSource;

public class ThemeClassDefinition extends PropertiesObject
{
	private static final long serialVersionUID = 1L;

	private String mName;
	private int mPosition;

	protected ThemeDefinition mTheme;
	private final ThemeClassDefinition mParentClass;
	private final ArrayList<ThemeClassDefinition> mChildItems = new ArrayList<>();

	// Cached properties (most accessed, or slowest).
	private LayoutBoxMeasures mMargins;
	private LayoutBoxMeasures mMarginsRTL;
	private LayoutBoxMeasures mPadding;
	private LayoutBoxMeasures mPaddingRTL;
	private String mBackgroundColor;
	private String mHighlightedBackgroundColor;
	private String mBackgroundImage;
	private String mHighlightedBackgroundImage;
	private String mBorderStyle;
	private String mBorderColor;
	private Integer mBorderWidth;
	private Integer mCornerRadius;
	private Integer[] mCornersRadius;
	private String mForegroundColor;
	private String mHighlightedForegroundColor;
	private ThemeClassDefinition mLabelClass;
	private boolean mLabelClassResolved;
	private ThemeClassDefinition mReadonlyClass;
	private boolean mReadonlyClassResolved;
	private Integer mElevation;
	private boolean mElevationResolved;
	private ThemeFontDefinition mFontDefinition;
	private Boolean mFontAllCaps;
	private Boolean mShowEditTextLine;
	private Integer mLabelWidth;
	private boolean mLabelWidthResolved;
	private ImageScaleType mScaleType;
	private Integer mImageWidth;
	private Integer mImageHeight;
	private Boolean mIsAnimated;
	private Integer mAnimationDuration;
	private String mImageInviteMessageColor;
	// add to clearCache if another field is added here

	public ThemeClassDefinition(ThemeDefinition theme, ThemeClassDefinition parentClass)
	{
		mTheme = theme;
		mParentClass = parentClass;
	}

	@Override
	public String toString()
	{
		return mName;
	}

	public void setName(String name) {
		mName = name;
	}

	public String getName() {
		return mName;
	}

	public void setTheme(ThemeDefinition theme) {
		mTheme = theme;
	}

	public ThemeDefinition getTheme() {
		return mTheme;
	}

	public List<ThemeClassDefinition> getChildItems()
	{
		return mChildItems;
	}

	public String getRootName()
	{
		if (mParentClass != null)
			return mParentClass.getRootName();
		else
			return mName;
	}

	/**
	 * @param position in the metadata of the theme/design system, important to resolve precedence in design system
	 */
	public void setPosition(int position) {
		mPosition = position;
	}

	public int getPosition() {
		return mPosition;
	}

	@Override
	public Object getProperty(String name)
	{
		// Overriding this method should be enough for inheritance, since other versions
		// (optStringProperty, getBooleanProperty) end up calling this one.
		Object result = getPropertyRaw(name);

		if (result != null && result.toString().startsWith("$"))
			result = mTheme.getToken(result.toString().substring(1));

		return result;
	}

	/**
	 * Gets the property without replacing the tokens
	 */
	@Nullable
	Object getPropertyRaw(String name) {
		Object result = super.getProperty(name);
		if (result == null && mParentClass != null)
			result = mParentClass.getProperty(name);

		if (result == null && mName != null)
			result = getImportedProperty(mTheme, mName, name);

		return result;
	}

	/**
	 * Gets the property without replacing the tokens with its source
	 */
	@Nullable
	ThemeValueWithSource getPropertyRawWithSource(@NonNull String name) {
		Object value = super.getProperty(name);
		if (value == null && mParentClass != null)
			value = mParentClass.getProperty(name);

		ThemeValueWithSource result;
		if (value == null)
			result = mName == null ? null : getImportedPropertyWithSource(mTheme, mName, name);
		else
			result = new ThemeValueWithSource(value, mTheme, getPosition());

		return result;
	}

	/**
	 * Same as {@link #getProperty(String)} but also returns where the value was taken
	 */
	@Nullable
	public ThemeValueWithSource getPropertyWithSource(@NonNull String name) {
		ThemeValueWithSource result = getPropertyRawWithSource(name);

		if (result != null && result.getValue().toString().startsWith("$"))
			result.setValue(mTheme.getToken(result.getValue().toString().substring(1)));

		return result;
	}

	public ThemeClassDefinition getParentClass() {
		return mParentClass;
	}

	/**
	 * Gets the transformation associated to this theme class. May be null.
	 */
	public TransformationDefinition getTransformation()
	{
		return mTheme.getTransformation(optStringProperty("ThemeTransformationReference"));
	}

	public boolean isAnimated()
	{
		if (mIsAnimated == null)
			mIsAnimated = Services.Strings.parseBoolean(optStringProperty("ThemeAnimated"));

		return mIsAnimated;
	}

	public Integer getAnimationDuration()
	{
		if (!isAnimated())
			return 0;

		if (mAnimationDuration == null)
		{
			Integer value = Services.Strings.parseMeasureValue(optStringProperty("AnimationDuration"), "ms");
			if (value == null)
				value = Services.Application.getAppContext().getResources().getInteger(android.R.integer.config_mediumAnimTime);

			mAnimationDuration = value;
		}

		return mAnimationDuration;
	}

	//gets for knows properties.
	public String getColor()
	{
		if (mForegroundColor == null)
			mForegroundColor = optStringProperty("color");

		return mForegroundColor;
	}

	public String getHighlightedColor()
	{
		if (mHighlightedForegroundColor == null)
			mHighlightedForegroundColor = optStringProperty("highlighted_color");

		return mHighlightedForegroundColor;
	}

	public ThemeFontDefinition getFont()
	{
		if (mFontDefinition == null)
			mFontDefinition = new ThemeFontDefinition(this);

		return mFontDefinition;
	}

	public boolean getFontAllCaps()
	{
		if (mFontAllCaps == null)
			mFontAllCaps = Services.Strings.tryParseBoolean(optStringProperty("font_all_caps"), true);

		return mFontAllCaps;
	}

	public boolean getShowEditTextLine()
	{
		if (mShowEditTextLine == null)
			mShowEditTextLine = Services.Strings.tryParseBoolean(optStringProperty("show_edit_text_line"), true);

		return mShowEditTextLine;
	}

	public boolean hasBorder()
	{
		String borderStyle = getBorderStyle();
		return (Strings.hasValue(getBorderColor()) && Strings.hasValue(borderStyle) && !borderStyle.equalsIgnoreCase("none"));
	}

	public String getBorderStyle()
	{
		if (mBorderStyle == null)
			mBorderStyle = optStringProperty("border_style");

		return mBorderStyle;
	}

	public int getBorderWidth()
	{
		if (mBorderWidth == null)
		{
			String str = optStringProperty("border_width");
			Integer dipValue = Services.Strings.parseMeasureValue(str, MeasureUnit.DIP);
			if (dipValue != null)
				mBorderWidth = Services.Device.dipsToPixels(dipValue);

			if (mBorderWidth == null)
				mBorderWidth = 0;
		}

		return mBorderWidth;
	}

	public String getBorderColor()
	{
		if (mBorderColor == null)
			mBorderColor = optStringProperty("border_color");

		return mBorderColor;
	}

	public boolean hasBackgroundColor(boolean highlighted)
	{
		return (highlighted ? Strings.hasValue(getHighlightedBackgroundColor()) : Strings.hasValue(getBackgroundColor()));
	}

	public String getBackgroundColor()
	{
		if (mBackgroundColor == null)
			mBackgroundColor = optStringProperty("background_color");

		return mBackgroundColor;
	}

	public String getHighlightedBackgroundColor()
	{
		if (mHighlightedBackgroundColor == null)
			mHighlightedBackgroundColor = optStringProperty("highlighted_background_color");

		return mHighlightedBackgroundColor;
	}

	public String getBackgroundImage()
	{
		if (mBackgroundImage == null)
			mBackgroundImage = getImage("background_image");

		return mBackgroundImage;
	}

	private BackgroundImageMode mBackgroundImageMode = null;
	public BackgroundImageMode getBackgroundImageMode()
	{
		if (mBackgroundImageMode != null)
			return mBackgroundImageMode;
		String strMode = optStringProperty("background_image_mode");
		mBackgroundImageMode = BackgroundImageMode.parse(strMode);
		return mBackgroundImageMode;
	}

	public String getHighlightedBackgroundImage()
	{
		if (mHighlightedBackgroundImage == null)
			mHighlightedBackgroundImage = getImage("highlighted_image");

		return mHighlightedBackgroundImage;
	}

	public boolean hasHighlightedBackground()
	{
		return Strings.hasValue(getHighlightedBackgroundColor()) || Strings.hasValue(getHighlightedBackgroundImage());
	}

	private int getCornerRadius()
	{
		if (mCornerRadius == null)
		{
			String cornerRadius = optStringProperty("border_radius");
			Integer radiusInDips = Services.Strings.parseMeasureValue(cornerRadius, MeasureUnit.DIP);
            if (radiusInDips != null)
				mCornerRadius = Services.Device.dipsToPixels(radiusInDips);
			else
				mCornerRadius = 0;
		}

		return mCornerRadius;
	}

	// get corners radius...
	public Integer[] getCornersRadius()
	{
		if (mCornersRadius == null)
		{
			// get each corner radius.
			mCornersRadius = new Integer[] { getOneCornerRadius("border_top_left_radius"),
					getOneCornerRadius("border_top_right_radius"),
					getOneCornerRadius("border_bottom_right_radius"),
					getOneCornerRadius("border_bottom_left_radius")};
		}

		return mCornersRadius;
	}

	public int getMaxCornersRadius()
	{
		int maxRadius = 0;
		for (int r : getCornersRadius()) {
			maxRadius = Math.max(r, maxRadius);
		}
		return maxRadius;
	}

	private int getOneCornerRadius(String cornerName)
	{
		String cornerRadius = optStringProperty(cornerName);
		Integer radiusInDips = Services.Strings.parseMeasureValue(cornerRadius, MeasureUnit.DIP);
		if (radiusInDips != null)
			return Services.Device.dipsToPixels(radiusInDips);
		else
		{
			//default to old corner radius
			return getCornerRadius();
		}
	}

	public @NonNull LayoutBoxMeasures getMargins()
	{
		if (mMargins == null || mMarginsRTL==null) {
			mMargins = LayoutBoxMeasures.from(this, "margin");
			// mirror Margins in RTL
			mMarginsRTL = new LayoutBoxMeasures(mMargins.right, mMargins.top, mMargins.left, mMargins.bottom);
		}
		if (Services.Application.isRTLLanguage())
			return mMarginsRTL;
		return mMargins;
	}

	/**
	 * Gets the padding for the theme class.
	 * This value INCLUDES border width, if set, so it may not match the json value.
	 */
	public @NonNull LayoutBoxMeasures getPadding()
	{
		if (mPadding == null || mPaddingRTL==null) {
			mPadding = LayoutBoxMeasures.from(this, "padding", getBorderWidth());
			// mirror Padding in RTL
			mPaddingRTL = new LayoutBoxMeasures(mPadding.right, mPadding.top, mPadding.left, mPadding.bottom);
		}
		if (Services.Application.isRTLLanguage())
			return mPaddingRTL;
		return mPadding;
	}

	public ImageScaleType getImageScaleType()
	{
		if (mScaleType == null)
			mScaleType = ImageScaleType.parse(optStringProperty("content_mode"));

		return mScaleType;
	}

	public ImageScaleType getImageScaleType(@NonNull ImageScaleType defaultValue)
	{
		// Do not use or set mScaleType because a different default might be passed each time.
		return ImageScaleType.parse(optStringProperty("content_mode"), defaultValue);
	}

	public Integer getImageWidth()
	{
		if (mImageWidth == null)
		{
			DimensionValue value = DimensionValue.parse(optStringProperty("width"));
			if (value != null && value.Type == DimensionValue.ValueType.PIXELS)
				mImageWidth = (int)value.Value;
			else
				mImageWidth = -1; // To mark as calculated, but return null.
		}

		return (mImageWidth != -1 ? mImageWidth : null);
	}

	public Integer getImageHeight()
	{
		if (mImageHeight == null)
		{
			DimensionValue value = DimensionValue.parse(optStringProperty("height"));
			if (value != null && value.Type == DimensionValue.ValueType.PIXELS)
				mImageHeight = (int)value.Value;
			else
				mImageHeight = -1; // To mark as calculated, but return null.
		}

		return (mImageHeight != -1 ? mImageHeight : null);
	}

	// reference for map pin image class .
	public Integer getPinImageWidth()
	{
		DimensionValue value = DimensionValue.parse(optStringProperty("PinWidth"));
		if (value != null && value.Type == DimensionValue.ValueType.PIXELS)
			return (int)value.Value;
		else
			return null;

	}

	public Integer getPinImageHeight()
	{
		DimensionValue value = DimensionValue.parse(optStringProperty("PinHeight"));
		if (value != null && value.Type == DimensionValue.ValueType.PIXELS)
			return (int)value.Value;
		else
			return null;
	}

	public ImageScaleType getPinImageScaleType()
	{
		return ImageScaleType.parse(optStringProperty("PinScaleType"));
	}
	//

	public ThemeClassDefinition getStartDragClass() {
		return MultiThemeClassDefinition.classReference(this, "start_dragging_class");
	}

	public ThemeClassDefinition getAcceptDragClass() {
		return MultiThemeClassDefinition.classReference(this, "accept_drag_class");
	}

	public ThemeClassDefinition getNoAcceptDragClass() {
		return MultiThemeClassDefinition.classReference(this, "no_accept_drag_class");
	}

	public ThemeClassDefinition getDragOverClass() {
		return MultiThemeClassDefinition.classReference(this, "drag_over_class");
	}

	public ThemeClassDefinition getLabelClass()
	{
		if (!mLabelClassResolved || Services.Application.isLiveEditingEnabled())
		{
			String labelClassName = optStringProperty("ThemeLabelClassReference");
			if (Strings.hasValue(labelClassName))
				mLabelClass = mTheme.getClass(labelClassName);
			mLabelClassResolved = true;
		}

		return mLabelClass;
	}

	public ThemeClassDefinition getReadonlyClass() {
		if (!mReadonlyClassResolved || Services.Application.isLiveEditingEnabled())
		{
			mReadonlyClass = MultiThemeClassDefinition.classReference(this, "ReadonlyAttributeClassReference");
			mReadonlyClassResolved = true;
		}

		return mReadonlyClass;
	}

	public ThemeClassDefinition getThemeGridOddRowClass()
	{
		String className = optStringProperty("ThemeGridOddRowClassReference");
		if (Strings.hasValue(className))
			return mTheme.getClass(className);
		else
			return null;
	}

	public ThemeClassDefinition getThemeGridEvenRowClass()
	{
		String className = optStringProperty("ThemeGridEvenRowClassReference");
		if (Strings.hasValue(className))
			return mTheme.getClass(className);
		else
			return null;

	}

	private String getThemeGridGroupSeparatorClassReference()
	{
		return optStringProperty("ThemeGroupSeparatorClassReference");
	}

	public ThemeClassDefinition getThemeGridGroupSeparatorClass()
	{
		if (getThemeGridGroupSeparatorClassReference().length()>0)
		{
			return mTheme.getClass(getThemeGridGroupSeparatorClassReference());
		}
		return null;
	}

	public ThemeClassDefinition getThemeGroupSeparatorClass()
	{
		return getThemeGridGroupSeparatorClass(); // It has the same property name
	}

	public int getVerticalLabelAlignment()
	{
		String labelAlignment =  optStringProperty("label_vertical_alignment");
		if (labelAlignment.equalsIgnoreCase(Properties.VerticalAlignType.BOTTOM))
			return Alignment.BOTTOM;
		else if (labelAlignment.equalsIgnoreCase(Properties.VerticalAlignType.MIDDLE))
			return Alignment.CENTER_VERTICAL;
		else if (labelAlignment.equalsIgnoreCase(Properties.VerticalAlignType.TOP))
			return Alignment.TOP;

		return Alignment.NONE;
	}

	public int getHorizontalLabelAlignment() {
		String labelAlignment = optStringProperty("label_horizontal_alignment");

		if (labelAlignment.equalsIgnoreCase(Properties.HorizontalAlignType.LEFT))
			return Alignment.START;
		else if (labelAlignment.equalsIgnoreCase(Properties.HorizontalAlignType.CENTER))
			return Alignment.CENTER_HORIZONTAL;
		else if (labelAlignment.equalsIgnoreCase(Properties.HorizontalAlignType.RIGHT))
			return Alignment.END;
		else
			return Alignment.NONE;
	}

	public int getLabelAlignment() {
		return getHorizontalLabelAlignment() | getVerticalLabelAlignment();
	}

	public int getLabelAlignmentInRTL() {
		//  See https://stackoverflow.com/a/34298259 and its question
		if (getHorizontalLabelAlignment()== Alignment.NONE ||
			getHorizontalLabelAlignment()== Alignment.START)
		{
			return Alignment.RIGHT | getVerticalLabelAlignment();
		}
		return getLabelAlignment();
	}

	public int getJustificationMode() {
		String labelAlignment = optStringProperty("label_horizontal_alignment");
		if (labelAlignment.equalsIgnoreCase(Properties.HorizontalAlignType.JUSTIFY))
			return JUSTIFICATION_MODE_INTER_WORD;
		else
			return JUSTIFICATION_MODE_NONE;
	}

	public Integer getLabelWidth()
	{
		if (!mLabelWidthResolved)
		{
			Integer dipValue = Services.Strings.parseMeasureValue(optStringProperty("label_width"), MeasureUnit.DIP);
			if (dipValue != null)
				mLabelWidth = Services.Device.dipsToPixels(dipValue);
			mLabelWidthResolved = true;
		}

		return mLabelWidth;
	}

	public String getThemeImageClass() {
		return optStringProperty("ThemeImageClassReference");
	}

	public String getThemeGrid() {
		return optStringProperty("ThemeGridClassReference");
	}

	public String getThemeTab() {
		return optStringProperty("ThemeTabClassReference");
	}

	public boolean hasMarginSet()
	{
		LayoutBoxMeasures margin = getMargins();
		return !margin.isEmpty();
	}

	public boolean hasPaddingSet()
	{
		LayoutBoxMeasures padding = getPadding();
		return !padding.isEmpty();
	}

	public String getImage(String key)
	{
		return MetadataLoader.getObjectName(optStringProperty(key));
	}

	public ThemeClassDefinition getRelatedClass(String property)
	{
		String relatedClassName = optStringProperty(property);
		if (Strings.hasValue(relatedClassName))
			return mTheme.getClass(relatedClassName);
		else
			return null;
	}

	/**
	 * Gets the elevation (in pixels) set in this class.
	 * If null, use the control's default elevation instead.
	 */
	public Integer getElevation()
	{
		if (!mElevationResolved)
		{
			Integer elevation = Services.Strings.tryParseInt(optStringProperty("elevation"));
			if (elevation != null)
				elevation = Services.Device.dipsToPixels(elevation);

			mElevation = elevation;
			mElevationResolved = true;
		}

		return mElevation;
	}

	public String getImageInviteMessageColor()
	{
		if (mImageInviteMessageColor == null)
			mImageInviteMessageColor = optStringProperty("invitemessage_color");

		return mImageInviteMessageColor;
	}

	public ThemeClassDefinition getThemeAnimationClass()
	{
		String className = optStringProperty("idLoadingAnimationClass");
		if (Strings.hasValue(className))
			return mTheme.getClass(className);
		else
			return null;
	}

	// properties of animation class
	public String getAnimationType()
	{
		return optStringProperty("idAnimationType");
	}

	public DimensionValue getAnimationWidth()
	{
		DimensionValue value = DimensionValue.parse(optStringProperty("sd_width_withPercentage"));
		return value;
	}

	public DimensionValue getAnimationHeight()
	{
		DimensionValue value = DimensionValue.parse(optStringProperty("sd_height_withPercentage"));
		return value;
	}

	// reference for progress class to animation
	public ThemeClassDefinition getProgressThemeAnimationClass()
	{
		String className = optStringProperty("AnimationClassReference");
		if (Strings.hasValue(className))
			return mTheme.getClass(className);
		else
			return null;
	}

	public ThemeClassDefinition getProgressThemeTitleClass()
	{
		String className = optStringProperty("TitleClassReference");
		if (Strings.hasValue(className))
			return mTheme.getClass(className);
		else
			return null;
	}

	public ThemeClassDefinition getProgressThemeDescriptionClass()
	{
		String className = optStringProperty("DescriptionClassReference");
		if (Strings.hasValue(className))
			return mTheme.getClass(className);
		else
			return null;
	}

	public String getProgressThemeBackgroundColor()
	{
		return optStringProperty("BackgroundColor");
	}

	//
	// reference for map route class .
	public String getStrokeColor()
	{
		return optStringProperty("StrokeColor");
	}

	public String getFillColor()
	{
		return optStringProperty("FillColor");
	}

	public int[] getDashPattern() {
		String pattern = optStringProperty("LineDashPattern");
		if (!Strings.hasValue(pattern))
			return null;

		String[] values = Services.Strings.split(pattern, Strings.SPACE);
		if (values.length == 0)
			return null;

		try {
			int[] intValues = new int[values.length];
			for (int i = 0; i < values.length; i++) {
				int currentVal = Integer.parseInt(values[i]);
				intValues[i] = currentVal == 0 || currentVal == 1 ? currentVal : Services.Device.dipsToPixels(currentVal);
			}
			return intValues;
		} catch (NumberFormatException ex) {
			Services.Log.error(ex);
			return null;
		}
	}

	public int getLineWidth()
	{
		Integer lineWidth = null;
		String str = optStringProperty("LineWidth");
		Integer dipValue = Services.Strings.parseMeasureValue(str, MeasureUnit.DIP);
		if (dipValue != null)
			lineWidth = Services.Device.dipsToPixels(dipValue);

		if (lineWidth == null)
			lineWidth = 0;

		return lineWidth;
	}

	public void clearCache() {
		mMargins = null;
		mPadding = null;
		mBackgroundColor = null;
		mHighlightedBackgroundColor = null;
		mBackgroundImage = null;
		mHighlightedBackgroundImage = null;
		mBorderStyle = null;
		mBorderColor = null;
		mBorderWidth = null;
		mCornerRadius = null;
		mCornersRadius = null;
		mForegroundColor = null;
		mHighlightedForegroundColor = null;
		mLabelClass = null;
		mLabelClassResolved = false;
		mReadonlyClass = null;
		mReadonlyClassResolved = false;
		mElevation = null;
		mElevationResolved = false;
		mFontDefinition = null;
		mFontAllCaps = null;
		mShowEditTextLine = null;
		mLabelWidth = null;
		mLabelWidthResolved = false;
		mScaleType = null;
		mImageWidth = null;
		mImageHeight = null;
		mIsAnimated = null;
		mAnimationDuration = null;
		mImageInviteMessageColor = null;
	}

	public ThemeClassDefinition cloneAndMergeClass(ThemeClassDefinition themeClass) {
		if (themeClass == null)
			return this;
		try {
			ThemeClassDefinition definition = (ThemeClassDefinition) themeClass.clone();

			// Change the name for debugging purpose
			definition.setName(getName() + "+" + definition.getName());

			// Table (definition) adds to rowClass (this)
			definition.mMargins = definition.getMargins().add(getMargins());
			definition.mPadding = definition.getPadding().add(getPadding());
			definition.isAnimated();
			if (definition.mIsAnimated == null)
				definition.mIsAnimated = isAnimated();
			else
				definition.mIsAnimated |= isAnimated();

			definition.getAnimationDuration(); // loads mAnimationDuration
			Integer animationDuration = getAnimationDuration();
			if (definition.mAnimationDuration == null)
				definition.mAnimationDuration = animationDuration;
			else if (animationDuration != null)
				definition.mAnimationDuration += animationDuration;

			definition.getElevation(); // loads mElevation
			Integer elevation = getElevation();
			if (definition.mElevation == null)
				definition.mElevation = elevation;
			else if (elevation != null)
				definition.mElevation += elevation;

			// Table (definition) wins over rowClass (this)
			String str;
			if (definition.getBackgroundColor().isEmpty())
				definition.mBackgroundColor = getBackgroundColor();
			if (definition.getBackgroundImage().isEmpty()) {
				definition.mBackgroundImage = getBackgroundImage();
				definition.mBackgroundImageMode = getBackgroundImageMode();
			}
			if (definition.getBorderColor().isEmpty())
				definition.mBorderColor = getBorderColor();
			if (definition.getBorderStyle().isEmpty())
				definition.mBorderStyle = getBorderStyle();
			str = definition.optStringProperty("border_width");
			if (str.isEmpty())
				definition.mBorderWidth = getBorderWidth();
			// radius for each corner
			if (definition.getMaxCornersRadius()==0)
				definition.mCornersRadius = getCornersRadius();
			if (definition.getColor().isEmpty())
				definition.mForegroundColor = getColor();
			if (definition.getHighlightedColor().isEmpty())
				definition.mHighlightedForegroundColor = getHighlightedColor();
			if (definition.getHighlightedBackgroundColor().isEmpty())
				definition.mHighlightedBackgroundColor = getHighlightedBackgroundColor();
			if (definition.getHighlightedBackgroundImage().isEmpty())
				definition.mHighlightedBackgroundImage = getHighlightedBackgroundImage();
			str = definition.optStringProperty("width");
			if (str.isEmpty())
				definition.mImageWidth = getImageWidth();
			str = definition.optStringProperty("height");
			if (str.isEmpty())
				definition.mImageHeight = getImageHeight();
			if (definition.getImageInviteMessageColor().isEmpty())
				definition.mImageInviteMessageColor = getImageInviteMessageColor();

			// Copy properties, Table (definition) wins over rowClass (this)
			NameMap<Object> nameMap = (NameMap<Object>)getInternalProperties().clone();
			nameMap.putAll(definition.getInternalProperties());
			definition.setInternalProperties(nameMap);

			return definition;
		}
		catch (CloneNotSupportedException e) {
			return this;
		}
	}
}
