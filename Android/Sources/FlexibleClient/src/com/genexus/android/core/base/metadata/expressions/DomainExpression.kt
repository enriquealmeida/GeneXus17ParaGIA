package com.genexus.android.core.base.metadata.expressions

import com.genexus.android.core.base.serialization.INodeObject
import java.util.HashMap

class DomainExpression(node: INodeObject) : Expression {
    private val name = node.getString("@exprValue")

    override fun eval(context: IExpressionContext?): Expression.Value {
        return Expression.Value.newDomain(name)
    }

    override fun values(nameTypes: HashMap<String, DataType>) {
    }

    override fun needsBackgroundThread(): Boolean {
        return false
    }

    companion object {
        const val TYPE = "domain"
    }
}
