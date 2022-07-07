package com.genexus.android.core.base.metadata.layout;

import java.util.ArrayList;
import java.util.List;

import com.genexus.android.core.base.metadata.ActionDefinition;
import com.genexus.android.core.base.metadata.DataItem;
import com.genexus.android.core.base.metadata.IDataSourceDefinition;
import com.genexus.android.core.base.metadata.enums.LayoutItemsTypes;
import com.genexus.android.core.base.metadata.loader.MetadataLoader;
import com.genexus.android.core.base.metadata.theme.LayoutBoxMeasures;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.metadata.types.IStructuredDataType;
import com.genexus.android.core.base.serialization.INodeObject;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.controls.ControlPropertiesDefinition;
import com.genexus.android.core.usercontrols.UcFactory;
import com.genexus.android.core.usercontrols.UserControlDefinition;

public class GridDefinition extends LayoutItemDefinition
{
	private static final long serialVersionUID = 1L;

	private String mDataSourceName;
	private String mAssociatedCollection;
	private String mDefaultAction;
	private String mEmptyDataSetImage;
	private String mEmptyDataSetImageClass;
	private String mEmptyDataSetText;
	private String mEmptyDataSetTextClass;
	private String mRowsPerPageStr;
	private Integer mRowsPerPage;

	private String mSelectionTypeStr;
	private SelectionType mSelectionType;
	private boolean mIsMultipleSelectionEnabled;
	private ShowSelector mShowSelector;

	private Boolean mHasAutoGrow;

	private ArrayList<TableDefinition> mItemLayouts;
	private ArrayList<TableDefinition> mOddItemLayouts;
	private ArrayList<TableDefinition> mEvenItemLayouts;
	private String mDefaultItemLayout;
	private String mDefaultSelectedItemLayout;

	private ArrayList<ILayoutActionDefinition> mMultipleSelectionActions;

	private boolean mHasInverseLoad;
	private boolean mHasPullToRefresh;

	public enum SelectionType
	{
		NoSelection,
		KeepWhileExecuting,
		KeepUntilNewSelection
	}

	public enum ShowSelector
	{
		None,
		Always,
		OnAction
	}

	public GridDefinition(LayoutDefinition layout, LayoutItemDefinition itemParent)
	{
		super(layout, itemParent);
	}

	@Override
	public void readData(INodeObject node)
	{
		super.readData(node);
		mAssociatedCollection = node.optString("@collection");
		mDefaultAction = node.optString("@defaultAction");
		mDataSourceName = node.optString("@DataProvider");
		mEmptyDataSetImage = MetadataLoader.getObjectName(node.optString("@emptyDataSetBackground"));
		mEmptyDataSetImageClass = node.optString("@emptyDataSetBackgroundClass");
		mEmptyDataSetText = node.optString("@emptyDataSetText");
		mEmptyDataSetTextClass = node.optString("@emptyDataSetTextClass");
		mRowsPerPageStr = node.optString("@rows");
		mShowSelector = processShowSelector(node.optString("@showSelector"));
		mDefaultItemLayout = node.optString("@defaultTable");
		mDefaultSelectedItemLayout = node.optString("@defaultSelectedItemLayout");

		mIsMultipleSelectionEnabled = node.optBoolean("@enableMultipleSelection");

		// Selection type is not calculated here because calculation depends on item layouts (loaded later).
		mSelectionTypeStr = node.optString("@selectionType");

		mHasInverseLoad = node.optBoolean("@inverseLoad");
		mHasPullToRefresh = node.optBoolean("@pullToRefresh", false);
	}

	private SelectionType processSelectionType(String selectionTypeValue) {
		if (isMultipleSelectionEnabled()) {
			return SelectionType.KeepUntilNewSelection;
		} else {
			switch (selectionTypeValue.toLowerCase()) {
				case "no selection": return SelectionType.NoSelection;
				case "keep selection while executing": return SelectionType.KeepWhileExecuting;
				case "keep until new selection": return SelectionType.KeepUntilNewSelection;
				default:
					if (Strings.hasValue(mDefaultAction))
						return SelectionType.KeepWhileExecuting;
					else if (getDefaultSelectedItemLayout(true) != null && getDefaultSelectedItemLayout(false) != null)
						return SelectionType.KeepUntilNewSelection;
					else
						return SelectionType.NoSelection;
			}
		}
	}

