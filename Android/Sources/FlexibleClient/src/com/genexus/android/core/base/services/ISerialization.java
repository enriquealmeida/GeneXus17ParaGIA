package com.genexus.android.core.base.services;

import com.genexus.android.core.base.serialization.INodeCollection;
import com.genexus.android.core.base.serialization.INodeObject;

public interface ISerialization
{
	INodeObject createNode();
	INodeObject createNode(String json);
	INodeObject createNode(Object json);

	INodeCollection createCollection();
	INodeCollection createCollection(String json);
	INodeCollection createCollection(Object json);

	boolean serializeObject(Object object, String filename);
	Object deserializeObject(String filename);
}
