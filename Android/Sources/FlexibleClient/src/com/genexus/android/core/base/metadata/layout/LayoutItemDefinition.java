package com.genexus.android.core.base.metadata.layout;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.genexus.android.core.base.metadata.ActionDefinition;
import com.genexus.android.core.base.metadata.DataItem;
import com.genexus.android.core.base.metadata.DataTypeName;
import com.genexus.android.core.base.metadata.IDataSourceDefinition;
import com.genexus.android.core.base.metadata.Properties;
import com.genexus.android.core.base.metadata.RelationDefinition;
import com.genexus.android.core.base.metadata.enums.Alignment;
import com.genexus.android.core.base.metadata.enums.ControlTypes;
import com.genexus.android.core.base.metadata.enums.DisplayModes;
import com.genexus.android.core.base.metadata.enums.ImageUploadModes;
import com.genexus.android.core.base.metadata.enums.LayoutItemsTypes;
import com.genexus.android.core.base.metadata.enums.LayoutModes;
import com.genexus.android.core.base.metadata.rules.PromptRuleDefinition;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.metadata.types.IStructuredDataType;
import com.genexus.android.core.base.model.PropertiesObject;
import com.genexus.android.core.base.serialization.INodeObject;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;
import static android.text.Layout.JUSTIFICATION_MODE_NONE;

public class LayoutItemDefinition extends PropertiesObject implements ILayoutItem, ILayoutItemVisitable, Serializable
{
	private static final long serialVersionUID = 1L;
	private String mControlName;
	private String mType;
	private LayoutDefinition mLayout;
	private LayoutItemDefinition mItemParent;
	private ControlInfo mControlInfo;
	private String mCaption;
	private String mThemeClass;
	private final ArrayList<LayoutItemDefinition> mChildItems = new ArrayList<>();
	private boolean mIsVisible;
	private boolean mIsBox;
	private boolean mIsEnabled;
	protected boolean mIsAutogrow;
	private boolean mIsHtml;
	private String mLabelPosition;

	private int mCellGravity = Alignment.NONE;
	private int mCellHorizontalAlign = Alignment.NONE;
	private int mCellVerticalAlign = Alignment.NONE;

	private RelationDefinition mRelationToNavigate;

	@RequiresApi(Build.VERSION_CODES.O)
	private int mJustificationMode = JUSTIFICATION_MODE_NONE;

	// Effective data item points to "real" field if mDataItem is an SDT. Otherwise they are the same.
	private DataItem mDataItem;
	private DataItem mEffectiveDataItem;

	public LayoutItemDefinition(DataItem data)
	{
		mDataItem = data;
	}

	public LayoutItemDefinition(LayoutDefinition layout, LayoutItemDefinition itemParent)
	{
		mLayout = layout;
		mItemParent = itemParent;
	}

	@Override
	public String toString()
	{
		String name = getName();
		return (Services.Strings.hasValue(name) ? name : "<missing control name>");
	}

	public int getCellGravity() {
		return mCellGravity;
	}

	public int getCellHorizontalAlign() {
		return mCellHorizontalAlign;
	}

	public int getCellVerticalAlign() {
		return mCellVerticalAlign;
	}

	public int getCellGravityInRTL() {
		//  See https://stackoverflow.com/a/34298259 and its question
		if (getCellHorizontalAlign()== Alignment.NONE ||
			getCellHorizontalAlign()== Alignment.START)
		{
			return Alignment.RIGHT | getCellVerticalAlign();
		}
		return getCellGravity();
	}


	public void setCellGravityAlign(int cellGravity, int hAlign, int vAlign) {
		mCellGravity = cellGravity;
		mCellHorizontalAlign = hAlign;
		mCellVerticalAlign = vAlign;
	}

	@RequiresApi(Build.VERSION_CODES.O)
	public int getJustificationMode() {
		return mJustificationMode;
	}

