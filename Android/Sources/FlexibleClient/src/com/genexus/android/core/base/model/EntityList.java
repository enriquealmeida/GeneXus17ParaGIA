package com.genexus.android.core.base.model;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.UUID;

import com.artech.base.services.IEntity;
import com.artech.base.services.IEntityList;
import com.genexus.android.core.base.metadata.StructureDefinition;
import com.genexus.android.core.base.metadata.expressions.Expression;
import com.genexus.android.core.base.serialization.INodeCollection;

public class EntityList extends BaseCollection<Entity> implements IEntityList
{
	private static final long serialVersionUID = 1L;
	private HashMap<String, Entity> mEntities = new LinkedHashMap<>();
	private UUID mVersion;
	private StructureDefinition mDefinition;
	private Expression.Type mItemType = Expression.Type.UNKNOWN;

	public EntityList()
	{
		super();
		updateVersion();
	}

	public EntityList(StructureDefinition definition)
	{
		this();
		mDefinition = definition;
	}

	public EntityList(@NonNull EntityList other)
	{
		this();
		if (other.size() > 0 && other.get(0).getParentInfo().getParentCollection() != null) {
			EntityParentInfo parentInfo = EntityParentInfo.collectionMemberOf(other.get(0).getParentInfo().getParent(), other.get(0).getParentInfo().getMemberName(), this);
			copyFromEntityList(other, parentInfo);
		} else {
			copyFromEntityList(other, null);
		}
	}

	// Safe way to change the parent info
	public EntityList(@NonNull EntityList collection, @NonNull EntityParentInfo parentInfo)
	{
		this();
		if (parentInfo.getParentCollection() != null)
			parentInfo = EntityParentInfo.collectionMemberOf(parentInfo.getParent(), parentInfo.getMemberName(), this);
		copyFromEntityList(collection, parentInfo);
	}

	// Safe way to change the parent info
	public EntityList(@NonNull Entity parent, @NonNull String collectionName, @NonNull EntityList collection)
	{
		this();
		EntityParentInfo parentInfo = EntityParentInfo.collectionMemberOf(parent, collectionName, this);
		copyFromEntityList(collection, parentInfo);
	}

	public EntityList(Iterable<Entity> other, StructureDefinition definition)
	{
		this();
		for (Entity entity : other)
			addEntity(entity);

		mDefinition = definition;
	}

	private void copyFromEntityList(@NonNull EntityList other, EntityParentInfo parentInfo)
	{
		for (int i = 0; i < other.size(); i++) {
			addEntity(other.get(i));
			if (parentInfo != null)
				other.get(i).addParentInfo(parentInfo);
		}

		mVersion = other.mVersion;
		mDefinition = other.mDefinition;
		mItemType = other.mItemType;
	}

	@Override
	public Expression.Type getItemType()
	{
		return mItemType;
	}

	public void setItemType(Expression.Type itemType)
	{
		mItemType = itemType;
	}

	public UUID getVersion() { return mVersion; }

	public StructureDefinition getDefinition()
	{
		return mDefinition;
	}

	@Override
	public void add(IEntity entity)
	{
		addEntity((Entity)entity);
	}

	public void addEntity(Entity entity)
	{
		String key = entity.getKeyString();

		if (key != null && key.length() > 0)
		{
			if (!mEntities.containsKey(key))
			{
				mEntities.put(key, entity);
				add(entity);
			}
		}
		else
		{
			key = String.valueOf(mEntities.size() + 1);
			mEntities.put(key, entity);
			add(entity);
		}

		updateVersion();
	}

	@Override
	protected void serializeItem(INodeCollection collection, Entity item, short jsonFormat) {
		collection.put(item.serialize(jsonFormat));
	}

	@Override
	protected Entity deserializeItem(INodeCollection collection, int index, short jsonFormat) {
		Entity item = EntityFactory.newEntity(mDefinition);
		item.deserialize(collection.getNode(index), jsonFormat);
		return item;
	}

	// WARNING: Only use this method when you know the old entities will not be used
	public void setParentInfo(@NonNull EntityParentInfo parentInfo) {
		if (parentInfo.getParentCollection() != null && !parentInfo.getParentCollection().equals(this))
			throw new IllegalArgumentException("Incorrect EntityList parent collection");

		for (Entity entity : this)
			entity.addParentInfo(parentInfo);
	}

	private void updateVersion() {
		mVersion = UUID.randomUUID();
	}
}
