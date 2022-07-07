package com.genexus.android.core.base.metadata.loader

import android.content.Context
import com.genexus.android.core.base.metadata.DataItem
import com.genexus.android.core.base.metadata.IPatternMetadata
import com.genexus.android.core.base.metadata.LevelDefinition
import com.genexus.android.core.base.metadata.RelationDefinition
import com.genexus.android.core.base.metadata.StructureDefinition
import com.genexus.android.core.base.serialization.INodeObject
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.base.utils.Strings
import java.lang.Exception

class EntityDefinitionLoader : MetadataLoader() {
    override fun load(context: Context, data: String): IPatternMetadata? {
        val dataLower = Strings.toLowerCase(data)

        // Try to read with both name formats, new and old.
        var json = getDefinition(context, "$dataLower.bc")
        if (json == null)
            json = getDefinition(context, dataLower)

        return if (json != null) loadJSON(json) else null
    }

    companion object {
        private fun loadJSON(jsonData: INodeObject): StructureDefinition {
            val gxObject = jsonData.getNode("GxObject") ?: throw Exception("missing GxObject")
            val structure = gxObject.getNode("Structure") ?: throw Exception("missing Structure")
            val relations = gxObject.getNode("Relations") ?: throw Exception("missing Relations")
            val definition = StructureDefinition(gxObject.getString("Name"))
            definition.connectivitySupport = MetadataParser.readConnectivity(gxObject)
            loadLevels(definition, structure)
            loadRelations(definition, relations)
            return definition
        }

        private fun loadRelations(definition: StructureDefinition, relations: INodeObject) {
            val manyToOneReader = relations.getNode("ManyToOne") ?: return
            val referencesReader = manyToOneReader.optCollection("references")
            for (i in 0 until referencesReader.length()) {
                val obj = referencesReader.getNode(i) ?: continue
                val rel = readRelation(obj)
                if (rel.bcRelated == null || rel.bcRelated != definition.name)
                    definition.ManyToOneRelations.add(rel)
            }
        }

        private fun readRelation(obj: INodeObject): RelationDefinition {
            val relation = RelationDefinition()
            relation.name = obj.getString("Name")
            relation.bcRelated = obj.getString("BusinessComponent")

            val foreignKey = obj.getNode("ForeignKey") ?: return relation
            val keyAtts = foreignKey.optCollection("KeyAttributes")
            val inferredAtts = obj.getNode("InferredAttributes")
            if (inferredAtts != null) {
                val atts = inferredAtts.optCollection("Attributes")
                for (i in 0 until atts.length()) {
                    val attRefJson = atts.getNode(i) ?: continue
                    val attName = attRefJson.optString("Name")
                    val att = Services.Application.definition.getAttribute(attName)
                    if (att != null) { // _GXI are being inferred so we have to ignore this here
                        att.setProperty("AtributeSuperType", attRefJson.optString("AtributeSuperType"))
                        relation.inferredAtts.add(attName)
                    }
                }
            }
            for (i in 0 until keyAtts.length()) {
                val attRefJson = keyAtts.getNode(i) ?: continue
                val attName = attRefJson.optString("Name")
                val att = Services.Application.definition.getAttribute(attName)
                if (att != null) {
                    att.setProperty("AtributeSuperType", attRefJson.optString("AtributeSuperType"))
                    relation.keys.add(attName)
                }
            }
            return relation
        }

        private fun loadAttributes(definition: LevelDefinition, structure: INodeObject) {
            val attributes = structure.optCollection("Attributes")
            for (i in 0 until attributes.length()) {
                val attribute = attributes.getNode(i) ?: continue
                val attName = attribute.getString("InternalName") ?: attribute.getString("Name")

                // get from globals att
                val attDefinition = Services.Application.definition.getAttribute(attName)
                if (attDefinition == null) {
                    Services.Log.warning("Load Entity Attributes", "Attribute Definition not found for $attName")
                    continue
                }

                val trnAtt = DataItem(attDefinition)
                for (propName in attribute.names()) trnAtt.setProperty(propName, attribute[propName])
                definition.Items.add(trnAtt)
            }
        }

        private fun loadLevels(structureDef: StructureDefinition, structure: INodeObject) {
            val tempLevels: HashMap<String, LevelDefinition> = LinkedHashMap()
            val levels = structure.optCollection("Levels")
            for (i in 0 until levels.length()) {
                val level = levels.getNode(i) ?: continue
                val parentLevel = Strings.toLowerCase(level.getString("ParentLevel"))
                var levelDefinition: LevelDefinition
                // Is the first level -> take the already created level, otherwise create a new one an add to the hash
                if (parentLevel.isEmpty()) {
                    levelDefinition = structureDef.Root
                    tempLevels[Strings.toLowerCase(level.getString("Name"))] = structureDef.Root
                } else {
                    levelDefinition = LevelDefinition(null)
                    levelDefinition.setIsCollection(true)
                    tempLevels[Strings.toLowerCase(level.getString("Name"))] = levelDefinition
                }

                // Load Attributes for the level
                loadAttributes(levelDefinition, level)

                // Load the Property Bag for the Level
                levelDefinition.deserialize(level)
                levelDefinition.name // only to force deserialization
                levelDefinition.setName(level.getString("LevelName"))
                levelDefinition.description = level.getString("Description")

                // Add the Level to the Parent Level
                if (parentLevel.isNotEmpty()) {
                    val parentLevelDefinition = tempLevels[parentLevel]
                    parentLevelDefinition?.Levels?.add(levelDefinition)
                    levelDefinition.parent = parentLevelDefinition
                }
            }
        }
    }
}
