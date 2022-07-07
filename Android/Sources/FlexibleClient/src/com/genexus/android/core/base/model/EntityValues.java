package com.genexus.android.core.base.model;

import com.genexus.android.core.base.metadata.DataItem;
import com.genexus.android.core.base.metadata.DataItemHelper;
import com.genexus.android.core.base.metadata.LevelDefinition;
import com.genexus.android.core.base.metadata.StructureDefinition;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.NameMap;
import com.genexus.android.core.base.utils.Strings;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

class EntityValues extends PropertiesObject
{
	private static final long serialVersionUID = 1L;

	private String mKeyString = Strings.EMPTY;

	private final StructureDefinition mDefinition;
	private final LevelDefinition mLevel;

	// Data items that can be read or written, but are not part of the structure.
	private final Map<String, DataItem> mExtraMembers = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

	private Entity.OnPropertyValueChangeListener mOnPropertyValueChangeListener;

	private String mSelectionExpression;
	private boolean mIsSelected = false; // Flag used when "selection expression" is not set.

	private final LinkedHashMap<String, EntityList> mLevels = new LinkedHashMap<>();

	private final NameMap<Object> mTags = new NameMap<>();
	private final NameMap<Object> mCacheTags = new NameMap<>();

	EntityValues(StructureDefinition definition, LevelDefinition level)
	{
		if (level == null && definition != null)
			level = definition.Root;

		mDefinition = definition;
		mLevel = level;
	}

	public boolean isEmpty()
	{
		return (getInternalProperties().size() == 0);
	}

	public void setExtraMembers(List<? extends DataItem> members)
	{
		mExtraMembers.clear();
		for (DataItem item : members)
			mExtraMembers.put(item.getName(), item);

		initializeMembers(members);
	}

	public void initialize()
	{
		initializeMembers(mLevel.Items);
	}

	private void initializeMembers(Iterable<? extends DataItem> members)
	{
		for (DataItem di : members)
		{
			if (baseGetProperty(di.getName()) == null)
				baseSetProperty(di.getName(), di.getEmptyValue());
		}
	}

	//TODO: This function should be removed, all levels should be accessible through getProperty
	public EntityList getLevel(String name)
	{
		return mLevels.get(name);
	}

	boolean hasEntityLevel(String name)
	{
		return mLevels.containsKey(name);
	}

	Set<Map.Entry<String,EntityList>> getLevelsEntries()
	{
		return mLevels.entrySet();
	}

	Object baseGetPropertyOrLevel(String name)
	{
		Object value = mLevels.get(name);
		if (value != null)
			return value;
		return baseGetProperty(name);
	}

	public DataItem getPropertyDefinition(String name)
	{
		// Variables and attributes are not distinct here, so ignore the '&'.
		name = DataItemHelper.getNormalizedName(name);

		// First look up in structure.
		DataItem member = mLevel.getAttribute(name);
		if (member != null)
			return member;

		// Then look up in "extra members" (i.e. variables).
		member = mExtraMembers.get(name);
		if (member != null)
			return member;

		// Then look in member without parent (i.e. secondary grid level)
		member = mLevel.getLevel(name);
		if (member != null && ((LevelDefinition)member).getNoParentLevel())
			return member;

		return null;
	}

	/**
	 * Calls super.setProperty(), without trying to parse property name to find components.
	 */
	boolean baseSetProperty(String name, Object value)
	{
		return super.setProperty(name, value);
	}

	/**
	 * Calls super.getProperty(), without trying to parse property name to find components.
	 */
	Object baseGetProperty(String name)
	{
		return super.getProperty(name);
	}

	boolean hasLevel(String name)
	{
		if (mLevel!=null)
		{
			for (int j = 0; j < mLevel.Levels.size(); j++)
			{
				LevelDefinition level = mLevel.Levels.get(j);
				if (name.equalsIgnoreCase(level.getName()))
					return true;
			}
		}
		return false;
	}

	public void putLevel(String levelName, EntityList items)
	{
		mLevels.put(levelName, items);
	}

	/**
	 * Gets the entity keys
	 * @return String with entity keys concatenated
	 * */
	public String getKeyString()
	{
		if (mKeyString == null || mKeyString.length() == 0)
		{
			ArrayList<String> keys = new ArrayList<>();
			if (mLevel != null)
			{
				for(DataItem keyAtt : mLevel.getKeys())
					keys.add((String)getProperty(keyAtt.getName()));

				mKeyString = Services.Strings.join(keys, Strings.COMMA);
			}
		}
		return mKeyString;
	}

	public List<String> getKey()
	{
		ArrayList<String> keys = new ArrayList<>();
		for (DataItem att : mDefinition.Root.getKeys())
			keys.add((String)getProperty(att.getName()));

		return keys;
	}

	public void setKey(List<String> keys)
	{
		List<DataItem> keysList = mDefinition.Root.getKeys();

		for(int i = 0; i < keysList.size(); i++)
		{
			DataItem att = keysList.get(i);
			if (keys.size() > i)
				setProperty(att.getName(), keys.get(i));
		}
	}

	public StructureDefinition getDefinition()
	{
		return mDefinition;
	}

	public LevelDefinition getLevel()
	{
		return mLevel;
	}

	public boolean isSelected()
	{
		if (Services.Strings.hasValue(mSelectionExpression))
			return optBooleanProperty(mSelectionExpression);
		else
			return mIsSelected;
	}

	public void setIsSelected(boolean value)
	{
		if (Services.Strings.hasValue(mSelectionExpression))
			setProperty(mSelectionExpression, value);
		else
			mIsSelected = value;
	}

	public String getSelectionExpression()
	{
		return mSelectionExpression;
	}

	public void setSelectionExpression(String expression)
	{
		mSelectionExpression = expression;
	}

	public void setOnPropertyValueChangeListener(Entity.OnPropertyValueChangeListener listener)
	{
		mOnPropertyValueChangeListener = listener;
	}

	public void triggerOnPropertyValueChange(String name, Object oldValue, Object newValue)
	{
		if (mOnPropertyValueChangeListener != null)
			mOnPropertyValueChangeListener.onPropertyValueChange(name, oldValue, newValue);
	}

	public Object getTag(String key)
	{
		return mTags.get(key);
	}

	public void setTag(String key, Object value)
	{
		mTags.put(key, value);
	}

	public Object getCacheTag(String key)
	{
		return mCacheTags.get(key);
	}

	public void setCacheTag(String key, Object value)
	{
		mCacheTags.put(key, value);
	}

	public void clearCacheTags() {
		mCacheTags.clear();

		for (Object value : getPropertyValues()) {
			if (value instanceof Entity) {
				((Entity)value).clearCacheTags();
			}
			else if (value instanceof Collection<?>) {
				for (Object element : (Collection<?>)value) {
					if (element instanceof Entity)
						((Entity)element).clearCacheTags();
				}
			}
		}
	}
}
