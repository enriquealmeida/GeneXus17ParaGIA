package com.genexus.android.core.base.metadata.expressions

import com.genexus.android.core.base.model.ValueCollection
import com.genexus.android.core.base.serialization.INodeObject
import java.util.HashMap

class ConstantCollectionExpression(node: INodeObject) : Expression {
    val parameters: List<Expression>

    init {
        parameters = mutableListOf()
        for (parameterNode in node.optCollection("parameter"))
            parameters.add(ExpressionFactory.parse(parameterNode))
    }

    override fun eval(context: IExpressionContext?): Expression.Value {
        var collection: ValueCollection? = null
        for (parameter in parameters) {
            val parameterValue = parameter.eval(context)
            if (collection == null)
                collection = ValueCollection(parameterValue.type)
            collection.add(parameterValue.coerceToString())
        }
        return Expression.Value.newCollection(collection)
    }

    override fun values(nameTypes: HashMap<String, DataType>) {
        for (p in parameters)
            p.values(nameTypes)
    }

    override fun needsBackgroundThread(): Boolean {
        return parameters.any { it.needsBackgroundThread() }
    }

    override fun toString(): String {
        val list = mutableListOf<String>()
        for (p in parameters)
            list.add(p.toString())
        return "(${list.joinToString()})"
    }

    companion object {
        const val TYPE = "collection"
    }
}
