package com.genexus.android.core.base.model

import com.artech.base.services.IEntity
import com.genexus.android.core.base.metadata.DataItem
import com.genexus.android.core.base.metadata.LevelDefinition
import com.genexus.android.core.base.metadata.StructureDefinition
import com.genexus.android.core.base.metadata.VariableDefinition
import com.genexus.android.core.base.serialization.INodeObject

interface Entity : IEntity {
    val definition: StructureDefinition
    val keyString: String
    var key: MutableList<String>
    val level: LevelDefinition
    val isSelected: Boolean
    var selectionExpression: String
    val isEmpty: Boolean
    val propertyNames: Iterable<String>
    val propertyValues: Iterable<Any?>
    val parentInfo: EntityParentInfo // returns the last parent
    val allParentInfo: Iterable<EntityParentInfo>

    fun addParentInfo(parentInfo: EntityParentInfo)
    fun toDebugString(): String

    fun setIsSelected(value: Boolean)
    fun getPropertyDefinition(name: String): DataItem?
    fun getLevel(name: String): EntityList?
    fun putLevel(name: String, level: EntityList)
    fun setExtraMembers(members: MutableList<VariableDefinition>)

    /**
     * Initializes all members of the entity with their "empty" values (e.g. 0 for numbers,
     * empty string for characters, "default" Entity for inner structures).
     */
    fun initialize()

    fun serialize(jsonFormat: Short): INodeObject
    fun deserialize(node: INodeObject)
    fun deserialize(node: INodeObject, jsonFormat: Short)
    fun deserializeValue(name: String, value: Any): Boolean
    fun fromString(json: String)
    fun setOnPropertyValueChangeListener(listener: OnPropertyValueChangeListener?)
    fun movePropertiesFrom(other: Entity, itemsToCopy: Iterable<DataItem>)
    fun isDirtyBySet(name: String): Boolean
    fun isDirtyByChange(name: String): Boolean
    fun resetDirties()

    fun getTag(name: String): Any?
    fun setTag(name: String, value: Any)
    fun getCacheTag(name: String): Any?
    fun setCacheTag(name: String, value: Any)
    fun clearCacheTags()

    fun optIntProperty(name: String?): Int
    fun optIntProperty(name: String?, defaultValue: Int): Int
    fun optLongProperty(name: String?): Long
    fun optLongProperty(name: String?, defaultValue: Long): Long
    fun optBooleanProperty(name: String?): Boolean
    fun optBooleanProperty(name: String?, defaultValue: Boolean): Boolean
    fun <T> getProperty(type: Class<T>, name: String?): T

    interface OnPropertyValueChangeListener {
        fun onPropertyValueChange(propertyName: String, oldValue: Any?, newValue: Any?)
    }

    companion object {
        const val OPERATION_INSERT = 1
        const val OPERATION_UPDATE = 2
        const val OPERATION_DELETE = 3
        const val OPERATION_INSERT_UPDATE = 4

        const val JSONFORMAT_AUTO: Short = 1
        const val JSONFORMAT_INTERNAL: Short = 2
        const val JSONFORMAT_EXTERNAL: Short = 3
        const val JSONFORMAT_INTERNAL_SET: Short = 4 // only save values that have been set
    }
}
