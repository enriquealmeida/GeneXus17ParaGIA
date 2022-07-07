package com.genexus.android.json

import com.genexus.android.core.base.serialization.INodeCollection
import com.genexus.android.core.base.serialization.INodeObject
import com.genexus.android.core.base.services.Services
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class NodeObject(val inner: JSONObject) : INodeObject {
    /**
     * Returns the JSON String corresponding to this Node Object.
     */
    override fun toString() = inner.toString()
    override fun has(name: String) = inner.has(name)

    override fun get(name: String): Any {
        return when (val value = inner.opt(name)) {
            is JSONObject -> NodeObject(value)
            is JSONArray -> NodeCollection(value)
            else -> value
        }
    }

    override fun getCollection(name: String): INodeCollection? {
        val array = inner.optJSONArray(name)
        return array?.let { NodeCollection(it) }
    }

    override fun getNode(name: String): INodeObject? {
        val obj = inner.optJSONObject(name)
        return obj?.let { NodeObject(it) }
    }

    override fun getString(name: String): String? {
        return try {
            inner.getString(name)
        } catch (e: JSONException) {
            null
        }
    }

    override fun names() = inner.names().toList()

    override fun optCollection(name: String): INodeCollection {
        val array = inner.optJSONArray(name)
        if (array != null)
            return NodeCollection(array)

        // Return array from a single item if present.
        val collection = NodeCollection()
        val item = inner.optJSONObject(name)
        if (item != null)
            collection.put(NodeObject(item))
        return collection
    }

    override fun optString(name: String) = optString(name, "")
    override fun optString(name: String, defaultValue: String): String = inner.optString(name, defaultValue)
    override fun optBoolean(name: String) = optBoolean(name, false)
    override fun optInt(name: String) = inner.optInt(name)
    override fun optFloat(name: String) = optDouble(name).toFloat()
    override fun optDouble(name: String) = inner.optDouble(name)
    override fun optStringArray(name: String) = inner.optJSONArray(name).toList()

    override fun optBoolean(name: String, defaultValue: Boolean): Boolean {
        val strValue = optString(name)
        return Services.Strings.tryParseBoolean(strValue, defaultValue)
    }

    override fun put(name: String, value: Any?) {
        try {
            val v = when (value) {
                is NodeObject -> value.inner
                is NodeCollection -> value.inner
                else -> value
            }
            inner.put(name, v)
        } catch (e: JSONException) {
            Services.Exceptions.handle(e)
        }
    }

    override fun isAtomic(name: String): Boolean {
        return try {
            inner[name].let { !(it is JSONObject || it is JSONArray) }
        } catch (e: JSONException) {
            false
        }
    }
}

fun JSONArray?.toList(): List<String> {
    if (this == null)
        return emptyList()

    val vector = mutableListOf<String>()
    try {
        for (i in 0 until length())
            vector.add(getString(i))
    } catch (e: JSONException) {
        Services.Exceptions.handle(e)
    }
    return vector
}
