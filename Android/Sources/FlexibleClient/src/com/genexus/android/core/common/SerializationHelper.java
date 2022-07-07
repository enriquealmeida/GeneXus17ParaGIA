package com.genexus.android.core.common;

import android.content.Context;

import com.genexus.android.json.NodeCollection;
import com.genexus.android.json.NodeObject;
import com.genexus.android.core.base.serialization.INodeCollection;
import com.genexus.android.core.base.serialization.INodeObject;
import com.genexus.android.core.base.services.ISerialization;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.utils.Cast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializationHelper implements ISerialization {
    private final Context mContext;

    public SerializationHelper(Context context) {
        mContext = context;
    }

    @Override
    public INodeObject createNode() {
        return new NodeObject(new JSONObject());
    }

    @Override
    public INodeObject createNode(String json) {
        try {
            return new NodeObject(new JSONObject(json));
        } catch (JSONException e) {
            return null;
        }
    }

    @Override
    public INodeObject createNode(Object json) {
        // Try to deserialize from JSON object.
        INodeObject node = Cast.as(INodeObject.class, json);

        // Try to deserialize fron native JSON object (e.g. from some SD API actions).
        if (node == null && json instanceof JSONObject)
            node = new NodeObject((JSONObject) json);

        // Try to deserialize from string (e.g. from procedure call output).
        if (node == null && json instanceof String)
            node = createNode((String) json);

        // Special case: when the HTTP service doesn't know whether the data is a collection or not,
        // it might create a one-item JSONArray instead of an individual JSONObject. Account for that.
        if (node == null) {
            INodeCollection nodes = createCollection(json);
            if (nodes != null && nodes.length() == 1)
                return nodes.getNode(0);
        }

        return node;
    }

    @Override
    public INodeCollection createCollection() {
        return new NodeCollection(new JSONArray());
    }

    @Override
    public INodeCollection createCollection(String json) {
        try {
            return new NodeCollection(new JSONArray(json));
        } catch (JSONException e) {
            return null;
        }
    }

    @Override
    public INodeCollection createCollection(Object json) {
        // Try to deserialize from JSON object.
        INodeCollection nodes = Cast.as(INodeCollection.class, json);

        // Fix for Java. If collections of 1 element are serialized as a single node,
        // nodes will be null, but reading a single node will succeed. Create a fake
        // collection in that case.
        if (nodes == null) {
            INodeObject singleNode = Cast.as(INodeObject.class, json);
            if (singleNode != null) {
                nodes = Services.Serializer.createCollection();
                nodes.put(singleNode);
            }
        }

        // Try to deserialize fron native JSON object (e.g. from some SD API actions).
        if (nodes == null && json instanceof JSONArray)
            nodes = new NodeCollection((JSONArray) json);

        // Try to deserialize from string (e.g. from procedure call output).
        if (nodes == null && json instanceof String)
            nodes = createCollection((String) json);

        return nodes;
    }

    @Override
    public boolean serializeObject(Object object, String filename) {
        try {
            FileOutputStream file = mContext.openFileOutput(filename, Context.MODE_PRIVATE);
            ObjectOutputStream output = new ObjectOutputStream(file);
            output.writeObject(object);
            output.close();
            return true;
        } catch (IOException ex) {
            Services.Log.error(String.format("Error serializing object to '%s'.", filename), ex);
            return false;
        }
    }

    @Override
    public Object deserializeObject(String filename) {
        try {
            FileInputStream file = mContext.openFileInput(filename);
            ObjectInputStream input = new ObjectInputStream(file);
            Object obj = input.readObject();
            input.close();
            return obj;
        } catch (IOException | ClassNotFoundException ex) {
            Services.Log.error(String.format("Error deserializing object from '%s'.", filename), ex);
            return false;
        }
    }
}