	private ShowSelector processShowSelector(String showSelectorValue) {
		switch (showSelectorValue.toLowerCase()) {
			case "always":
			case "platform default":
				return ShowSelector.Always;
			case "on action":
				return ShowSelector.OnAction;
			case "":
			default: // Either "no" or property not present (before GX version that enabled it).
				return ShowSelector.None;
		}
	}

	@Override
	public IDataSourceDefinition getDataSource()
	{
		// Return the data source (DP) associated to the grid.
		if (Services.Strings.hasValue(mDataSourceName))
			return getLayout().getParent().getDataSources().get(mDataSourceName);

		return super.getDataSource();
	}

	public Iterable<DataItem> getDataSourceItems()
	{
		if (getDataSource() == null)
			return new ArrayList<>();

		if (Services.Strings.hasValue(mAssociatedCollection))
		{
			// It's an SDT. Return the members of the SDT type.
			DataItem collectionDataItem = getDataSource().getDataItem(mAssociatedCollection);
			if (collectionDataItem == null)
			{
				// Member information not found.
				Services.Log.warning(String.format("Collection data item (%s) information was not found in specification of data source '%s'.", mAssociatedCollection, getDataSource().getName()));
				return new ArrayList<>();
			}

			IStructuredDataType collectionType = collectionDataItem.getTypeInfo(IStructuredDataType.class);
			if (collectionType == null)
			{
				// Member information does not point to an SDT definition.
				Services.Log.warning(String.format("Data item '%s' is not based on an SDT in specification of data source '%s'.", mAssociatedCollection, getDataSource().getName()));
				return new ArrayList<>();
			}

			return collectionType.getItems();
		}
		else
			return getDataSource().getDataItems();
	}

	public String getDataSourceMember()
	{
		return mAssociatedCollection;
	}

	private static final int ROWS_PER_PAGE_DEFAULT = 10;
	private static final int ROWS_PER_PAGE_UNLIMITED = 0;

	private static final String CONTROL_SD_HORIZONTAL_GRID = "SDHorizontalGrid";

	public int getRowsPerPage()
	{
		if (mRowsPerPage == null)
			mRowsPerPage = calculateRowsPerPage();

		return mRowsPerPage;
	}

	private int calculateRowsPerPage()
	{
		if (Strings.hasValue(mRowsPerPageStr))
		{
			if (mRowsPerPageStr.equalsIgnoreCase("<default>"))
				return ROWS_PER_PAGE_DEFAULT;

			if (mRowsPerPageStr.equalsIgnoreCase("<unlimited>"))
				return ROWS_PER_PAGE_UNLIMITED;

			Integer intValue = Services.Strings.tryParseInt(mRowsPerPageStr);
			return (intValue != null ? intValue : ROWS_PER_PAGE_DEFAULT);
		}

		return ROWS_PER_PAGE_DEFAULT;
	}

	public ActionDefinition getDefaultAction()
	{
		if (Services.Strings.hasValue(mDefaultAction))
			return getLayout().getParent().getEvent(mDefaultAction);

		return null;
	}

	public String getEmptyDataSetImage()
	{
		return mEmptyDataSetImage;
	}

	public String getEmptyDataSetImageClass()
	{
		return mEmptyDataSetImageClass;
	}

	public String getEmptyDataSetText()
	{
		return Services.Language.getTranslation(mEmptyDataSetText);
	}

	public String getEmptyDataSetTextClass()
	{
		return mEmptyDataSetTextClass;
	}

	public boolean isMultipleSelectionEnabled() {
		return mIsMultipleSelectionEnabled && getShowSelector() != GridDefinition.ShowSelector.None;
	}

	public SelectionType getSelectionType() {
		if (mSelectionType == null) {
			mSelectionType = processSelectionType(mSelectionTypeStr);
		}

		return mSelectionType;
	}

	public ShowSelector getShowSelector()
	{
		return mShowSelector;
	}

	public List<ILayoutActionDefinition> getMultipleSelectionActions()
	{
		if (mMultipleSelectionActions == null)
		{
			mMultipleSelectionActions = new ArrayList<>();
			for (ILayoutActionDefinition action : getLayout().getAllActions())
				if (isMultipleSelectionAction(action.getEvent()))
					mMultipleSelectionActions.add(action);
		}

		return mMultipleSelectionActions;
	}

