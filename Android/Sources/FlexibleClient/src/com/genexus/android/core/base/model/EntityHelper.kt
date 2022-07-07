package com.genexus.android.core.base.model

import com.genexus.android.core.base.services.Services
import java.util.Locale

object EntityHelper {
    /**
     * Gets the entity from which a code expression should be evaluated (e.g. the same entity
     * when it's provided by a DP, or the root entity when it's part of an SDT structure).
     */
    @JvmStatic
    fun forEvaluationIndex(paramEntity: Entity, childIndexes: ArrayList<Int>, childPath: String): Entity {
        // Get the "TRUE" root entity for executing the action.
        // For a normal Entity (e.g. the Form entity, or an entity in a grid row with a DP) it's the same one.
        // For a "member" entity (e.g. an SDT variable or an SDT collection item) it's the first parent entity
        // that is not a member itself (i.e. one of the "normal" cases outlined above).
        var entity: Entity = paramEntity
        fun normalize(s: String) = ".${s.replace("&","")}.".lowercase(Locale.ROOT)
        val childPathDot = normalize(childPath)
        while (entity.allParentInfo.any { it.isMember }) {
            val parentInfo = entity.allParentInfo.firstOrNull {
                (it.parentCollection?.contains(entity) ?: false) &&
                    childPathDot.contains(normalize(it.memberName))
            } // memberName is part of the path

            val parentEntity: Entity?
            if (parentInfo != null) {
                childIndexes.add(0, parentInfo.parentCollection.indexOf(entity))
                parentEntity = parentInfo.parent
            } else {
                // it can be a member not collection
                parentEntity = entity.allParentInfo.firstOrNull {
                    it.isMember && it.parentCollection == null &&
                        childPathDot.contains(normalize(it.memberName))
                }?.parent
            }
            entity = parentEntity ?: break // if parentEntity == null then entity is the root
        }

        val index = childPath.indexOf(".")
        val firstMember = (if (index < 0) childPath else childPath.substring(0, index)).lowercase(Locale.ROOT)
        if (!entity.propertyNames.any { firstMember == it.lowercase(Locale.ROOT) }) {
            Services.Log.error("ParentInfo not root for $childPath")
        }

        return entity
    }

    /**
     * Set the current item in the collections
     * @return The same as forEvaluationIndex
     */
    @JvmStatic
    fun forEvaluationCurrent(entity: Entity): Entity? {
        if (!entity.allParentInfo.any { it.isMember }) {
            if (entity.parentInfo.parent != null)
                Services.Log.error("ParentInfo not root")
            return entity
        }

        var firstRoot: Entity? = null
        for (parentInfo in entity.allParentInfo.filter { it.parentCollection?.contains(entity) ?: false }) {
            parentInfo.parentCollection.currentItem = entity

            if (parentInfo.parent != null) {
                // wa for sdt set current in local event
                // should set current item , also to this entity, if is entity list of sdt.? sdt grid, vs sdt form...
                for (enValue in parentInfo.parent.propertyValues) {
                    if (enValue is EntityList) {
                        val list = enValue

                        // set current for collection of same type
                        if (list.definition != null && list.definition == entity.definition)
                            list.currentItem = entity

                        // if not have type, set current for collection of items of same type
                        if (list.definition == null && list.size > 0) {
                            val sampleEntity = list[0]
                            if (sampleEntity.definition == entity.definition)
                                list.currentItem = entity
                        }
                    }
                }

                val root = forEvaluationCurrent(parentInfo.parent)
                if (firstRoot == null)
                    firstRoot = root // we arrive from every parent to the same root, just get the first one
            }
        }

        return firstRoot
    }
}
