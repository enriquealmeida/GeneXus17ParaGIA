package com.genexus.android.core.base.model

import android.util.Pair
import com.genexus.android.core.actions.Action
import com.genexus.android.core.base.metadata.DataItem
import com.genexus.android.core.base.metadata.DataItemHelper
import com.genexus.android.core.base.metadata.LevelDefinition
import com.genexus.android.core.base.metadata.StructureDefinition
import com.genexus.android.core.base.metadata.VariableDefinition
import com.genexus.android.core.base.metadata.expressions.Expression
import com.genexus.android.core.base.serialization.INodeObject
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.base.utils.Strings
import com.genexus.android.core.utils.Cast
import org.simpleframework.xml.Transient
import java.util.Arrays
import kotlin.collections.ArrayList

/**
 * EntityBase implements Entity interface, leaving the values in EntityValues and handling the parentInfo separately
 */
private class EntityBase(val entityValues: EntityValues, initialParentInfo: EntityParentInfo) : Entity {
    @Transient
    private val serializer = EntitySerializer(this)
    private val parentInfoList = mutableListOf(initialParentInfo)

    override fun addParentInfo(parentInfo: EntityParentInfo) {
        parentInfoList.remove(parentInfo) // if exist move it to the head
        parentInfoList.add(0, parentInfo)
    }

    override fun toDebugString(): String {
        return "[Id=${System.identityHashCode(this)}, Data=$this]"
    }

    override val definition: StructureDefinition get() = entityValues.definition
    override val keyString: String get() = entityValues.keyString
    override var key: MutableList<String> get() = entityValues.key; set(value) { entityValues.key = value }
    override val level: LevelDefinition get() = entityValues.level
    override val isSelected: Boolean get() = entityValues.isSelected
    override var selectionExpression: String get() = entityValues.selectionExpression; set(value) { entityValues.selectionExpression = value }
    override val isEmpty: Boolean get() = entityValues.isEmpty
    override val propertyNames: Iterable<String> get() = entityValues.propertyNames
    override val propertyValues: Iterable<Any?> get() = entityValues.propertyValues
    override val parentInfo: EntityParentInfo get() = parentInfoList.first()
    override val allParentInfo: Iterable<EntityParentInfo> get() = parentInfoList

    override fun setIsSelected(value: Boolean) = entityValues.setIsSelected(value)
    override fun getPropertyDefinition(name: String): DataItem? = entityValues.getPropertyDefinition(name)
    override fun getLevel(name: String): EntityList? = entityValues.getLevel(name)
    override fun putLevel(name: String, level: EntityList) = entityValues.putLevel(name, level)
    override fun setExtraMembers(members: MutableList<VariableDefinition>) = entityValues.setExtraMembers(members)
    override fun initialize() = entityValues.initialize()
    override fun fromString(json: String) = deserialize(Services.Serializer.createNode(json), Entity.JSONFORMAT_INTERNAL)
    override fun setOnPropertyValueChangeListener(listener: Entity.OnPropertyValueChangeListener?) = entityValues.setOnPropertyValueChangeListener(listener)
    override fun isDirtyBySet(name: String): Boolean = entityValues.isDirtyBySet(name)
    override fun isDirtyByChange(name: String): Boolean = entityValues.isDirtyByChange(name)
    override fun resetDirties() = entityValues.resetDirties()
    override fun getTag(name: String): Any? = entityValues.getTag(name)
    override fun setTag(name: String, value: Any) = entityValues.setTag(name, value)
    override fun getCacheTag(name: String): Any? = entityValues.getCacheTag(name)
    override fun setCacheTag(name: String, value: Any) = entityValues.setCacheTag(name, value)
    override fun clearCacheTags() = entityValues.clearCacheTags()

    override fun optIntProperty(name: String?, defaultValue: Int): Int {
        val s = getProperty(name) as String? ?: return defaultValue
        return try {
            s.toInt()
        } catch (_: NumberFormatException) {
            defaultValue
        }
    }
    override fun optLongProperty(name: String?, defaultValue: Long): Long {
        val str = getProperty(name) as String? ?: return defaultValue
        return try {
            str.toLong()
        } catch (_: NumberFormatException) {
            defaultValue
        }
    }
    override fun optBooleanProperty(name: String?, defaultValue: Boolean): Boolean {
        val value = getProperty(name)
        return if (value is Boolean)
            value
        else if (value is String)
            Services.Strings.tryParseBoolean(value as String?, false)
        else
            false
    }
    override fun optIntProperty(name: String?): Int = optIntProperty(name, 0)
    override fun optLongProperty(name: String?): Long = optLongProperty(name, 0)
    override fun optBooleanProperty(name: String?): Boolean = optBooleanProperty(name, false)
    override fun optStringProperty(name: String?): String = getProperty(name)?.toString() ?: Strings.EMPTY
    override fun <T> getProperty(type: Class<T>, name: String?): T = Cast.`as`(type, getProperty(name))

