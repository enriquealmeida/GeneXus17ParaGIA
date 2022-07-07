package com.genexus.android.live_editing.commands;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.genexus.android.core.activities.GenexusActivity;
import com.genexus.android.core.base.metadata.GenexusApplication;
import com.genexus.android.core.base.metadata.IViewDefinition;
import com.genexus.android.core.base.metadata.enums.GxObjectTypes;
import com.genexus.android.core.base.serialization.INodeObject;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.fragments.IDataView;
import com.genexus.android.live_editing.support.ILiveEditingImageManager;
import com.google.gson.annotations.SerializedName;

public class CommandLayoutChanged implements IServerCommand {
	@SerializedName("ObjName")
	private final String mObjectName;
	@SerializedName("ObjType")
	private final String mObjectType;
	@SerializedName("Data")
	private final INodeObject mNewMetadata;

	public CommandLayoutChanged(@NonNull String objectName, @NonNull String objectType,
								@NonNull INodeObject newMetadata) {
		mObjectName = objectName;
		mObjectType = objectType;
		mNewMetadata = newMetadata;
	}

	@Override
	public boolean execute(ILiveEditingImageManager liveEditingImageManager) {
		if (GxObjectTypes.WORK_WITH_GUID.equals(mObjectType)) {
			// Update the metadata for the object
			MetadataHelper.replacePattern(mObjectName, mNewMetadata);
			// Force updating the reference to the main's DataView which may be this object.
			// When using Slide menu, SlideNavigation.getComponents uses
			// Services.Application.get().getMain() to get the dataview and may create components from
			// that DataView.
			GenexusApplication app = Services.Application.get();
			IViewDefinition mainView = app.getDefinition().getView(app.getAppEntry());
			if (mainView != null) {
				app.setMain(mainView);
			}
			return true;
		}
		return false;
	}

	@Override
	public void applyChanges(Activity activity) {
		// Only re-create the activity if the layout modified is from an object contained inside this activity.
		if (activity instanceof GenexusActivity) {
			GenexusActivity genexusActivity = (GenexusActivity) activity;
			if (hasObjectInside(genexusActivity)) {
				// We should prevent saving state because, for instance, SlideComponents saves
				// references to itself and it contains references to old ViewData with out-of-date
				// metadata.
				genexusActivity.setShouldNotSaveState(true);
				genexusActivity.recreate();
			}
		}
	}

	@Override
	public boolean shouldInspectUIAfterApplyingChanges() {
		return false;
	}

	private boolean hasObjectInside(GenexusActivity gxActivity) {
		boolean hasObject = false;

		Iterable<IDataView> dataViews = gxActivity.getDataViews();
		for (IDataView dataView : dataViews) {
			IViewDefinition viewDefinition = dataView.getDefinition();
			String name = viewDefinition.getObjectName();
			if (name != null && name.equalsIgnoreCase(mObjectName)) {
				hasObject = true;
				break;
			}
		}

		return hasObject;
	}
}
