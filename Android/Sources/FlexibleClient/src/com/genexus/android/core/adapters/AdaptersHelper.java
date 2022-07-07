package com.genexus.android.core.adapters;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Insets;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.WindowMetrics;
import android.widget.CheckBox;

import androidx.annotation.RequiresApi;

import com.genexus.android.R;
import com.genexus.android.core.actions.UIContext;
import com.genexus.android.core.activities.ActivityLauncher;
import com.genexus.android.core.base.metadata.AttributeDefinition;
import com.genexus.android.core.base.metadata.DataItem;
import com.genexus.android.core.base.metadata.DataTypeName;
import com.genexus.android.core.base.metadata.ILayoutDefinition;
import com.genexus.android.core.base.metadata.RelationDefinition;
import com.genexus.android.core.base.metadata.StructureDefinition;
import com.genexus.android.core.base.metadata.enums.DisplayModes;
import com.genexus.android.core.base.metadata.enums.LayoutModes;
import com.genexus.android.core.base.metadata.enums.Orientation;
import com.genexus.android.core.base.metadata.layout.GridDefinition;
import com.genexus.android.core.base.metadata.layout.LayoutDefinition;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.base.metadata.layout.Size;
import com.genexus.android.core.base.metadata.layout.TableDefinition;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.model.EntityFactory;
import com.genexus.android.core.base.model.EntityHelper;
import com.genexus.android.core.base.model.EntityList;
import com.genexus.android.core.base.model.ValueCollection;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.common.DataRequest;
import com.genexus.android.core.common.FormatHelper;
import com.genexus.android.core.common.PhoneHelper;
import com.genexus.android.core.controllers.IDataSourceBoundView;
import com.genexus.android.core.controllers.ViewData;
import com.genexus.android.core.controls.DataBoundControl;
import com.genexus.android.core.controls.IGxActionControl;
import com.genexus.android.core.controls.IGxEdit;
import com.genexus.android.core.controls.grids.GridItemViewInfo;
import com.genexus.android.core.providers.ColumnDefinition;
import com.genexus.android.core.utils.Cast;

public class AdaptersHelper
{
	// cache of sizes from android ID_ANDROID_CONTENT.
	// height default without actionbar.
	private static int mPortraitHeight = 0;
	private static int mLandscapeHeight= 0;

	public static void cacheWindowSizes(Activity activity, Orientation deviceOrientation, int height, int witdh, boolean useStatusBar)
	{
		Services.Log.debug("cache ID_ANDROID_CONTENT " + deviceOrientation.toString() + " h: " + height + " w: " + witdh );

		if (deviceOrientation== Orientation.PORTRAIT)
		{
			// in portail the height is the value ID_ANDROID_CONTENT give to as.
			mPortraitHeight = height;
			if (useStatusBar)
				mPortraitHeight = height - getStatusBarHeight(activity);

		}

		if (deviceOrientation== Orientation.LANDSCAPE)
		{
			// in landscape the height is the value ID_ANDROID_CONTENT give to as.
			mLandscapeHeight = height;
			if (useStatusBar)
				mLandscapeHeight = height - getStatusBarHeight(activity);

		}
		// if orientation undefined , do not cache it.
		Services.Log.debug("cache ID_ANDROID_CONTENT " + deviceOrientation.toString() + " ph: " + mPortraitHeight + " lh: " + mLandscapeHeight );

	}

	public static boolean hasCacheWindowSizes(Orientation deviceOrientation)
	{
		Services.Log.debug("has cache window " + deviceOrientation.toString() );
		if (deviceOrientation== Orientation.PORTRAIT)
		{
			if (mPortraitHeight>0)
				return true;
		}
		if (deviceOrientation== Orientation.LANDSCAPE)
		{
			if (mLandscapeHeight>0)
				return true;
		}
		Services.Log.debug("has cache window not found. false " + deviceOrientation.toString() );
		return false;
	}
	public static void clearCacheWindowSizes()
	{
		mPortraitHeight = 0;
		mLandscapeHeight = 0;
		Services.Log.debug("cache ID_ANDROID_CONTENT empty  ph: " + mPortraitHeight + " lh: " + mLandscapeHeight );

	}

