package com.genexus.android.core.base.serialization

interface INodeCollection : Iterable<INodeObject> {
    fun length(): Int
    fun getString(index: Int): String?
    fun getNode(index: Int): INodeObject?
    fun put(value: String)
    fun put(value: INodeObject)
}
