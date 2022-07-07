package com.genexus.android.core.base.model;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Objects;

public class EntityParentInfo implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private final Entity mParentEntity;
	private final EntityList mParentCollection;
	private final String mMemberName;

	private EntityParentInfo(Entity parentEntity, EntityList parentCollection, String memberName)
	{
		mParentEntity = parentEntity;
		mParentCollection = parentCollection;
		mMemberName = memberName;
	}

	public static final EntityParentInfo NONE = new EntityParentInfo(null, null, null);

	public static @NonNull EntityParentInfo memberOf(Entity parent, String name)
	{
		return new EntityParentInfo(parent, null, name);
	}

	public static @NonNull EntityParentInfo collectionMemberOf(Entity parent, String collectionName, EntityList collection)
	{
		return new EntityParentInfo(parent, collection, collectionName);
	}

	public static @NonNull EntityParentInfo subordinatedProviderOf(Entity rootData)
	{
		return new EntityParentInfo(rootData, null, null);
	}

	public Entity getParent() { return mParentEntity; }
	public EntityList getParentCollection() { return mParentCollection; }
	public boolean isMember() { return mMemberName != null; }
	public String getMemberName() { return mMemberName; }

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof EntityParentInfo))
			return false;
		EntityParentInfo that = (EntityParentInfo) o;
		return Objects.equals(mParentEntity, that.mParentEntity) &&
			Objects.equals(mParentCollection, that.mParentCollection) &&
			Objects.equals(mMemberName, that.mMemberName);
	}

	@Override
	public int hashCode() {
		return Objects.hash(mParentEntity, mParentCollection, mMemberName);
	}
}
