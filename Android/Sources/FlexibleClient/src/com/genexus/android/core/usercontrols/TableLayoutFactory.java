package com.genexus.android.core.usercontrols;

import android.content.Context;

import com.genexus.android.layout.IGxLayout;
import com.genexus.android.core.base.metadata.layout.LayoutDefinition;
import com.genexus.android.core.base.metadata.layout.TableDefinition;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.ReflectionHelper;
import com.genexus.android.core.ui.Coordinator;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.TreeMap;

public class TableLayoutFactory {
    public static final String FLEX = "Flex";
    public static final String ROOTFLEX = "RootFlex";

    private TreeMap<String, Class<?>> mMap = new TreeMap<>();

    public void addControl(String type, Class<? extends IGxLayout> clazz) {
        mMap.put(type, clazz);
    }

    public boolean hasControl(String type) {
        return mMap.containsKey(type);
    }

    public Object createControl(String type, Object[] params) {
        Class<?> clazz = mMap.get(type);
        if (clazz == null) {
            Services.Log.error(String.format("Table layout of type %s is not registered.", type));
            return null;
        }

        Class<?>[] paramsTypes = getConstructorParameters(type);
        if (paramsTypes == null) {
            Services.Log.error(String.format("Unknown type %s.", type));
            return null;
        }

        Constructor<?> constructor = ReflectionHelper.getConstructor(clazz, paramsTypes);
        if (constructor == null) {
            Services.Log.error(String.format("User control class '%s' does not have an appropriate constructor.", clazz.getName()));
            return null;
        }

        try
        {
            return constructor.newInstance(params);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException ex) {
            Services.Log.error("Exception creating custom Table Layout.", ex);
            return null;
        }
    }

    private Class<?>[] getConstructorParameters(String type) {
        switch (type) {
            case FLEX:
                return new Class<?>[] { Context.class, TableDefinition.class, Coordinator.class };
            case ROOTFLEX:
                return new Class<?>[] { Context.class, LayoutDefinition.class, Coordinator.class };
            default:
                return null;
        }
    }
}