	@RequiresApi(Build.VERSION_CODES.O)
	public void setJustificationMode(int justificationMode) {
		mJustificationMode = justificationMode;
	}

	/**
	 * Get the data source associated to this layout item.
	 * Can be the component's datasource or the grid's datasource if the item belongs to a grid.
	 * By default it's inherited from parent, and then from component if its the root layout item;
	 * some layout items may override it to return their own datasource (which is then inherited
	 * downwards).
	 */
	public IDataSourceDefinition getDataSource()
	{
		if (mItemParent == null)
			return mLayout.getDataSource();

		return mItemParent.getDataSource();
	}

	public void setName(String name) {
		mControlName = name;
	}

	@Override
	public String getName()
	{
		return mControlName;
	}

	public void setFK(RelationDefinition relation) {
		mRelationToNavigate = relation;
	}
	public RelationDefinition getFK()	{
		return mRelationToNavigate;
	}

	private void loadDataItem(INodeObject node)
	{
		if (mDataItem != null)
			return;

		String dataElement = node.optString("@attribute");
		if (getDataSource() != null && dataElement.length() > 0) {
			mDataItem = getDataSource().getDataItem(dataElement);
			if (mDataItem == null)
				mDataItem = getLayout().getParent().getVariable(dataElement); // Variable in a SDT Grid
		}
	}

	public void readData(INodeObject node)
	{
		mControlName = node.optString("@controlName");
		if (mType.equals("textblock") || mType.equals("group"))
			mCaption = node.optString("@caption");
		else if (mType.equals("data"))
			mCaption = node.optString("@labelCaption");
		else
			mCaption = Strings.EMPTY;

		mIsVisible = node.optBoolean("@visible", true);
		mIsBox = node.optString("@invisibleMode", "Keep Space").equalsIgnoreCase("Keep Space");
		mIsEnabled = node.optBoolean("@enabled", true);
		mIsHtml = node.optString("@format", "Text").equalsIgnoreCase("HTML");

		mThemeClass = node.optString("@class");

		// Set Alignment
		String hAlign = node.optString("@hAlign");
		String vAlign = node.optString("@vAlign");
		setHAlign(hAlign);
		setVAlign(vAlign);

		loadDataItem(node);

		INodeObject customProps = node.getNode("CustomProperties");
		if (mControlInfo == null)
		{
			String controlType = node.optString("@controlType");
			if (!Strings.hasValue(controlType))
				controlType = node.optString("@ControlType");
			if (!Strings.hasValue(controlType) && customProps != null)
				controlType = customProps.optString("@ControlType");

			if (Strings.hasValue(controlType))
			{
				mControlInfo = new ControlInfo();
				mControlInfo.setControl(controlType);

				if (customProps != null)
					mControlInfo.deserialize(customProps);
			}
		}

		// calculate better autogrow, in some UC, has controlInfo, but autogrow is present in properties.
		mIsAutogrow = false;
		if (mControlInfo != null && mControlInfo.getProperty("@AutoGrow")!=null)
		{
			mIsAutogrow = mControlInfo.optBooleanProperty("@AutoGrow");
		}
		else
		{
			mIsAutogrow = node.optBoolean("@AutoGrow");
		}
	}

	public boolean hasAutoGrow()
	{
		return mIsAutogrow;
	}

	public boolean addScrollView() {
		return false;
	}

	public boolean noRequireScrollView()
	{
		return (Strings.starsWithIgnoreCase(this.getName(), "GxNoScroll") || Strings.starsWithIgnoreCase(this.getName(), "&GxNoScroll"));
	}

	public ControlInfo getControlInfo()
	{
		return mControlInfo;
	}

	public void setUnusableForReuse() {
		if (mControlInfo != null)
			mControlInfo.setProperty("@" + mControlInfo.getControl() + "SDGxReuseView", true);
	}

	public void setControlInfo(ControlInfo controlInfo) {
		mControlInfo = controlInfo;
	}

