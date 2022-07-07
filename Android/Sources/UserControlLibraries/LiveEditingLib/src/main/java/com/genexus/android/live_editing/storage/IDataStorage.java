package com.genexus.android.live_editing.storage;

import java.util.List;

import com.genexus.android.live_editing.support.Endpoint;

public interface IDataStorage {
	void storeEndpoint(Endpoint endpoint);
	List<Endpoint> getStoredEndpoints();
}
