package com.genexus.android.core.externalapi;

import androidx.annotation.NonNull;

import com.genexus.android.core.actions.ApiAction;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.NameMap;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ExternalApiFactory {

	private final NameMap<ExternalApiDefinition> mDefinitions;
	private final NameMap<ExternalApi> mInstances;
	private final NameMap<Constructor<? extends ExternalApi>> sConstructorsCache;

	public ExternalApiFactory() {
		mDefinitions = new NameMap<>();
		mInstances = new NameMap<>();
		sConstructorsCache = new NameMap<>();
	}

	public void addDefinition(ExternalApiDefinition apiDefinition) {
		mDefinitions.put(apiDefinition.Name, apiDefinition);
		callInitialize(apiDefinition.mClass);
	}

	private void callInitialize(Class<? extends ExternalApi> clazz) {
		try {
			clazz.getMethod("initialize").invoke(null);
		} catch (NoSuchMethodException |
			IllegalAccessException |
			InvocationTargetException ignored) {
		}
	}

	public static void addApi(ExternalApiDefinition externalApiDefinition) {
		Services.Application.getExternalApiFactory().addDefinition(externalApiDefinition);
	}

	public ExternalApi getInstance(String apiName, ApiAction action) {
		ExternalApiDefinition apiDefinition = mDefinitions.get(apiName);

		if (apiDefinition == null)
			throw new NoClassDefFoundError(String.format("No ExternalApiDefinition was found for '%s'.", apiName));

		try {
			ExternalApi api;
			if (apiDefinition.RecreateWhenExecuting)
				api = createInstanceOld(apiDefinition, action);
			else
				api = createInstance(apiDefinition, action);

			api.setAction(action);
			return api;
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException(String.format("Error while instantiating External Api class '%s'.", apiDefinition.mClass.getName()), e);
		}
	}

	@NonNull
	private ExternalApi createInstance(ExternalApiDefinition apiDefinition, ApiAction action)
		throws IllegalAccessException, InvocationTargetException, InstantiationException {
		Class<? extends ExternalApi> clazz = apiDefinition.mClass;
		ExternalApi api = mInstances.get(clazz.getName());
		if (api != null)
			return api;

		api = getConstructor(clazz).newInstance(action);
		mInstances.put(clazz.getName(), api);
		return api;
	}

	@NonNull
	private ExternalApi createInstanceOld(ExternalApiDefinition apiDefinition, ApiAction action)
		throws IllegalAccessException, InvocationTargetException, InstantiationException {
		return getConstructorOld(apiDefinition.mClass).newInstance(action);
	}

	@NonNull
	private Constructor<? extends ExternalApi> getConstructorOld(Class<? extends ExternalApi> clazz) {
		Constructor<? extends ExternalApi> constructor = sConstructorsCache.get(clazz.getName());
		if (constructor == null) {
			constructor = getConstructor(clazz);
			sConstructorsCache.put(clazz.getName(), constructor);
		}

		return constructor;
	}

	@NonNull
	private Constructor<? extends ExternalApi> getConstructor(Class<? extends ExternalApi> clazz) {
		try {
			return clazz.getConstructor(ApiAction.class);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(String.format("External Api class '%s' does not have the appropriate constructor.", clazz.getName()), e);
		}
	}
}