    private fun isSpecialProperty(name: String?): Boolean {
        return name != null &&
            (
                name.equals("gx_md5_hash", ignoreCase = true) ||
                    name.equals("gxdynprop", ignoreCase = true) ||
                    name.equals("gxdyncall", ignoreCase = true) ||
                    Strings.starsWithIgnoreCase(name, "Gxprops_")
                )
    }

    override fun setProperty(name: String, value: Any?): Boolean {
        if (isSpecialProperty(name))
            return entityValues.baseSetProperty(name, value)

        val propertyContainer = resolvePropertyContainer(name, true)
        if (propertyContainer != null) {
            val innerEntity = propertyContainer.first
            val innerName = propertyContainer.second
            return if (value is Iterable<*> && entityValues.level.getLevel(innerName) != null) { // Dataproviders offline use setProperty to set the levels, capture that case here
                // TODO: dont have support for entity list with a definition of a sub level entity.
                @Suppress("UNCHECKED_CAST") val level = EntityList(value as Iterable<Entity>, null)
                level.itemType = Expression.Type.SDT
                putLevel(innerName, level)
                true
            } else { // Perform conversion if necessary.
                val newValue = innerEntity.serializer.deserializeValue(innerName, value) ?: value
                val oldValue = innerEntity.entityValues.baseGetProperty(innerName)
                val set: Boolean = innerEntity.entityValues.baseSetProperty(innerName, newValue)
                // Fire the "property value change" event if applicable.
                if (set) {
                    if (oldValue == null || oldValue != newValue) {
                        entityValues.triggerOnPropertyValueChange(name, oldValue, newValue)
                        innerEntity.entityValues.setDirtyByChange(innerName) // dirty needed for BC media upload
                    }
                    innerEntity.entityValues.setDirtyBySet(innerName) // dirty needed for Null Serialization
                }
                set
            }
        }

        // TODO: Remove this line ASAP. It's only for the chart control.
        entityValues.baseSetProperty(name, value)

        if (!Action.isPredefinedErrorVariable(name))
            Services.Log.warning(String.format("Entity.setProperty(%s, %s) failed.", name, value))

        return false
    }

    override fun getProperty(name: String?): Any? {
        if (name == null)
            return null

        if (isSpecialProperty(name))
            return entityValues.baseGetProperty(name)

        val propertyContainer = resolvePropertyContainer(name, true)
        if (propertyContainer != null) {
            val value = propertyContainer.first.entityValues.baseGetPropertyOrLevel(propertyContainer.second)
            if (value != null)
                return value
        }

        // TODO: Remove these lines ASAP. It's only for the chart control.
        val valueHere = entityValues.baseGetProperty(name)
        if (valueHere != null)
            return valueHere

        for (level in entityValues.level.Levels) {
            if (level.name.equals(name, ignoreCase = true) && !level.isCollection) {
                val entity = EntityFactory.newEntity(entityValues.definition, level, EntityParentInfo.memberOf(this, level.name))
                setProperty(level.name, entity)
                return entity
            }
        }

        Services.Log.warning(String.format("Entity.getProperty(%s) failed.", name))
        return null
    }

