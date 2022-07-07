package com.genexus.android.core.base.metadata.expressions

import com.genexus.android.core.base.model.Entity
import java.lang.RuntimeException

class ExpressionErrorException(expression: Expression, contextEntity: Entity, cause: Throwable) : RuntimeException(message(expression, contextEntity), cause) {
    companion object {
        private fun message(expression: Expression, contextEntity: Entity): String {
            val nameTypes = HashMap<String, DataType>()
            expression.values(nameTypes)
            return "$expression   Panel: '${contextEntity.definition.name}'   ${variablesToString(nameTypes, contextEntity)}"
        }

        private fun variablesToString(map: Map<String, DataType>, contextEntity: Entity): String {
            return map.keys.sortedBy { it }.joinToString("  ", transform = { name -> "$name:${map[name]} = \"${contextEntity.optStringProperty(name)}\"" })
        }
    }
}