	private boolean isMultipleSelectionAction(ActionDefinition action)
	{
		if (action != null)
		{
			// See if this action (or any of its components) has multiple selection over THIS grid.
			ActionDefinition.MultipleSelectionInfo msInfo = action.getMultipleSelectionInfo();
			if (msInfo != null && msInfo.useSelection() && msInfo.getGrid() != null && msInfo.getGrid().equalsIgnoreCase(getName()))
				return true;

			for (ActionDefinition component : action.getNextActions())
			{
				if (isMultipleSelectionAction(component))
					return true;
			}
		}

		return false;
	}

	public static String getMultipleSelectionAction(ActionDefinition action)
	{
		if (action != null)
		{
			// See if this action (or any of its components) has multiple selection over any grid.
			ActionDefinition.MultipleSelectionInfo msInfo = action.getMultipleSelectionInfo();
			if (msInfo != null && msInfo.useSelection() && msInfo.getGrid() != null)
				return msInfo.getGrid();

			for (ActionDefinition component : action.getNextActions())
			{
				String gridName = getMultipleSelectionAction(component);
				if (gridName != null)
					return gridName;
			}
		}

		return null;
	}

	/**
	 * Returns the expression that, when evaluated, indicates that an item is selected.
	 */
	public String getSelectionExpression()
	{
		return new ControlPropertiesDefinition(this).readDataExpression("@selectionFlag", "@selectionFlagFieldSpecifier");
	}

	public void calculateBounds(float absoluteWidth, float absoluteHeight)
	{
		if (getThemeClass()!=null)
		{
			LayoutBoxMeasures margins = getThemeClass().getMargins();
			LayoutBoxMeasures padding = getThemeClass().getPadding();
			absoluteWidth -= margins.getTotalHorizontal() + padding.getTotalHorizontal();
			absoluteHeight -= margins.getTotalVertical() + padding.getTotalVertical();
			for (TableDefinition itemLayout : getOddItemLayouts())
				itemLayout.calculateBounds(absoluteWidth, absoluteHeight);
			for (TableDefinition itemLayout : getEvenItemLayouts())
				itemLayout.calculateBounds(absoluteWidth, absoluteHeight);

		}
		else
		{
			for (TableDefinition itemLayout : getItemLayouts())
				itemLayout.calculateBounds(absoluteWidth, absoluteHeight);
		}
	}

	@Override
	public boolean hasAutoGrow()
	{
		if (mHasAutoGrow == null)
		{
			mHasAutoGrow = super.hasAutoGrow();

			// Autogrow is not supported in all cases, filter them here.
			if (mHasAutoGrow && mustDisableAutogrow())
				mHasAutoGrow = false;
		}

		return mHasAutoGrow;
	}

	public boolean gridUserControlSupportAutoGrow()
	{
		if (getControlInfo() != null) {
			UserControlDefinition gridControl = UcFactory.getControlDefinition(getControlInfo().getControl());
			if (gridControl != null && gridControl.SupportAutoGrow)
				return true;
		}
		return false;
	}

	public boolean gridIsHorizontalGridUserControl()
	{
		return getControlInfo() != null && CONTROL_SD_HORIZONTAL_GRID.equalsIgnoreCase(getControlInfo().getControl());
	}

	private boolean mustDisableAutogrow()
	{
		// For now only default grid supports autogrow.
		if (getControlInfo() != null)
		{
			// support autogrow for horizontal grid.
			if (gridUserControlSupportAutoGrow())
				return false;

			UserControlDefinition gridControl = UcFactory.getControlDefinition(getControlInfo().getControl());
			if (gridControl != null)
				return true;
		}

		// Item layouts cannot have a percentage height.
		for (TableDefinition itemLayout : getOddItemLayouts())
		{
			if (!itemLayout.hasDipHeight())
				return true;
		}
		for (TableDefinition itemLayout : getEvenItemLayouts())
		{
			if (!itemLayout.hasDipHeight())
				return true;
		}

		// No controls contained in this grid can have autogrow.
		// We disregards non-data ones, since they shouldn't grow unless they contain a data control that does.
		if (AutogrowFinder.containsDataControlsWithAutogrow(this))
			return true;

		// All ok.
		return false;
	}

	private static class AutogrowFinder implements ILayoutVisitor
	{
		public static boolean containsDataControlsWithAutogrow(GridDefinition grid)
		{
			AutogrowFinder finder = new AutogrowFinder();
			finder.mRoot = grid;
			finder.mOnlyDataControls = true;

			grid.accept(finder);
			return finder.mContainsAutogrow;
		}

