package com.genexus.android.core.utils;

import androidx.annotation.NonNull;

import com.genexus.android.core.base.utils.ReflectionHelper;
import com.genexus.GXBaseCollection;
import com.genexus.xml.GXXMLSerializable;

public class GXInstanceFactory {

	public static @NonNull
	GXBaseCollection getGXBaseCollectionInstance(String className, String elementsName, String containedXmlNamespace, int remoteHandle) {
		Class<?> clazzType = ReflectionHelper.getClass(Object.class, className);
		if (clazzType == null)
			throw new IllegalStateException(String.format("%s class could not be loaded", className));

		return new GXBaseCollection(clazzType, elementsName, containedXmlNamespace, remoteHandle);
	}

	public static @NonNull GXXMLSerializable getGXXMLSerializable(String className, boolean emptyParameters) {
		Class<?> clazz = ReflectionHelper.getClass(Object.class, className);
		if (clazz == null)
			throw new IllegalStateException(String.format("%s class could not be loaded", className));

		Object obj = ReflectionHelper.createDefaultInstance(clazz, emptyParameters);
		if (obj == null)
			throw new IllegalStateException(String.format("%s class could not be instantiated", className));

		return (GXXMLSerializable) obj;
	}
}