	private void setVAlign(String vAlign)
	{
		if (vAlign.equals(Properties.VerticalAlignType.BOTTOM)) {
			mCellGravity |= Alignment.BOTTOM;
			mCellVerticalAlign = Alignment.BOTTOM;
		} else if (vAlign.equals(Properties.VerticalAlignType.MIDDLE)) {
			mCellGravity |= Alignment.CENTER_VERTICAL;
			mCellVerticalAlign = Alignment.CENTER_VERTICAL;
		} else if (vAlign.equals(Properties.VerticalAlignType.TOP)) {
			mCellGravity |= Alignment.TOP;
			mCellVerticalAlign = Alignment.TOP;
		}
	}

	private void setHAlign(String hAlign)
	{
		if (hAlign.equals(Properties.HorizontalAlignType.LEFT)) {
			mCellGravity |= Alignment.START;
			mCellHorizontalAlign = Alignment.START;
		} else if (hAlign.equals(Properties.HorizontalAlignType.CENTER)) {
			mCellGravity |= Alignment.CENTER_HORIZONTAL;
			mCellHorizontalAlign = Alignment.CENTER_HORIZONTAL;
		} else if (hAlign.equals(Properties.HorizontalAlignType.RIGHT)) {
			mCellGravity |= Alignment.END;
			mCellHorizontalAlign = Alignment.END;
		} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && hAlign.equals(Properties.HorizontalAlignType.JUSTIFY)) {
			mJustificationMode = JUSTIFICATION_MODE_INTER_WORD;
		}
	}

	public LayoutDefinition getLayout() { return mLayout; }
	public LayoutItemDefinition getParent() { return mItemParent; }

	/**
	 * Goes up the parent chain until an item is found of the specified type.
	 * @return The nearest ancestor of the specified type, or null if none is found.
	 */
	public <TypeT extends LayoutItemDefinition> TypeT findParentOfType(Class<TypeT> itemType)
	{
		LayoutItemDefinition parent = getParent();
		if (parent == null)
			return null;

		if (itemType.isInstance(parent))
			return itemType.cast(parent);

		return parent.findParentOfType(itemType);
	}

	/**
	 * Goes down the children chain collecting all items of the specified type.
	 * @return The items of the specified type directly or indirectly contained by this one.
	 */
	public <TypeT extends LayoutItemDefinition> List<TypeT> findChildrenOfType(Class<TypeT> itemType)
	{
		ArrayList<TypeT> list = new ArrayList<>();

		if (itemType.isInstance(this))
			list.add(itemType.cast(this));

		for (LayoutItemDefinition child : getChildItems())
		{
			List<TypeT> childList = child.findChildrenOfType(itemType);
			list.addAll(childList);
		}

		return list;
	}

	public GridDefinition getOwnerGrid()
	{
		return findParentOfType(GridDefinition.class);
	}

	public List<LayoutItemDefinition> getChildItems() {
		return mChildItems;
	}

	public void setType(String type) {
		mType = type;
	}

	public String getType() {
		return mType;
	}

	@Override
	public void accept(ILayoutVisitor visitor)
	{
		if (mChildItems.size() > 0)
		{
			visitor.enterVisitor(this);
			visitor.visit(this);
			for (int i = 0; i < mChildItems.size() ; i++)
				mChildItems.get(i).accept(visitor);

			// Ignore Cells as containers because always contains only one element
			if (!getType().equals(LayoutItemsTypes.CELL))
				visitor.leaveVisitor(this);
		}
		else
			visitor.visit(this);
	}

	public String getCaption()
	{
		return Services.Language.getTranslation(mCaption);
	}

	public void setCaption(String caption)
	{
		mCaption = caption;
	}

	private ThemeClassDefinition mClassDefinition;

	public ThemeClassDefinition getThemeClass()
	{
		// Don't cache the ThemeDefinition if setTheme was called or if LiveEditing is on.
		if (mClassDefinition == null || mClassDefinition.getTheme() != Services.Themes.getCurrentTheme() || Services.Application.isLiveEditingEnabled())
			mClassDefinition = Services.Themes.getThemeClass(mThemeClass);

		return mClassDefinition;
	}

	public DataTypeName getDataTypeName()
	{
		return getDataItem().getDataTypeName();
	}

	public DataItem getDataItem()
	{
		// Effective data item points to "real" field if mDataItem is an SDT. Otherwise they are the same.
		// Cache calculation, since it might be costly.
		if (mEffectiveDataItem == null && mDataItem != null)
			mEffectiveDataItem = getEffectiveDataItem();

		if (mEffectiveDataItem == null)
			Services.Log.error(String.format("Control '%s' does not have an associated data item.", getName()));

		return mEffectiveDataItem;
	}

	private DataItem getEffectiveDataItem()
	{
		String fieldSpecifier = getFieldSpecifier();
		if (Services.Strings.hasValue(fieldSpecifier))
		{
			// An SDT (or BC) field. Get info from SDT structure.
			IStructuredDataType structureType = mDataItem.getTypeInfo(IStructuredDataType.class);
			if (structureType != null)
			{
				fieldSpecifier = fieldSpecifier.replace("item(0).", Strings.EMPTY);
				DataItem subItem = structureType.getItem(fieldSpecifier);
				if (subItem != null)
					return subItem;
			}

			Services.Log.warning(String.format("LayoutDataItem '%s' has field specifier '%s', but field information could not be obtained from data item definition.", getName(), fieldSpecifier));
		}

		return mDataItem;
	}

	public String getLabelPosition()
	{
		if (mLabelPosition == null)
		{
			String position = optStringProperty("@labelPosition");

			// Missing means 'platform default'.
			if (!Services.Strings.hasValue(position))
				position = Properties.LabelPositionType.PLATFORM_DEFAULT;

			// And 'platform default' means 'top' if the control is stand-alone and 'left' inside a Grid.
			// At least on Android! Move to a Service if it needs to be different on Blackberry.
			if (position.equals(Properties.LabelPositionType.PLATFORM_DEFAULT))
			{
				if (getOwnerGrid() != null)
				{
					if (!getControlType().equals(ControlTypes.PHOTO_EDITOR))
						position = Properties.LabelPositionType.LEFT;
					else
						position =	Properties.LabelPositionType.NONE;
				}
				else
				{
					position = Properties.LabelPositionType.TOP;
				}
			}
			mLabelPosition = position;
		}

		return mLabelPosition;
	}

	public void setLabelPosition(String position) {
		mLabelPosition = position;
	}

	public String getInviteMessage()
	{
		String inviteMessage = optStringProperty("@InviteMessage");
		return Services.Language.getTranslation(inviteMessage);
	}

	public String getTimeInviteMessage()
	{
		String timeInviteMessage = optStringProperty("@timeInviteMessage");
		return Services.Language.getTranslation(timeInviteMessage);
	}


	public boolean getAutoLink()
	{
		return optBooleanProperty("@autolink");
	}

	public String getControlType()
	{
		DataItem dataItem = getDataItem();
		return (dataItem != null ? getDataItem().getControlType() : ControlTypes.TEXT_BOX);
	}

	public boolean getReadOnly(short layoutMode, short trnMode)
	{
		if (optStringProperty("@readonly").equalsIgnoreCase("Auto"))
		{
			if (layoutMode == LayoutModes.LIST)
				return true;

			if (layoutMode == LayoutModes.VIEW)
			{
				//var editables / att readonly in view
				return !optStringProperty("@attribute").startsWith("&");
			}

			if (layoutMode == LayoutModes.EDIT)
			{
				//var readonly / att editables in edit
				return optStringProperty("@attribute").startsWith("&");
			}
		}

		if (trnMode == DisplayModes.DELETE)
			return true;

		if (getDataItem().getAutoNumber())
			return true;

		if (trnMode == DisplayModes.EDIT && getDataItem().isKey())
		{
			return true;
		}

		if (optBooleanProperty("@readonly"))
			return true;

		return getDataItem().getReadOnly();
	}

	public String getDataId()
	{
		// Replaces item(0) by item(0), so same as before.
		return getDataId(0);
	}

	public String getDataId(int position)
	{
		if (mDataItem == null)
			return null;

		String idName = mDataItem.getName();
		String fieldSpecifier = getFieldSpecifier();

		if (Services.Strings.hasValue(fieldSpecifier))
		{
			fieldSpecifier = fieldSpecifier.replace("item(0)", String.format("item(%d)", position));
			idName += Strings.DOT + fieldSpecifier;
		}

		return idName;
	}

	public String getDataId(ArrayList<Integer> positions)
	{
		if (mDataItem == null)
			return null;

		String idName = mDataItem.getName();
		String fieldSpecifier = getFieldSpecifier();

		if (Services.Strings.hasValue(fieldSpecifier))
		{
			for (int n = 0; n < positions.size(); n++) {
				// Note: increase positions because GX indexes are 1-based.
				fieldSpecifier = fieldSpecifier.replaceFirst("item\\(0\\)", String.format("item(%d)", positions.get(n) + 1));
			}
			idName += Strings.DOT + fieldSpecifier;
		}

		return idName;
	}

	private String getFieldSpecifier()
	{
		return optStringProperty("@fieldSpecifier");
	}

	public boolean isVisible()
	{
		return mIsVisible;
	}

	public boolean getKeepSpace() {
		return mIsBox;
	}

	public boolean isEnabled()
	{
		return mIsEnabled;
	}

	public int getDesiredWidth()
	{
		return getDesiredWidth(getThemeClass());
	}

	public int getDesiredWidth(ThemeClassDefinition themeClass)
	{
		if (mItemParent != null)
		{
			int cellWidth = ((CellDefinition)mItemParent).getAbsoluteWidth();
			if (getThemeClass() != null)
				cellWidth -= themeClass.getMargins().getTotalHorizontal();

			cellWidth = Math.max(0, cellWidth);
			return cellWidth;
		}
		else
			return -1;
	}

	public int getDesiredHeight()
	{
		return getDesiredHeight(getThemeClass());
	}

	public int getDesiredHeight(ThemeClassDefinition themeClass)
	{
		if (mItemParent != null)
		{
			int cellHeight = ((CellDefinition)mItemParent).getAbsoluteHeight();
			if (getThemeClass() != null)
				cellHeight -= themeClass.getMargins().getTotalVertical();

			cellHeight = Math.max(0, cellHeight);
			return cellHeight;
		}
		else
			return -1;
	}

	public boolean isHtml() { return mIsHtml; }

	public boolean hasPrompt(short layoutMode, short trnMode)
	{
		if (mLayout != null)
			return mLayout.getPrompts().hasPrompt(this, layoutMode, trnMode);
		else
			return false;
	}

	public PromptRuleDefinition getPrompt(short layoutMode, short trnMode)
	{
		if (mLayout != null)
			return mLayout.getPrompts().getPromptOn(this, layoutMode, trnMode);
		else
			return null;
	}

	public int getMaximumUploadSizeMode() {
		String maxUploadSize = (mControlInfo != null) ? mControlInfo.optStringProperty("@MaximumUploadSize") : optStringProperty("@MaximumUploadSize");
		return ImageUploadModes.getModeFromString(maxUploadSize);
	}

	public ActionDefinition getEventHandler(String eventName)
	{
		if (mLayout != null && mLayout.getParent() != null)
		{
			String actionName = getName() + "." + eventName;  // String.format("%s.%s", controlDefinition.getName(), eventName) is slooooooooooow.
			return mLayout.getParent().getEvent(actionName);
		}
		else
			return null;
	}
}
