package com.genexus.android.core.externalapi;

@SuppressWarnings({"InconsistentCapitalization", "checkstyle:MemberName"})
public class ExternalApiDefinition {

	public final String Name;
	public final Class<? extends ExternalApi> mClass;
	public final boolean RecreateWhenExecuting;

	public ExternalApiDefinition(String name, Class<? extends ExternalApi> clazz) {
		this(name, clazz, true);
	}

	public ExternalApiDefinition(String name, Class<? extends ExternalApi> clazz, boolean recreateWhenExecuting) {
		Name = name;
		mClass = clazz;
		RecreateWhenExecuting = recreateWhenExecuting;
	}
}
