package com.genexus.android.core.base.services;

import com.genexus.android.core.base.model.EntityList;
import com.artech.synchronization.ISynchronizationHelper;

import org.json.JSONArray;

import java.util.LinkedHashMap;

public interface ISyncService extends ISynchronizationHelper {
	long getSyncLastTime();
	void setSyncLastTime(long lastTime);
	long getSendLastTime();
	void setSendLastTime(long lastTime);
	void callReorCreatePendingEvents(boolean runAsFullReor);
	EntityList getPendingEventsList(String status);
	EntityList getPendingEventsListWithCheckPoints(String status);
	boolean sendPendingsToServerInBackground();
	int callSynchronizer(boolean includeHashesinPost, boolean waitIfRunning);
	int callSynchronizerCheck();
	void storeHashMapOnDisk(LinkedHashMap<String, String> tablesCheckSum);
	JSONArray readJsonArrayFromDisk();
	LinkedHashMap<String, String> convertJsonArraytoHash(JSONArray jsonParameters);
	int saveCheckPoint();
	void restorePendingToDatabase(EntityList paramEntityList);
	void cleanExistingHashes();
	void cleanExistingHashes(String tempChecksum, String tempSync);
	void cleanBlobs(String tempBlobsDirectory);
}
