package com.genexus.android.core.base.metadata.expressions

import com.genexus.android.core.base.serialization.INodeObject

internal class StyleClassExpression(node: INodeObject) : ConstantExpression(node) {
    override fun eval(context: IExpressionContext?): Expression.Value {
        return Expression.Value.newString(constant)
    }

    companion object {
        const val TYPE = "styleclass"
    }
}