    /**
     * Given an entity and a (possibly complex) property name, return the entity
     * that "really" contains the property. May return the same entity and property name,
     * or a subordinated entity (e.g. an SDT member) and the property name inside it.
     */
    private fun resolvePropertyContainer(notNormalizedPropertyName: String, allowGoUp: Boolean): Pair<EntityBase, String>? { // Variables and attributes are not distinct here, so ignore the '&'.
        val propertyName = DataItemHelper.getNormalizedName(notNormalizedPropertyName)

        // Consider properties of the type "&var.field".
        val propertyPath = Services.Strings.split(propertyName, '.')

        // First find if the property (actually its most immediate member) actually belongs to this entity.
        val rootProperty = propertyPath[0]
        val propDefinition = getPropertyDefinition(rootProperty)
        val sublevel = entityValues.level.getLevel(rootProperty)
        if (propDefinition == null && sublevel == null) {
            // NOT here. So we cannot assign in this entity, or go further below if it's a composite property.
            // It MAY, however, be a parent property (e.g. assigning a form variable from a grid row context).
            // This is DISABLED if we already went down a level, because it makes no sense to look for
            // a partial expression in the parent context.
            if (allowGoUp) {
                for (parentInfo in parentInfoList) {
                    val parent = parentInfo.parent
                    if (parent is EntityBase) {
                        val pair = parent.resolvePropertyContainer(propertyName, true)
                        if (pair != null)
                            return pair
                    }
                }
            }
            return null // Failure. Property unknown here, and we don't have a parent to check.
        }
        return if (propertyPath.size == 1) { // Simple property, the current entity is the correct one.
            Pair(this, propertyName)
        } else { // Compound property. Find the entity of the first level and go recursively down.
            val component = entityValues.baseGetPropertyOrLevel(propertyPath[0])
            if (component != null) { // Build the *remaining* path into an ArrayList, to ease manipulation.
                val rest = ArrayList<String>()
                rest.addAll(Arrays.asList(*propertyPath).subList(1, propertyPath.size))
                if (component is EntityBase) { // A single item. Go recursively.
                    component.resolvePropertyContainer(Services.Strings.join(rest, Strings.DOT), false)
                } else if (component is EntityList) { // A collection item. Go recursively.
                    val list = component
                    // The following term in the expression MUST be a collection item selector, either
                    // an explicit indexer (item(<n>)) or the enumeration position (CurrentItem).
                    val itemSelector = rest[0]
                    rest.removeAt(0)
                    var item: Entity? = null
                    if (itemSelector.equals(BaseCollection.PROPERTY_CURRENT_ITEM, ignoreCase = true)) {
                        item = list.currentItem
                    } else if (Strings.toLowerCase(itemSelector).contains(Strings.toLowerCase(BaseCollection.METHOD_GET_ITEM))) {
                        val itemIndexStr = Strings.toLowerCase(itemSelector).replace(Strings.toLowerCase(BaseCollection.METHOD_GET_ITEM), Strings.EMPTY)
                            .replace("(", Strings.EMPTY).replace(")", Strings.EMPTY)
                        val itemIndex = Services.Strings.tryParseInt(itemIndexStr, 0) - 1 // GX is 1-based.
                        if (itemIndex >= 0 && itemIndex < list.size)
                            item = list[itemIndex]
                    }
                    if (item is EntityBase) {
                        item.resolvePropertyContainer(Services.Strings.join(rest, Strings.DOT), false)
                    } else {
                        Services.Log.warning("Could not get collection item with selector '$itemSelector'.")
                        null
                    }
                } else
                    null // Unknown component found.
            } else
                null // Component not found.
        }
    }

    override fun movePropertiesFrom(other: Entity, itemsToCopy: Iterable<DataItem>) {
        if (other is EntityBase) {
            for (item in itemsToCopy) {
                var itemValue = other.entityValues.baseGetProperty(item.name)
                if (itemValue != null) {
                    if (itemValue is EntityList && itemValue.size > 0) {
                        val memberName = itemValue.get(0).allParentInfo.firstOrNull { it.parent == other }?.memberName
                        itemValue = EntityList(this, memberName ?: "", itemValue)
                    }
                    entityValues.baseSetProperty(item.name, itemValue)
                }
            }
        } else {
            throw IllegalArgumentException("Expected EntityBase")
        }
    }

    // auto and internal only use only internal name for now (use internal for clarify), external only use external name
    override fun serialize(jsonFormat: Short): INodeObject {
        val obj = Services.Serializer.createNode()

        // serialize only first level
        for (key in entityValues.propertyNames) {
            val itemDefinition: DataItem? = entityValues.level.getAttribute(key)
            val sublevel: LevelDefinition? = entityValues.level.getLevel(key)

            // ignore att that doesn't exist in the structure definition.
            if (!key.equals("gx_md5_hash", ignoreCase = true) && itemDefinition == null && (sublevel == null || sublevel.isCollection)) {
                if (getPropertyDefinition(key) == null && !entityValues.hasEntityLevel(key)) // Only warn if it's not a variable or level.
                    Services.Log.warning("entitySerialize", "$key is not present in the structure")
                continue
            }

            if (entityValues.hasEntityLevel(key))
                continue

            val serializedKey = if (jsonFormat == Entity.JSONFORMAT_EXTERNAL && itemDefinition != null)
                itemDefinition.externalName
            else
                key

            val value = getProperty(key)

            // in external format, do not write empty collections or not set values with JsonSerialize NoProperty
            if (jsonFormat == Entity.JSONFORMAT_EXTERNAL && itemDefinition != null) {
                val jsonNoProperty = if (itemDefinition.jsonNullSerialization == null)
                    itemDefinition.isCollection // compatibility default
                else
                    itemDefinition.jsonNullSerialization.equals("idJsonNoProperty", ignoreCase = true)
                if (jsonNoProperty) {
                    if (itemDefinition.isCollection) {
                        if (value is BaseCollection<*> && value.isEmpty())
                            continue
                    } else if (!isDirtyBySet(key)) {
                        continue
                    }
                }
            }

            // in internal set format, only save values that have been set
            if (jsonFormat == Entity.JSONFORMAT_INTERNAL_SET && !isDirtyBySet(key))
                continue

            if (value is Entity) {
                val innerNode = value.serialize(jsonFormat)
                obj.put(serializedKey, innerNode)
            } else if (value is BaseCollection<*>) {
                val innerCollection = value.serialize(jsonFormat)
                obj.put(serializedKey, innerCollection)
            } else {
                obj.put(serializedKey, value)
            }
        }

        // Serialize sub levels
        for ((key1, value) in entityValues.levelsEntries) {
            if (value.size > 0) {
                val arrayLines = Services.Serializer.createCollection()
                for (entity in value) {
                    val objLine = entity.serialize(jsonFormat)
                    arrayLines.put(objLine)
                }
                obj.put(key1, arrayLines)
            }
        }

        // in external format, write empty levels with JsonSerialize Empty
        if (jsonFormat == Entity.JSONFORMAT_EXTERNAL) {
            for (levelDefinition in entityValues.level.Levels) {
                val level: EntityList? = entityValues.getLevel(levelDefinition.name)
                if (levelDefinition.jsonNullSerialization.equals("idJsonEmpty", ignoreCase = true) &&
                    (level == null || level.size == 0)
                ) {
                    val arrayLines = Services.Serializer.createCollection()
                    obj.put(levelDefinition.name, arrayLines)
                }
            }
        }

        return obj
    }

