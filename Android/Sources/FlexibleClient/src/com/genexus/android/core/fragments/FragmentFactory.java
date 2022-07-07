package com.genexus.android.core.fragments;

import androidx.annotation.NonNull;

import com.genexus.android.core.app.ComponentParameters;
import com.genexus.android.core.base.metadata.DashboardMetadata;
import com.genexus.android.core.base.metadata.IDataViewDefinition;
import com.genexus.android.core.base.metadata.SectionDefinition;
import com.genexus.android.core.base.metadata.enums.DisplayModes;

public class FragmentFactory
{
	public static @NonNull BaseFragment newFragment(@NonNull ComponentParameters params)
	{
		if (params.Object instanceof SectionDefinition && DisplayModes.isEdit(params.Mode))
		{
			SectionDefinition section = (SectionDefinition)params.Object;
			if (section.getBusinessComponent() != null)
				return new LayoutFragmentEditBC();
		}
		
		if (params.Object instanceof IDataViewDefinition)
			return new LayoutFragment();
		
		if (params.Object instanceof DashboardMetadata)
			return new DashboardFragment();
		
		throw new IllegalArgumentException("Cannot create a Fragment for these parameters.");
	}
}