	public static void setBounds(LayoutDefinition layout, Size size)
	{
		setBounds(layout.getTable(), size.getWidth(), size.getHeight());
	}

	public static void setBounds(TableDefinition table, int width, int height)
	{
		if (table == null)
		{
			Services.Log.error("setBounds", "No layout in set bounds");
			return;
		}
		table.calculateBounds(width, height);
	}

	public static Size getWindowSize(Activity activity, ILayoutDefinition definition)
	{
		return new Size(getWindowWidth(activity), getWindowHeight(activity, definition));
	}

	/**
	 * Tries to get the available width from the (previously measured) view of the activity.
	 * If if fails then calls getDisplayWidth().
	 */
	public static int getWindowWidth(Activity activity)
	{
		int width = 0;

		//Doesn't work on create method, should call it from another place?
		View content = activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT);
		if (content!=null)
			width = content.getWidth();

		if (width == 0)
			width = getDisplayWidth(activity);

    	return width;
	}

	/**
	 * Gets the available width using only the DisplayMetrics class and removing known decorations.
	 */
	@SuppressWarnings("deprecation")
	private static int getDisplayWidth(Activity activity)
	{
		Display display = activity.getWindowManager().getDefaultDisplay();
		return display.getWidth();
	}

	/**
	 * Tries to get the available height from the root view of the activity.
	 * If if fails then calls getDisplayHeight().
	 */
	public static int getWindowHeight(Activity activity, ILayoutDefinition layout)
	{
		int height = 0;


		if (height==0)
		{
			// use the cache value if possible
			int orientation = activity.getResources().getConfiguration().orientation;
			if (orientation== Configuration.ORIENTATION_LANDSCAPE
					&& mLandscapeHeight!=0)
			{
				Services.Log.debug(" use cache ID_ANDROID_CONTENT  landscape height " + mLandscapeHeight);
				// use cache ID_ANDROID_CONTENT
				height = mLandscapeHeight;
			}
			if (orientation== Configuration.ORIENTATION_PORTRAIT
					&& mPortraitHeight!=0)
			{
				Services.Log.debug(" use cache ID_ANDROID_CONTENT  portrait height " + mPortraitHeight);
				// use cache ID_ANDROID_CONTENT
				height = mPortraitHeight;
				// if header row add status bar

			}
			if (height>0)
			{
				// add status bar if needed,
				// StatusBarOverlayingAvailable in Android 5.x or up
				if (layout != null && layout.getEnableHeaderRowPattern() )
				{
					height = height + getStatusBarHeight(activity);
					Services.Log.debug(" use cache ID_ANDROID_CONTENT  portrait height plus action bar " + height);
				}
				// remove action bar if needed
				height = removeActionBarSize(activity, layout, height);
				// use value from cache
				return height;
			}
		}

		//Doesn't work on create method, should call it from another place?
		View content = activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT);
		if (content!=null)
			height = content.getHeight();

		// Fallback to old methos, it shoult not be used anymore
		if (height == 0 )
		{
			Services.Log.warning("Calling getDisplayHeight , no cache height.");
			height = getDisplayHeight(activity, layout);
		}
		else
		{
			// remove actionbar is layout said that.
			height = removeActionBarSize(activity, layout, height);
		}

    	return height;
	}

	public static int getWindowHeightForDahsboardTabs(Activity activity, ILayoutDefinition layout)
	{
		int height = 0;

		// Priority ID_ANDROID_CONTENT in Dashboard as tabs, becuase works ok here, do not need cache.
		//Doesn't work on create method, should call it from another place?
		View content = activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT);
		if (content!=null)
			height = content.getHeight();

		if (height == 0 )
		{
			return getWindowHeight(activity, layout);
		}
		else
		{
			Services.Log.debug(" getWindowHeightForDahsboardTabs remove status bar to ID_ANDROID_CONTENT if necessary " + height);
			height = removeActionBarSize(activity, layout, height);
		}
		return height;
	}

	private static int removeActionBarSize(Activity activity, ILayoutDefinition layout, int height)
	{
		// remove action bar from android content
		int titleBarHeight = getTitleBarHeight(activity, layout);
		// ActionBar EnableHeaderRowPattern
		if (layout != null && layout.getEnableHeaderRowPattern())
			titleBarHeight = 0;

		height = height - titleBarHeight;
		return height;
	}

	/**
	 * Gets the available height using only the DisplayMetrics class and removing known decorations.
	 */
	@SuppressWarnings("deprecation")
	private static int getDisplayHeight(Activity activity, ILayoutDefinition layout)
	{
		DisplayMetrics displayMetrics = new DisplayMetrics();
		Display display = activity.getWindowManager().getDefaultDisplay();
		display.getMetrics(displayMetrics);
		int height = display.getHeight();

		// Remove decorations.
		// Supposedly this should work for any Android version (?)
		// Tested in Nexus 4/7/10, Galaxy S4 / Tab 10.1 / Tab 2 10.1
		int statusBarHeight = getStatusBarHeight(activity);

		// StatusBar EnableHeaderRowPattern
		// StatusBarOverlayingAvailable in Android 5.x or up
		if (layout != null && layout.getEnableHeaderRowPattern() )
		{
			statusBarHeight = 0;
		}

		// if has cutout status bar already remove from size, like system buttons
		if (hasCutOut(activity))
		{
			statusBarHeight = 0;
			// StatusBarOverlayingAvailable in Android 5.x or up
			if (layout != null && layout.getEnableHeaderRowPattern() )
			{
				// this could not work if do not have cutout on top. Pending ask for this.
				height = height + getStatusBarHeight(activity);
			}
			// add the difference in the button cutout. if not button it should be 0.
			int difference = getCutOutDifference(activity);

			// rest cutout difference. China test. Max action bar size.
			difference = Math.max(difference, getStatusBarHeight(activity));
			height = height - difference;

		}

		// remove status bar size if necessary.
		height = height - statusBarHeight;

		//title bar
		height = removeActionBarSize(activity, layout, height);

		Services.Log.info("WindowDisplay Height", String.valueOf(height));
		return height;
	}

	public static int getStatusAndActionBarHeight(Activity activity, ILayoutDefinition layout)
	{
		int statusActionBarHeight = 0;
		statusActionBarHeight += AdaptersHelper.getStatusBarHeight(activity);
		statusActionBarHeight += getTitleBarHeight(activity, layout);
		return statusActionBarHeight;

	}

	public static int getStatusBarHeight(Activity activity)
	{
	    int statusBarHeight = 0;

		int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0)
			statusBarHeight = activity.getResources().getDimensionPixelSize(resourceId);

		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
			Services.Log.debug("status bar height: " + statusBarHeight   );
			final WindowManager windowManager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);

			// from: https://stackoverflow.com/a/47019192 use WindowInsets to get status bar height in API 30 or up.
			final WindowMetrics metrics = windowManager.getCurrentWindowMetrics();
			// Gets all excluding insets
			final WindowInsets windowInsets = metrics.getWindowInsets();
			Insets insets = windowInsets.getInsetsIgnoringVisibility(
			WindowInsets.Type.navigationBars() | WindowInsets.Type.displayCutout());

			if (insets.top > 0) {
				statusBarHeight = insets.top;
				Services.Log.debug("new status bar height: " + statusBarHeight );
			}
		}
		return statusBarHeight;
	}

	// cut out support. warning , it should not work in multi window mode.
	// Pending, convert to API 28 to query cutout better.
	private static boolean hasCutOut(Activity activity)
	{
		// no cutout before oreo
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
		{
			Services.Log.debug(" No cutout before oreo");
			return false;
		}
		// cutout only in portrait
		int orientation = activity.getResources().getConfiguration().orientation;
		if (orientation== Configuration.ORIENTATION_LANDSCAPE)
		{
			Services.Log.debug(" No cutout ORIENTATION_LANDSCAPE");
			return false;
		}

		int difference = getCutOutDifference(activity);
		if (difference>=0)
		{
			Services.Log.debug(" Device has cutout. difference is  " + difference);
			return true;
		}
		return false;
	}

	private static int getCutOutDifference(Activity activity) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
			return getCutOutDifferenceApi30(activity);
		} else {
			return getCutOutDifferenceDeprecated(activity);
		}
	}

	@RequiresApi(Build.VERSION_CODES.R)
	private static int getCutOutDifferenceApi30(Activity activity) {
		final WindowInsets windowInsets = activity.getWindowManager().getCurrentWindowMetrics().getWindowInsets();
		Insets insets = windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.displayCutout());
		return insets.bottom - insets.top;
	}

	@SuppressWarnings("deprecation")
	private static int getCutOutDifferenceDeprecated(Activity activity)
	{
		//hasCutOut in status bar

		Display display = activity.getWindowManager().getDefaultDisplay();
		DisplayMetrics displayMetrics = new DisplayMetrics();
		display.getMetrics(displayMetrics);

		Point size = new Point();
		display.getSize(size);
		int height = size.y;
		Services.Log.debug(" Display get size " + height);

		Point totalSize = new Point();
		display.getRealSize(totalSize);

		int totalHeight = totalSize.y;

		int widthDips = Services.Device.getScreenWidth();
		int heightDips = Services.Device.getScreenHeight();
		Services.Log.debug("Smallest width (dips): " + Services.Device.getScreenSmallestWidth());
		Services.Log.debug("Screen size (dips): " + widthDips + "x" + heightDips); // Screen size
		Services.Log.debug("Screen size (pixels): " + Services.Device.dipsToPixels(widthDips) + "x" + Services.Device.dipsToPixels(heightDips));
		int navBar = getNavigationBarHeight(activity);
		int statusBar = getStatusBarHeight(activity);

		Services.Log.debug(" Display cut out data total: " + totalHeight+ " height: " + height + " navBar: " + navBar + " statusBar: " + statusBar);
		int difference = totalHeight - height - navBar - statusBar;
		Services.Log.debug(" Display difference " + difference);

		return difference;
	}

	private static int getNavigationBarHeight(Activity activity)
	{
		int resourceId = activity.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
		// use has navigation bar property to know if has nav menu on screen
		if (resourceId > 0&& hasNavigationBar(activity) )
		{
			return activity.getResources().getDimensionPixelSize(resourceId);
		}
		return 0;
	}

	private static boolean hasNavigationBar(Activity activity) {
		boolean hasMenuKey = ViewConfiguration.get(activity.getBaseContext()).hasPermanentMenuKey();
		//boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
		int hasNavBarId = activity.getResources().getIdentifier("config_showNavigationBar",
				"bool", "android");
		// not have keys or have navbar.
		return !hasMenuKey || (hasNavBarId > 0 && activity.getResources().getBoolean(hasNavBarId));
	}


	private static final int TITLE_BAR_HEIGHT_IN_DIP = 48;

	public static int getTitleBarHeight(Activity activity, ILayoutDefinition layout)
	{
		if (layout != null && layout.getShowApplicationBar())
		{
			final TypedArray styledAttributes = activity.getTheme().obtainStyledAttributes(new int[] { R.attr.actionBarSize });
			int size = (int) styledAttributes.getDimension(0, 0);
			styledAttributes.recycle();			
			
			if (size == 0)
				return Services.Device.dipsToPixels(TITLE_BAR_HEIGHT_IN_DIP);
			else
				return size;
		}
		else
			return 0; // No title bar.
	}

	public static boolean hasOnClickAction(DataBoundControl control, short layoutMode, short trnMode)
	{
	    LayoutItemDefinition controlDef = control.getDefinition();
		if (!controlDef.getReadOnly(layoutMode, trnMode))
			return false;

		DataTypeName domainDefinition = controlDef.getDataTypeName();
		if (domainDefinition != null && domainDefinition.getActions().size() != 0)
		{
			// Semantic domain action.
			// In grids, setting autolink to false disables it. Everywhere else it does not.
			if (layoutMode == LayoutModes.LIST)
			{
				if (controlDef.getAutoLink())
					return true;
			}
			else
				return true;
		}

		RelationDefinition relDef = controlDef.getFK();
		if (relDef != null && controlDef.getAutoLink())
			return true; // FK link.

	    return false;
	}

	public static void launchDomainAction(UIContext context, View v, Entity entity)
	{
		if (!(v instanceof DataBoundControl))
			return;

		DataBoundControl actionDomainControl = (DataBoundControl)v;

	    LayoutItemDefinition formAttDef = actionDomainControl.getDefinition();
		DataTypeName domainDefinition = formAttDef.getDataTypeName();
		//Actions, first FK
		RelationDefinition relDef = formAttDef.getFK();

		if (relDef != null && formAttDef.getAutoLink())
		{
			ActivityLauncher.callRelated(context, entity, relDef);
		}
		else if (domainDefinition != null && domainDefinition.getActions().size()>0)
		{
			String type = domainDefinition.getActions().get(0);
			String value = actionDomainControl.getGxValue();
			PhoneHelper.launchDomainAction(context, type, value);
		}
	}

	public static void loadGrid(IDataSourceBoundView view, ViewData sourceData, Entity entity, boolean useLastMemberPart)
	{
		EntityList gridData = null;
		String member = view.getDataSourceMember();
		if (Services.Strings.hasValue(member))
		{
			if (useLastMemberPart) {
				int lastIndex = member.lastIndexOf('.');
				if (lastIndex >= 0)
					member = member.substring(lastIndex + 1);
			}
			Object property = entity.getProperty(member);
			ValueCollection valueCollection = Cast.as(ValueCollection.class, property);
			if (valueCollection != null)
				gridData = convertToEntityList(valueCollection);
			else
				gridData = Cast.as(EntityList.class, property);
		}

		if (gridData != null) {
			if (sourceData == null)
				view.update(ViewData.customData(gridData, DataRequest.RESULT_SOURCE_LOCAL));
			else
				view.update(ViewData.memberData(sourceData, gridData));
		} else {
			view.update(ViewData.empty(false));
		}
	}

	public static EntityList convertToEntityList(ValueCollection valueCollection) {
		DataItem item = new DataItem(new AttributeDefinition(null));
		item.setProperty("Name", VALUE_COLLECTION_IN_ENTITY);
		StructureDefinition definition = new StructureDefinition("ValueCollection");
		definition.getItems().add(item);
		EntityList gridData = new EntityList(definition);
		for (int n = 0; n < valueCollection.size(); n++) {
			Entity newEntity = EntityFactory.newEntity(definition);
			newEntity.setProperty(VALUE_COLLECTION_IN_ENTITY, valueCollection.get(n));
			gridData.add(newEntity);
		}
		return gridData;
	}

	public static boolean drawListItem(ViewData data, GridItemViewInfo itemView, Entity itemEntity,
	                                   OnClickListener actionHandler, OnClickListener domainActionHandler,
	                                   boolean notReuseViews, boolean inSelectionMode, GridDefinition gridDefinition)
	{
		// Bind the data with the holder.
		for (View view : itemView.getBoundViews())
		{
			if (view instanceof IGxEdit)
			{
				IGxEdit edit = (IGxEdit)view;
				ArrayList<Integer> childIndexes = new ArrayList<>();
				// Get the entity from which properties can be "getted" (may not be the same as the collection item).
				Entity dataEntity = EntityHelper.forEvaluationIndex(itemEntity, childIndexes, edit.getGxTag()); // each edit can have different childIndexes

				setEditValue(edit, dataEntity, childIndexes);

				// Don't reuse views if there are any editable controls.
				// Otherwise, when an edit with focus is reused, the ListView "jumps" as the item comes into view.
				// Also, it messes "tab order" since the controls in successive rows have the same id.
				if (edit.isEditable())
					notReuseViews = true;
			}
			else if (view instanceof IGxActionControl)
			{
				IGxActionControl action = (IGxActionControl) view;
				action.setEntity(itemEntity);
				action.setOnClickListener(actionHandler);
			}
			else if (view instanceof IDataSourceBoundView)
			{
				loadGrid((IDataSourceBoundView)view, data, itemEntity, true);
			}

			DataBoundControl dataControl = Cast.as(DataBoundControl.class, view);
			if (dataControl != null && AdaptersHelper.hasOnClickAction(dataControl, LayoutModes.LIST, DisplayModes.VIEW))
			{
				dataControl.setOnClickListener(domainActionHandler);
				dataControl.setEntity(itemEntity);
			}
		}

		CheckBox itemCheckbox = itemView.getCheckbox();
		if (inSelectionMode)
		{
			itemCheckbox.setVisibility(View.VISIBLE);
			itemCheckbox.setChecked(itemEntity.isSelected());
			if (itemCheckbox.getTag() == null)
				itemCheckbox.setTag(itemEntity);
		}
		else
		{
			if (itemCheckbox != null)
				itemCheckbox.setVisibility(View.GONE);
		}

		return notReuseViews;
	}

	public static void setEditValue(IGxEdit edit, Entity entity)
	{
		// null in itemPositions means "do not recalculate dataId".
		setEditValue(edit, entity, null);
	}

	private static void setEditValue(IGxEdit edit, Entity entity, ArrayList<Integer> itemPositions)
	{
		String dataId = edit.getGxTag();

		DataBoundControl control = getControlFromEdit(edit);
		if (control != null)
		{
			LayoutItemDefinition definition = control.getDefinition();
			if (definition.getDataItem().isCollection() &&
					definition.getDataItem().getStorageType() != ColumnDefinition.TYPE_STRUCTURED &&
					dataId.endsWith("item(0)")) {
				dataId = VALUE_COLLECTION_IN_ENTITY; // Collection of basic type
			}
			else if (itemPositions != null) {
				// Resolve data item expression if necessary. Why is done here instead of
				// when the control is created? Because list item views are reused under some
				// conditions, and in those cases the expression would keep the old assigned
				// position. So we recalculate it to force use of the current one.
				dataId = definition.getDataId(itemPositions);
				edit.setGxTag(dataId);
			}
		}

		if (dataId == null)
			return;

		edit.setGxValue(entity.optStringProperty(dataId));
	}

	/**
	 * Posts the values of all editable controls in the bound views list into
	 * their corresponding properties in the supplied Entity.
	 */
	public static void saveEditValues(List<View> boundViews, Entity data)
	{
		for (View view : boundViews)
		{
			IGxEdit edit = Cast.as(IGxEdit.class, view);
			if (edit != null)
				AdaptersHelper.saveEditValue(edit, data);
		}
	}

	/**
	 * If the edit control is a view that can be edited (i.e. it's not read-only) then save its
	 * value to the property it is bound to in the supplied Entity.
	 */
	public static boolean saveEditValue(IGxEdit edit, Entity data)
	{
		if (edit.isEditable())
		{
			String name = edit.getGxTag();
			String value = edit.getGxValue();
			if (name != null && value != null)
				return data.setProperty(edit.getGxTag(), value);
		}

		return false;
	}

	public static CharSequence getFormattedText(Entity entity, String dataId, DataItem dataItem)
	{
		String value = entity.optStringProperty(dataId);
		if (dataItem != null)
		{
			CharSequence cs = FormatHelper.formatValue(value, dataItem);
			if (cs != null)
				return cs;
		}

		return value;
	}

	private static DataBoundControl getControlFromEdit(IGxEdit edit)
	{
		if (edit instanceof DataBoundControl)
			return (DataBoundControl)edit;

		if (edit instanceof View)
			return Cast.as(DataBoundControl.class, ((View)edit).getParent());

		return null;
	}

	public static final String VALUE_COLLECTION_IN_ENTITY = "value";
}
