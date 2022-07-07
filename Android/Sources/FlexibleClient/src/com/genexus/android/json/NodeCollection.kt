package com.genexus.android.json

import com.genexus.android.core.base.serialization.INodeCollection
import com.genexus.android.core.base.serialization.INodeObject
import org.json.JSONArray
import org.json.JSONException
import java.lang.IllegalStateException
import java.util.NoSuchElementException

class NodeCollection(val inner: JSONArray = JSONArray()) : INodeCollection {
    /**
     * Returns the JSON String corresponding to this Node Collection.
     */
    override fun toString() = inner.toString()
    override fun length() = inner.length()

    override fun getString(index: Int): String? {
        return try {
            inner.getString(index)
        } catch (e: JSONException) {
            throw IllegalStateException("Exception retrieving string item from NodeCollection", e)
        }
    }

    override fun getNode(index: Int): INodeObject? {
        return try {
            NodeObject(inner.getJSONObject(index))
        } catch (e: JSONException) {
            if (e.message?.startsWith("Value null at") == true)
                null // null in json
            else
                throw IllegalStateException("Exception retrieving INodeObject item from NodeCollection", e)
        }
    }

    override fun put(value: String) {
        inner.put(value)
    }

    override fun put(value: INodeObject) {
        inner.put((value as NodeObject).inner)
    }

    override fun iterator() = object : Iterator<INodeObject> {
        private var current = 0

        override fun hasNext(): Boolean {
            while (current < inner.length() && getNode(current) == null)
                current++
            return current < inner.length()
        }

        override fun next(): INodeObject {
            if (hasNext())
                return getNode(current++)!!
            else
                throw NoSuchElementException()
        }
    }
}
