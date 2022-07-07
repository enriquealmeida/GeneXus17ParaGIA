package com.genexus.android.core.base.model;

import androidx.annotation.NonNull;

import com.genexus.android.core.base.metadata.DataItem;
import com.genexus.android.core.base.metadata.LevelDefinition;
import com.genexus.android.core.base.metadata.StructureDataType;
import com.genexus.android.core.base.metadata.StructureDefinition;
import com.genexus.android.core.base.serialization.INodeCollection;
import com.genexus.android.core.base.serialization.INodeObject;
import com.genexus.android.core.base.services.Services;

import java.util.List;

/**
 * Entity factory. Creates specialized kinds of entities.
 */
public class EntityFactory
{
	/**
	 * Creates a new SDT Entity, from its data type name.
	 * @param sdtTypeName SDT name (GX name).
	 * @throws IllegalArgumentException When the SDT definition is not found in the application's metadata.
	 */
	public static @NonNull Entity newSdt(@NonNull String sdtTypeName)
	{
		StructureDataType sdtDefinition = getSdtDefinition(sdtTypeName);
		Entity entity = newEntity(sdtDefinition.getStructure());
		entity.initialize(); // create inner members empty values
		return entity;
	}

	/**
	 * Creates a new BC Entity, from its data type name.
	 * @param bcTypeName BC name (GX name).
	 * @throws IllegalArgumentException When the BC definition is not found in the application's metadata.
	 */
	public static @NonNull Entity newBC(@NonNull String bcTypeName)
	{
		StructureDefinition bcDefinition = Services.Application.getDefinition().getBusinessComponent(bcTypeName);
		if (bcDefinition == null)
			throw new IllegalArgumentException(String.format("BC definition for '%s' is missing.", bcTypeName));
		Entity entity = newEntity(bcDefinition);
		entity.initialize(); // create inner members empty values
		return entity;
	}

	/**
	 * Creates a new SDT inner level Entity, from its data type name.
	 * @param sdtTypeName SDT name (GX name).
	 * @param levelName Level name (GX name).
	 * @throws IllegalArgumentException When the SDT definition or its level is not found in the application's metadata.
	 */
	public static @NonNull Entity newSdt(@NonNull String sdtTypeName, @NonNull String levelName)
	{
		// TODO: When getLevel() is removed, this should be merged with the other overload and
		// TODO: receive the "full" data type name, i.e. sdtTypeName.levelTypeName.
		// TODO: Note that levelTypeName should NOT be the same as levelName!
		StructureDataType sdtDefinition = getSdtDefinition(sdtTypeName);
		LevelDefinition levelDefinition = sdtDefinition.getLevel(levelName);
		if (levelDefinition == null)
			throw new IllegalArgumentException(String.format("SDT '%s' does not contain a level with name '%s'.", sdtTypeName, levelName));

		return newEntity(sdtDefinition.getStructure(), levelDefinition, EntityParentInfo.NONE);
	}

	/**
	 * Creates a new SDT Collection from its data type name and the given values.
	 * @param sdtTypeName SDT name (GX name).
	 * @param values the serialized values to put in each item of the SDT.
	 * @throws IllegalArgumentException if the SDT definition doesn't have an item inside.
	 */
	public static @NonNull INodeCollection newSdtCollection(@NonNull String sdtTypeName, @NonNull List<String> values) {
		StructureDataType sdtDefinition = getSdtDefinition(sdtTypeName);

		List<DataItem> items = sdtDefinition.getRoot().Items;
		if (items.isEmpty()) {
			throw new IllegalArgumentException(String.format("SDT '%s' does not contain an item inside.", sdtTypeName));
		}

		DataItem item = sdtDefinition.getRoot().Items.get(0);

		INodeCollection collection = Services.Serializer.createCollection();

		for (String value : values) {
			INodeObject node = Services.Serializer.createNode();
			node.put(item.getName(), value);
			collection.put(node);
		}

		return collection;
	}

	private static @NonNull StructureDataType getSdtDefinition(@NonNull String sdtTypeName)
	{
		StructureDataType sdtDefinition = Services.Application.get().getDefinition().getSDT(sdtTypeName);
		if (sdtDefinition == null)
			throw new IllegalArgumentException(String.format("SDT definition for '%s' is missing.", sdtTypeName));

		return sdtDefinition;
	}

	public static @NonNull Entity newEntity() {
		return newEntity(StructureDefinition.EMPTY);
	}

	public static @NonNull Entity newEntity(@NonNull StructureDefinition definition) {
		return newEntity(definition, definition.Root, EntityParentInfo.NONE);
	}

	public static @NonNull Entity newEntity(@NonNull StructureDefinition definition, @NonNull LevelDefinition level) {
		return newEntity(definition, level, EntityParentInfo.NONE);
	}

	public static @NonNull Entity newEntity(@NonNull StructureDefinition definition, @NonNull EntityParentInfo parentInfo) {
		return newEntity(definition, definition.Root, parentInfo);
	}

	public static @NonNull Entity newEntity(@NonNull StructureDefinition definition, @NonNull LevelDefinition level, @NonNull EntityParentInfo parentInfo) {
		EntityValues values = new EntityValues(definition, level);
		values.initialize();
		return new EntityBase(values, parentInfo);
	}
}
