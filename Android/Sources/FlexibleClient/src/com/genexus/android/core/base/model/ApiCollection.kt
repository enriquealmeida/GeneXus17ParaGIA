package com.genexus.android.core.base.model

import com.genexus.android.core.base.metadata.expressions.Expression
import com.genexus.android.core.base.serialization.INodeCollection
import com.genexus.android.core.externalapi.ExternalApi

class ApiCollection : BaseCollection<ExternalApi>() {
    override fun getItemType() = Expression.Type.API
    override fun serializeItem(collection: INodeCollection?, item: ExternalApi?, jsonFormat: Short) { }
    override fun deserializeItem(collection: INodeCollection?, index: Int, jsonFormat: Short) = null
}