		private GridDefinition mRoot;
		private boolean mOnlyDataControls;
		private boolean mContainsAutogrow;

		@Override
		public void enterVisitor(LayoutItemDefinition visitable) { }

		@Override
		public void visit(LayoutItemDefinition visitable)
		{
			if (!mContainsAutogrow)
			{
				if (visitable != mRoot && visitable.hasAutoGrow())
				{
					if (!mOnlyDataControls || LayoutItemsTypes.DATA.equalsIgnoreCase(visitable.getType()))
						mContainsAutogrow = true;
				}
			}
		}

		@Override
		public void leaveVisitor(LayoutItemDefinition visitable) { }
	}

	// restore method
	public List<TableDefinition> getItemLayouts()
	{
		if (mItemLayouts == null)
		{
			ArrayList<TableDefinition> itemLayouts = new ArrayList<>();
			for (LayoutItemDefinition itemLayout : getChildItems())
			{
				if (itemLayout instanceof TableDefinition)
					itemLayouts.add((TableDefinition)itemLayout);
			}

			mItemLayouts = itemLayouts;
		}

		return mItemLayouts;
	}

	// end restore method

	private ArrayList<TableDefinition> createItemLayouts(ThemeClassDefinition rowClass) {
		ArrayList<TableDefinition> itemLayouts = new ArrayList<>();
		for (LayoutItemDefinition itemLayout : getChildItems())
		{
			if (itemLayout instanceof TableDefinition) {
				TableDefinition definition = ((TableDefinition) itemLayout).cloneAndMergeClass(rowClass);
				itemLayouts.add(definition);
			}
		}
		return itemLayouts;
	}

	public List<TableDefinition> getOddItemLayouts() {
		if (mOddItemLayouts == null)
			mOddItemLayouts = createItemLayouts(getThemeClass().getThemeGridOddRowClass());
		return mOddItemLayouts;
	}

	public List<TableDefinition> getEvenItemLayouts()
	{
		if (mEvenItemLayouts == null)
			mEvenItemLayouts = createItemLayouts(getThemeClass().getThemeGridEvenRowClass());
		return mEvenItemLayouts;
	}

	public List<TableDefinition> getItemLayouts(boolean odd) {
		if (getThemeClass()!=null)
			return odd ? getOddItemLayouts() : getEvenItemLayouts();
		else
			return getItemLayouts();
	}

	public TableDefinition getItemLayout(String name, boolean odd) {
		if (Strings.hasValue(name))
		{
			if(getThemeClass()!=null) {
				for (TableDefinition itemLayout : odd ? getOddItemLayouts() : getEvenItemLayouts()) {
					if (name.equalsIgnoreCase(itemLayout.optStringProperty("@layoutName")))
						return itemLayout;
				}
			}
			else
			{
				for (TableDefinition itemLayout : getItemLayouts())
				{
					if (name.equalsIgnoreCase(itemLayout.optStringProperty("@layoutName")))
						return itemLayout;
				}
			}
		}

		return null;
	}

	/**
	 * Gets the default layout for grid items. Never returns null.
	 */
	public TableDefinition getDefaultItemLayout(boolean odd) {
		TableDefinition itemLayout = getItemLayout(mDefaultItemLayout, odd);

		// If the default layout is not specified (or not found), use the first one.
		if (itemLayout == null)
		{
			if (getThemeClass()!=null)
				itemLayout = getItemLayouts(odd).get(0);
			else
				itemLayout = getItemLayouts().get(0);
		}

		return itemLayout;
	}

	/**
	 * Gets the default layout for grid items when they are selected. May return null.
	 */
	public TableDefinition getDefaultSelectedItemLayout(boolean odd) {
		// Bugfix: DefaultSelectedItemLayout is *always* set.
		// Therefore, ignore it when it's the same as the default item layout (otherwise a custom layout is lost when selecting).
		if (mDefaultSelectedItemLayout.equalsIgnoreCase(mDefaultItemLayout))
			return null;

		return getItemLayout(mDefaultSelectedItemLayout, odd);
	}

	public boolean hasBreakBy()
	{
		IDataSourceDefinition dataSource = getDataSource();
		if (dataSource != null)
			return dataSource.getOrders().hasAnyWithBreakBy();
		else
			return false;
	}

	public boolean hasPullToRefresh()
	{
		return mHasPullToRefresh;
	}

	public boolean hasInverseLoad()
	{
		return mHasInverseLoad;
	}
}
