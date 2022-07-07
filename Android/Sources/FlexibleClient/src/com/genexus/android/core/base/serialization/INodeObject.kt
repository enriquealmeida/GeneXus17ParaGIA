package com.genexus.android.core.base.serialization

interface INodeObject {
    fun names(): List<String>

    /**
     * Returns true if the value mapped by name is 'atomic' (i.e. a String, Boolean, Integer, Long or Double,
     * but not a JSONObject or JSONArray.
     */
    fun isAtomic(name: String): Boolean
    operator fun get(name: String): Any
    fun put(name: String, value: Any?)
    fun has(name: String): Boolean
    fun getNode(name: String): INodeObject?
    fun getCollection(name: String): INodeCollection?
    fun optCollection(name: String): INodeCollection
    fun getString(name: String): String?
    fun optString(name: String): String
    fun optString(name: String, defaultValue: String): String
    fun optBoolean(name: String): Boolean
    fun optBoolean(name: String, defaultValue: Boolean): Boolean
    fun optInt(name: String): Int
    fun optFloat(name: String): Float
    fun optDouble(name: String): Double
    fun optStringArray(name: String): List<String>
}
