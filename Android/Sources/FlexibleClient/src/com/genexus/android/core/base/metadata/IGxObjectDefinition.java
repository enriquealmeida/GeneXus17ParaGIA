package com.genexus.android.core.base.metadata;

import com.genexus.android.core.base.metadata.enums.Connectivity;

/**
 * Interface for basic information present in all GX objects
 * (BCs, procedures, Panels, dashboards, &c)
 */
public interface IGxObjectDefinition
{
	/**
	 * Returns the name of the object.
	 */
	String getName();

	/***
	 * Returns the {@link Connectivity} access associated to this object (might be online,
	 * offline, or "inherit", in which case it should be determined by the calling context.
	 */
	Connectivity getConnectivitySupport();
}
