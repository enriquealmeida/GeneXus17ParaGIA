package com.genexus.android.core.base.metadata.expressions

import com.genexus.android.core.base.metadata.enums.GxObjectTypes
import com.genexus.android.core.base.serialization.INodeObject
import java.lang.IllegalArgumentException

class GxObjectLinkExpression(node: INodeObject) : Expression {
    private val value: String = node.optString("@exprValue")

    override fun eval(context: IExpressionContext?): Expression.Value {
        return when (GxObjectTypes.getGxObjectTypeFromName(value)) {
            GxObjectTypes.QUERY, GxObjectTypes.DATAPROVIDER -> Expression.Value(Expression.Type.OBJECT_LINK, value)
            else -> throw IllegalArgumentException("Unsupported type for link '$value'")
        }
    }

    override fun values(nameTypes: HashMap<String, DataType>) { }

    override fun needsBackgroundThread() = false

    companion object {
        const val TYPE = "gxobjectlink"
    }
}
