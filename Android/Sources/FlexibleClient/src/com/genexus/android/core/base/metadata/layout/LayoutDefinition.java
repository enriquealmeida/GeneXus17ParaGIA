package com.genexus.android.core.base.metadata.layout;

import com.genexus.android.layout.TablesLayoutVisitor;
import com.genexus.android.core.base.metadata.ActionDefinition;
import com.genexus.android.core.base.metadata.IDataSourceDefinition;
import com.genexus.android.core.base.metadata.IDataViewDefinition;
import com.genexus.android.core.base.metadata.ILayoutDefinition;
import com.genexus.android.core.base.metadata.SectionDefinition;
import com.genexus.android.core.base.metadata.enums.LayoutItemsTypes;
import com.genexus.android.core.base.metadata.enums.Orientation;
import com.genexus.android.core.base.metadata.loader.MetadataLoader;
import com.genexus.android.core.base.metadata.settings.PlatformDefinition;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.serialization.INodeCollection;
import com.genexus.android.core.base.serialization.INodeObject;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.common.LayoutHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LayoutDefinition
	implements ILayoutDefinition, ILayoutItemVisitable, Serializable
{
	private static final long serialVersionUID = 1L;

	private String mType;
	private String mPlatform;
	private String mOrientation;
    private String mId;

	private Orientation mActualOrientation;
	private boolean mIsOrientationSwitchable;

	private final IDataViewDefinition mParent;
	private final ArrayList<LayoutItemDefinition> mItems = new ArrayList<>();
	private ActionBarDefinition mActionBar;
	private ArrayList<ActionGroupDefinition> mActionGroups;
	private PlatformDefinition mPlatformDefinition;
	private String mThemeClass;
	private String mEmptyDataSetBackground;
	private INodeObject mContent;

	private LayoutPromptsDefinition mLayoutPrompts;
	private LayoutItemLookup mItemLookup;

	public static final String TYPE_ANY = Strings.EMPTY;
	public static final String TYPE_VIEW = "View";
	public static final String TYPE_EDIT = "Edit";

	private static final String ORIENTATION_PORTRAIT = "Portrait";
	private static final String ORIENTATION_LANDSCAPE = "Landscape";

    public LayoutDefinition(IDataViewDefinition parent)
	{
		mParent = parent;
	}

	public TableDefinition getTable()
	{
		return (mItems.size() != 0 ? (TableDefinition)mItems.get(0) : null);
	}

	public IDataViewDefinition getParent() { return mParent; }

	@Override
	public String toString()
	{
		return String.format("[%s] %s (Orientation: %s)", getType(), getPlatformDefinition(), getOrientation());
	}

	public IDataSourceDefinition getDataSource()
	{
		return getParent().getMainDataSource();
	}

    public String getId()
    {
        if (mContent == null)
            return mId;

        return mContent.optString("@id", Strings.EMPTY);
    }

	public String getType()
	{
		if (mContent == null)
			return mType;

		return mContent.optString("@Type", Strings.EMPTY);
	}

	public PlatformDefinition getPlatformDefinition()
	{
		if (mPlatformDefinition == null)
		{
			if (mContent != null)
				mPlatform = mContent.optString("@Platform", Strings.EMPTY);

			mPlatformDefinition = Services.Application.getDefinition().getPatternSettings().getPlatform(mPlatform);
		}

		return mPlatformDefinition;
	}

	public Orientation getOrientation()
	{
		if (mOrientation == null && mContent != null)
			mOrientation = mContent.optString("@Orientation");

		if (mOrientation != null)
		{
			if (mOrientation.equalsIgnoreCase(ORIENTATION_PORTRAIT))
				return Orientation.PORTRAIT;
			else if (mOrientation.equalsIgnoreCase(ORIENTATION_LANDSCAPE))
				return Orientation.LANDSCAPE;
		}

		return Orientation.UNDEFINED; // Any.
	}

	public void setActualOrientation(Orientation orientation, boolean canRotate)
	{
		mActualOrientation = orientation;
		mIsOrientationSwitchable = canRotate;
	}

	public Orientation getActualOrientation()
	{
		return (mActualOrientation != null ? mActualOrientation : getOrientation());
	}

	public boolean isOrientationSwitchable()
	{
		return mIsOrientationSwitchable;
	}

	public ActionBarDefinition getActionBar() { return mActionBar; }
	public List<ActionGroupDefinition> getActionGroups() { return mActionGroups; }

	private ArrayList<ILayoutActionDefinition> mAllActions = null;

	/**
	 * Gets the list of all actions associated to this layout (either in the action bar, or in the form itself).
	 */
	List<ILayoutActionDefinition> getAllActions()
	{
		if (mAllActions == null)
		{
			mAllActions = new ArrayList<>();

			 // Don't duplicate if same event is associated to two controls.
			ArrayList<ActionDefinition> events = new ArrayList<>();

			// From action bar.
			for (ILayoutActionDefinition action : getActionBar().getActions())
			{
				if (action.getEvent() != null && !events.contains(action.getEvent()))
				{
					mAllActions.add(action);
					events.add(action.getEvent());
				}
			}

			if (getTable() != null)
			{
				for (ILayoutActionDefinition action : getTable().findChildrenOfType(LayoutActionDefinition.class))
				{
					if (action.getEvent() != null && !events.contains(action.getEvent()))
						mAllActions.add(action);
				}
			}
		}

		return mAllActions;
	}

	@Override
	public boolean getShowApplicationBar()
	{
		return mActionBar.isVisible();
	}

	@Override
	public boolean getEnableHeaderRowPattern()
	{
		if (getTable()!=null)
		{
			return getTable().getEnableHeaderRowPattern();
		}
		return false;
	}

	@Override
	public ThemeClassDefinition getHeaderRowApplicationBarClass()
	{
		if (getTable()!=null)
		{
			return Services.Themes.getThemeClass(getTable().getHeaderRowApplicationBarClass());
		}
		return null;
	}

	@Override
	public ThemeClassDefinition getApplicationBarClass()
	{
		return mActionBar.getThemeClass();
	}

	public void getDataItems(LayoutItemDefinition parentItem, List<LayoutItemDefinition> data)
	{
		List<LayoutItemDefinition> items;
		if (parentItem != null)
			items = parentItem.getChildItems();
		else
			items = mItems;

		for (int i = 0; i < items.size(); i++)
		{
			LayoutItemDefinition item = items.get(i);
	    	if (item.getType().equalsIgnoreCase(LayoutItemsTypes.DATA))
	    	{
	    		data.add(item);
	    	}
	    	else
	    	{
	    		getDataItems(item, data);
	    	}
		}
	}

	@Override
	public void accept(ILayoutVisitor visitor)
	{
		// We just take the first item because the definition always has only one parent (a table)
		if (mItems.size() > 0)
			mItems.get(0).accept(visitor);
	}

	public ThemeClassDefinition getThemeClass()
	{
		 return Services.Themes.getThemeClass(mThemeClass);
	}

	public void setContent(INodeObject jsonLayout)
	{
		mContent = jsonLayout;
	}

	public void deserialize()
	{
		if (mContent != null)
		{
			mType = mContent.optString("@Type", Strings.EMPTY);
			mPlatform = mContent.optString("@Platform", Strings.EMPTY);
			mOrientation = mContent.optString("@Orientation");
			mThemeClass = mContent.optString("@class", Strings.EMPTY);
            mId = mContent.optString("@id", Strings.EMPTY);
			mEmptyDataSetBackground = MetadataLoader.getObjectName(mContent.optString("@emptyDataSetBackground"));

			mActionBar = new ActionBarDefinition(this, mContent.getNode("actionBar"));

			mActionGroups = new ArrayList<>();
			readActionGroups(mContent, this, mActionGroups);

			readLayoutItems(mContent, this, null);

			//if layout has ads add it here
			if (Services.Application.get().getUseAds() &&
				getParent().getShowAds() &&
				!(getParent() instanceof SectionDefinition))
			{
				//add another row to main table
				if (getTable() != null && !TablesLayoutVisitor.hasAdsTable(this))
				{
					//get ads size in this device in dpi
					int adHeight = LayoutHelper.DEFAULT_AD_CONTROL_HEIGHT_DP;
					//get position at the bottom.
					int positionY = Services.Device.pixelsToDips(getTable().getFixedHeightSum());

					String cellContent = "\"table\": { \"@controlName\": \"GoogleAdsControl\", \"@width\": \"100%\",\"@height\": \"100%\", " +
		            "  \"@visible\": \"True\", \"@FixedHeightSum\": \"0\", \"@FixedWidthSum\": \"0\" }";

					LayoutItemDefinition layoutRowItemDef = LayoutHelper.getRowWithCell(this, getTable(), cellContent, String.valueOf(adHeight) + "dpi",
							Strings.ZERO, String.valueOf(positionY), Strings.ZERO, "100",
							Strings.ZERO , String.valueOf(adHeight), "100", Strings.ZERO);

					//TODO at the top must move all controls y down.
					if (layoutRowItemDef!=null)
					{
						getTable().getChildItems().add(layoutRowItemDef);
						//	change fix FixedHeightSum
						getTable().addToFixedHeightSum(Services.Device.dipsToPixels(adHeight));
					}
				}
			}

			mContent = null;
		}

		if (mItemLookup == null)
			mItemLookup = new LayoutItemLookup(this);

		if (mLayoutPrompts == null)
			mLayoutPrompts = new LayoutPromptsDefinition(this);
	}

	public String getEmptyDataSetBackground()
	{
		return mEmptyDataSetBackground;
	}

	public LayoutItemDefinition getControl(String name)
	{
		return mItemLookup.getControl(name);
	}

	public LayoutItemDefinition getDataControl(String dataId)
	{
		return mItemLookup.getDataControl(dataId);
	}

	LayoutPromptsDefinition getPrompts()
	{
		return mLayoutPrompts;
	}

	public static void readLayoutItems(INodeObject json, LayoutDefinition layout, LayoutItemDefinition itemParent)
	{
		// Read child in Order
		for (String attName : json.names())
		{
			if (!attName.startsWith("@"))
			{
				// Check if we can create this kind of node
				LayoutItemDefinition dummyLayoutItem = LayoutItemDefinitionFactory.createDefinition(layout, itemParent, attName);
				if (dummyLayoutItem != null)
				{
					// Actually, might be many instances of this node, read each one.
					INodeCollection layoutArray = json.optCollection(attName);
					for (int k = 0; k < layoutArray.length() ; k++)
					{
						INodeObject singleJson = layoutArray.getNode(k);
						if (singleJson != null) { // ignore null nodes, i.e. empty rows in flex table
							LayoutItemDefinition layoutItem = LayoutItemDefinitionFactory.createDefinition(layout, itemParent, attName);
							if (layoutItem == null)
								throw new IllegalStateException("layoutItem cannot be null if dummyLayoutItem wasn't!");

							layoutItem.setType(attName);

							layoutItem.readData(singleJson);
							readLayoutItems(singleJson, layout, layoutItem);

							// Add to parent (or as root)
							if (itemParent != null)
								itemParent.getChildItems().add(layoutItem);
							else
								layout.mItems.add(layoutItem);
						}
					}
				}
				else
				{
					if (itemParent != null)
						itemParent.setProperty(attName, json.getString(attName));
				}
			}
			else
			{
				if (itemParent != null)
					itemParent.setProperty(attName, json.getString(attName));
			}
		}
	}

	private static void readActionGroups(INodeObject content, LayoutDefinition layout, ArrayList<ActionGroupDefinition> collection)
	{
		collection.clear();

		INodeObject groupsNode = content.getNode("actionGroups");
		if (groupsNode != null)
		{
			for (INodeObject groupNode : groupsNode.optCollection("actionGroup"))
			{
				ActionGroupDefinition group = new ActionGroupDefinition(layout, groupNode);
				collection.add(group);
			}
		}
	}
}
