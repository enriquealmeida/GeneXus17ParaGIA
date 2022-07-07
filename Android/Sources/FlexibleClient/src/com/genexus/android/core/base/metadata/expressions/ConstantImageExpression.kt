package com.genexus.android.core.base.metadata.expressions

import com.genexus.android.core.base.serialization.INodeObject
import com.genexus.android.core.base.services.Services

internal class ConstantImageExpression(node: INodeObject?) : ConstantExpression(node) {
    override fun eval(context: IExpressionContext): Expression.Value {
        constant?.let { imageName ->
            Services.Resources.getImage(context.action.context, imageName)?.let { imageFile ->
                imageFile.uri.substringResources()?.let {
                    return Expression.Value.newString(it)
                }
            }
        }
        Services.Log.warning("Image not found in constant image expression '$constant'.")
        return Expression.Value.newString("")
    }

    private fun String.substringResources(): String? {
        val pos = lowercase().indexOf("/resources/")
        return if (pos >= 0)
            substring(pos + 1)
        else
            null
    }

    companion object {
        const val TYPE = "image"
    }
}
