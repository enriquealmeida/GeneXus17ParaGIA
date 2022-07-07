package com.genexus.android.live_editing.support;

import androidx.annotation.Nullable;

import com.genexus.android.core.base.services.IResourcesService;

public interface ILiveEditingImageManager extends IResourcesService {
	@Nullable Endpoint getCurrentEndpoint();
	void setCurrentEndpoint(Endpoint endpoint);
	void addImageToDirtyList(String imageName);
}