    override fun deserialize(node: INodeObject) {
        deserialize(node, Entity.JSONFORMAT_AUTO)
    }

    // auto try both format, internal only use only internal name, external only external name
    override fun deserialize(node: INodeObject, jsonFormat: Short) {
        // Deserialize first level, including complex variables, but not levels.
        for (jsonName in node.names()) {
            if (entityValues.hasLevel(jsonName))
                continue

            var di: DataItem? = null
            entityValues.definition?.let {
                if (jsonFormat == Entity.JSONFORMAT_AUTO) {
                    di = it.getAttribute(jsonName) ?: it.getAttributeByExternalName(jsonName)
                } else if (jsonFormat == Entity.JSONFORMAT_INTERNAL) {
                    di = it.getAttribute(jsonName)
                } else if (jsonFormat == Entity.JSONFORMAT_EXTERNAL) {
                    di = it.getAttributeByExternalName(jsonName)
                }
            }
            val diX = di
            if (diX != null)
                deserializeValue(diX.name, node[jsonName])
            else // Get extra values for example gx_md5_hash when changing a BC in WWD.  Before it was ignored everything for those without definition, usefull for BC that have extra fields, but now those extra numeric fields are read as string.
                setProperty(jsonName, node.getString(jsonName))
        }

        var hasNoParentLevel = false
        for (j in entityValues.level.Levels.indices) {
            val level: LevelDefinition = entityValues.level.Levels.get(j)
            if (level.isCollection) {
                val entities = node.optCollection(level.name)
                val items = EntityList().apply {
                    itemType = Expression.Type.SDT
                }
                entities.forEach { entityJson ->
                    val parentInfo = if (level.noParentLevel) {
                        hasNoParentLevel = true
                        EntityParentInfo.NONE
                    } else {
                        EntityParentInfo.collectionMemberOf(this, level.name, items)
                    }
                    val entity = EntityFactory.newEntity(entityValues.definition, level, parentInfo)
                    entity.deserialize(entityJson, jsonFormat)
                    items.addEntity(entity)
                }
                putLevel(level.name, items)
            } else {
                val childNode = node.getNode(level.name)
                if (childNode != null) {
                    val entity = EntityFactory.newEntity(entityValues.definition, level, EntityParentInfo.memberOf(this, level.name))
                    entity.deserialize(childNode, jsonFormat)
                    setProperty(level.name, entity)
                }
            }
        }
        if (!hasNoParentLevel && entityValues.level.Levels.size > 0) {
            Services.Log.error("EntityDeserialize", "Found level in only header case")
        }
    }

    override fun deserializeValue(name: String, value: Any): Boolean {
        val propertyValue = serializer.deserializeValue(name, value)
        return if (propertyValue != null) {
            // We already know that this is the proper entity and value has been converted, so just assign it.
            val oldValue = entityValues.baseGetProperty(name)
            val set = entityValues.baseSetProperty(name, propertyValue)
            if (set) {
                if (oldValue == null || oldValue != propertyValue)
                    entityValues.setDirtyByChange(name) // dirty needed for BC media upload
                entityValues.setDirtyBySet(name) // dirty needed for Null Serialization
            }
            true
        } else
            false
    }

    override fun toString(): String {
        return serialize(Entity.JSONFORMAT_INTERNAL).toString()
    }
}
