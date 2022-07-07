package com.genexus.android.core.base.synchronization;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.artech.base.services.AndroidContext;
import com.genexus.android.core.base.metadata.DataItem;
import com.genexus.android.core.base.metadata.GenexusApplication;
import com.genexus.android.core.base.metadata.StructureDefinition;
import com.artech.base.synchronization.GXOfflineDatabase;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.model.EntityList;
import com.genexus.android.core.base.model.PropertiesObject;
import com.genexus.android.core.base.services.ISyncService;
import com.genexus.android.core.base.services.LogCategory;
import com.genexus.android.core.base.services.ServiceResponse;
import com.genexus.android.core.base.services.Services;
import com.artech.base.synchronization.bc.SdtGxPendingEvent;
import com.artech.base.synchronization.dbcreate.Reorganization;
import com.artech.base.synchronization.dbcreate.reorg;
import com.artech.base.synchronization.dps.getpendingeventandcheckpointsbytimestamp;
import com.artech.base.synchronization.dps.getpendingeventbytimestamp;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.base.utils.ThreadUtils;
import com.genexus.android.core.layers.GxObjectFactory;
import com.genexus.android.core.layers.LocalUtils;
import com.artech.synchronization.ISynchronizationHelper;
import com.genexus.GXutil;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.UUID;

import json.org.json.JSONObject;

import static com.genexus.android.core.common.FileHelper.deleteDir;
import static com.genexus.android.core.common.FileHelper.deleteFile;

public class SynchronizationHelper implements ISynchronizationHelper, ISyncService {
	public enum DataSyncCriteria {
		Automatic, Manual, AfterElapsedTime
	}

	public enum LocalChangesProcessing {
		WhenConnected, UserDefined, Never
	}

	private static final String LOG_TAG = "SyncHelper";

	private static final String OUTPUT_PARAMETER = "ReturnValue";
	private static final String SYNC_LAST_TIME = "sync_last_time";
	private static final String SEND_LAST_TIME = "send_last_time";
	private static final String CHECKPOINT_DATA = "__checkpoint__";
	public static final short CHECKPOINT_STATUS = 51;

	public static final String SYNC_BLOB_PLACEHOLDER = "<!gxfile%s!>";

	public static boolean isRunningSendOrReceive = false;
	public static boolean isRunningReceive = false;

	// SYNC constants result START
	public static final int SYNC_OK = 0;
	// not used, Change constants for IOS compat.
	public static final int SYNC_FAIL_SYNCNOTNEEDED = 1;
	public static final int SYNC_FAIL_APPNOTOFFLINE = 2;

	// used
	public static final int SYNC_FAIL_ERRORHASPENDINGEVENTS = 3;
	public static final int SYNC_FAIL_UNKNOWN = 99;
	public static final int SYNC_FAIL_ALREADYRUNNING = 8;

	//server constants
	public static final int SYNC_FAIL_SERVERBYROWVERSIONINVALID = 51;
	public static final int SYNC_FAIL_SERVERREINSTALL = 52;
	public static final int SYNC_FAIL_SERVERVERSIONINVALID = 53;
	// SYNC constants result END

	// SYNC check Constant
	public static final int SYNC_CHECK_FAIL = 3;
	// END SYNC check constant

	// Status for check in DPs
	public static final String PENDING_STATUS_STRING = "1";
	public static final String CHECKPOINT_STATUS_STRING = "51";

	private final Context mAppContext;
	private final GenexusApplication mGenexusApplication;

	public SynchronizationHelper(Context appContext, GenexusApplication genexusApplication) {
		mAppContext = appContext;
		mGenexusApplication = genexusApplication;
	}

	@Override
	public long getSyncLastTime() {
		return Services.Preferences.getLong(SYNC_LAST_TIME, 0);
	}

	@Override
	public void setSyncLastTime(long lastTime) {
		Services.Preferences.setLong(SYNC_LAST_TIME, lastTime);
	}

	@Override
	public long getSendLastTime() {
		return Services.Preferences.getLong(SEND_LAST_TIME, 0);
	}

	@Override
	public void setSendLastTime(long lastTime) {
		Services.Preferences.setLong(SEND_LAST_TIME, lastTime);
	}

	@Override
	public void callReorCreatePendingEvents(boolean runAsFullReor) {
		if (runAsFullReor) {
			Reorganization gxReor = new Reorganization();
			gxReor.execute();
		} else {
			//call the reor to create the sync events table.
			// reor run in auto commit mode
			reorg eventReor = new reorg(-1);
			try {
				eventReor.CreateGxPendingEvent();
			} catch (SQLException e) {
			}
		}
	}

	//Replicator
	@Override
	public EntityList getPendingEventsList(String status) {
		PropertiesObject parameters = new PropertiesObject();

		//Only With this Status.
		parameters.setProperty("PendingEventStatus", status);

		EntityList resultData = new EntityList();

		//call DP to get pending events
		getpendingeventbytimestamp pendingEvent = new getpendingeventbytimestamp(mGenexusApplication.getRemoteHandle());

		LocalUtils.beginTransaction();

		try {
			pendingEvent.execute(parameters);

			Object output = parameters.getProperty(OUTPUT_PARAMETER);
			List<?> outputList = (List<?>) output;
			for (Object outputItem : outputList) {
				if (outputItem instanceof Entity)
					resultData.add((Entity) outputItem);
			}
		} finally {
			LocalUtils.endTransaction();
		}

		return resultData;
	}

	@Override
	public EntityList getPendingEventsListWithCheckPoints(String status) {
		PropertiesObject parameters = new PropertiesObject();

		//Only With this Status.
		parameters.setProperty("PendingEventStatus", status);
		EntityList resultData = new EntityList();

		//call DP to get pending events
		getpendingeventandcheckpointsbytimestamp pendingEvent = new getpendingeventandcheckpointsbytimestamp(mGenexusApplication.getRemoteHandle());
		LocalUtils.beginTransaction();

		try {
			pendingEvent.execute(parameters);

			Object output = parameters.getProperty(OUTPUT_PARAMETER);
			List<?> outputList = (List<?>) output;
			for (Object outputItem : outputList) {
				if (outputItem instanceof Entity)
					resultData.add((Entity) outputItem);
			}
		} finally {
			LocalUtils.endTransaction();
		}

		return resultData;
	}

	@Override
	public boolean sendPendingsToServerInBackground() {
		//TODO: run in a background thread or a service...
		//TODO: Run only if has Internet connection.
		//TODO: run only one an a Time , put a synchronized semaphore?

		// Only call send if time between send has elapsed.
		// calculate time dif
		long minTimeBetweenSends = mGenexusApplication.getSynchronizerMinTimeBetweenSends();
		long nowTime = new Date().getTime();
		long lastSend = getSendLastTime();
		// minTimeBetweenSync in seconds
		if (lastSend != 0 && ((nowTime - lastSend) < (minTimeBetweenSends * 1000))) {
			Services.Log.debug(LogCategory.SYNCHRONIZATION, LOG_TAG, "MinTimeBetweenSends time not happened yet.");

			// set and alarm to execute it later
			SynchronizationSendAlarm alarm = new SynchronizationSendAlarm();
			alarm.setAlarm(mAppContext);
			Services.Log.debug("set sync Send alarm after elapsed time");

			return true;
		}

		SynchronizationSendExecutor.getInstance().startProcess();
		return true;
	}

	//Synchronizer
	@Override
	public int callSynchronizer(boolean includeHashesinPost, boolean waitIfRunning) {
		if (isRunningReceive) {
			if (waitIfRunning) {
				Services.Log.debug(LogCategory.SYNCHRONIZATION, LOG_TAG, "callSynchronizer wait because another Receive is already running (Sync.Receive) ");
				while (isRunningReceive) //another receive is running, wait
				{
					//wait half sec for other receive to finish
					ThreadUtils.sleep(500);
					Services.Log.debug(LogCategory.SYNCHRONIZATION, LOG_TAG, "wait to another Receive to finish");
				}
				Services.Log.debug(LogCategory.SYNCHRONIZATION, LOG_TAG, "callSynchronizer finish wait, start new (Sync.Receive) ");

			} else {
				Services.Log.warning(LogCategory.SYNCHRONIZATION, LOG_TAG, "callSynchronizer not run because Received is already running");
				return SYNC_FAIL_ALREADYRUNNING;
			}
		}
		isRunningSendOrReceive = true;
		isRunningReceive = true;

		// if has pending events, sync should fail
		EntityList pendings = getPendingEventsList(SynchronizationHelper.PENDING_STATUS_STRING); // Pending
		if (pendings.size() > 0) {
			// force a send if send automatic is set.
			if (mGenexusApplication.isOfflineApplication()
					&& mGenexusApplication.getSynchronizerSendAutomatic()) {
				Services.Log.debug(LogCategory.SYNCHRONIZATION, LOG_TAG, "callOfflineReplicator (Sync.Send) from callSynchronizer (Sync.Receive) ");
				//Send
				SynchronizationSendHelper.callOfflineReplicator();
				// replicator could set isRunningSendOrReceive to false
				isRunningSendOrReceive = true;
			}

			pendings = getPendingEventsList(SynchronizationHelper.PENDING_STATUS_STRING); // Pending
			if (pendings.size() > 0) {
				Services.Log.error(LogCategory.SYNCHRONIZATION, LOG_TAG, " Has Pending events, cannot do a receive.");
				isRunningSendOrReceive = false;
				isRunningReceive = false;
				return SYNC_FAIL_ERRORHASPENDINGEVENTS;
			}
		}

		try {
			String synchronizerName = mGenexusApplication.getSynchronizer();
			if (!Services.Strings.hasValue(synchronizerName)) {
				Services.Log.error(LogCategory.SYNCHRONIZATION, LOG_TAG, "Could not get syncronizer:" + synchronizerName);
				return SYNC_FAIL_UNKNOWN;
			}

			// Call synchronizer like a procedure
			String url = mGenexusApplication.UriMaker.getAllBCUrl(synchronizerName);

			JSONArray jsonParameters = new JSONArray(); //should contains tables checksum

			// get hashMap from disk
			LinkedHashMap<String, String> tablesCheckSum = readHashMapFromDisk();

			if (includeHashesinPost) {
				// convert hashMap to json array string.
				jsonParameters = convertHashToJsonArray(tablesCheckSum);
			}

			// Add tables checksum.
			// [["TABLE1","bv834j22bjRqh9kh54EUrUXvxcAc+yNmY"],["TABLE2","bwR0SVLUk8wLKaMhy5ZRtl45T4rKS5KrJlrpZ4t"],["TABLE3",..."], ....]

			// Create Sync class
			String syncClassName = Strings.toLowerCase(synchronizerName);
			GXOfflineDatabase syncObj = GxObjectFactory.getSyncOfflineDatabase(syncClassName);

			// Obtain the synchronizer's version
			String syncVersion;
			syncVersion = syncObj.getSyncVersion();

			// Execute
			ServiceResponse syncResponse = null;
			try {
				try {
					syncResponse = Services.HttpService.postJsonSyncResponse(url, jsonParameters,
							syncVersion);
				} catch (IOException e) {
					Services.Log.error(LogCategory.SYNCHRONIZATION, LOG_TAG, "Error calling when receiving the sync's json response from server", e);
					return SYNC_FAIL_UNKNOWN;
				}

				if (!syncResponse.getResponseOk() || syncResponse.Stream == null) {
					Services.Log.error(LogCategory.SYNCHRONIZATION, LOG_TAG, "Invalid sync response from server");
					return SYNC_FAIL_UNKNOWN;
				}

				Integer errorCode;
				errorCode = syncObj.startJsonParser(syncResponse.Stream);

				if (errorCode != 0) {
					Services.Log.error(LogCategory.SYNCHRONIZATION, LOG_TAG, "Metadata error code received. Code: " + errorCode);
				}

				switch (errorCode) {
					// should return sync_fail .
					case 1:
						return SYNC_FAIL_SERVERBYROWVERSIONINVALID;
					case 2:
						return SYNC_FAIL_SERVERREINSTALL;
					case 3:
						return SYNC_FAIL_SERVERVERSIONINVALID;
				}

				Services.Log.debug(LogCategory.SYNCHRONIZATION, LOG_TAG, "Start invoke local sync proc");

				LocalUtils.beginTransaction();

				try {
					syncObj.executeGXAllSync();

					// We are done with the response, close the stream before doing a new HTTP request.
					if (syncResponse.Stream != null) {
						syncResponse.Stream.close();
					}

					Services.Log.debug(LogCategory.SYNCHRONIZATION, LOG_TAG, "End invoke local sync proc");
					Services.Log.debug(LogCategory.SYNCHRONIZATION, LOG_TAG, "Local sync commit changes");

					LinkedHashMap<String, String> newTablesCheckSum;

					// Get each table checksum and store it.
					LinkedHashMap<String, String> result = syncObj.getTableChecksum();
					newTablesCheckSum = result;

					// Update the checksum's table.
					for (Entry<String, String> entry : newTablesCheckSum.entrySet()) {
						tablesCheckSum.put(entry.getKey(), entry.getValue());
					}

					// store check sum.
					storeHashMapOnDisk(tablesCheckSum);

					// store check sum as json.
					jsonParameters = convertHashToJsonArray(tablesCheckSum);
					storeJsonOnDisk(jsonParameters);

					Services.Log.debug(LogCategory.SYNCHRONIZATION, LOG_TAG, "DATABASE SYNCHRONIZATION FINISHED");
					Services.Log.debug(LogCategory.SYNCHRONIZATION, LOG_TAG, "Database file: " + AndroidContext.ApplicationContext.getDataBaseFilePath());
					Services.Log.debug(LogCategory.SYNCHRONIZATION, LOG_TAG, "Hashes file: " + AndroidContext.ApplicationContext.getDataBaseSyncHashesFilePath());

					// Post confirm sync to server event.
					String urlConfirm = url + "&event=gxconfirmsync";
					ServiceResponse confirmSyncResponse = Services.HttpService.postJsonSyncResponse(urlConfirm, jsonParameters, syncVersion);
					if (confirmSyncResponse.Stream != null)
						confirmSyncResponse.Stream.close();

					//set last time sync
					setSyncLastTime(new Date().getTime());
				} finally {
					LocalUtils.endTransaction();
				}
			} finally {
				if (syncResponse != null && syncResponse.Stream != null) {
					syncResponse.Stream.close();
				}
			}
		} catch (IOException e) {
			// Any Error should return sync_Failed
			Services.Log.error(LogCategory.SYNCHRONIZATION, LOG_TAG, "Error running callSynchronizer method", e);
			return SYNC_FAIL_UNKNOWN;
		} finally {
			isRunningSendOrReceive = false;
			isRunningReceive = false;
		}

		return SYNC_OK;
	}

	//Synchronizer Check
	@Override
	public int callSynchronizerCheck() {
		// get Sync Name
		String synchronizerName = mGenexusApplication.getSynchronizer();
		if (!Services.Strings.hasValue(synchronizerName)) {
			Services.Log.error(LogCategory.SYNCHRONIZATION, LOG_TAG, "Could not get syncronizer:" + synchronizerName);
			return SYNC_CHECK_FAIL;
		}

		// Call synchronizer like a procedure
		String url = mGenexusApplication.UriMaker.getAllBCUrl(synchronizerName);
		// get hashMap from disk
		LinkedHashMap<String, String> tablesCheckSum = readHashMapFromDisk();
		// convert hashMap to json array string.
		JSONArray jsonParameters = convertHashToJsonArray(tablesCheckSum); //should contains tables checksum

		// Create Sync class
		String syncClassName = Strings.toLowerCase(synchronizerName);
		GXOfflineDatabase syncObj = GxObjectFactory.getSyncOfflineDatabase(syncClassName);
		// Obtain the synchronizer's version
		String syncVersion = syncObj.getSyncVersion();

		//Post confirm sync to server event.
		String urlConfirm = url + "&event=gxchecksync";
		try {
			ServiceResponse response = Services.HttpService.postJsonSyncResponse(urlConfirm, jsonParameters, syncVersion);
			// read responce.
			StringWriter writer = new StringWriter();
			IOUtils.copy(response.Stream, writer);
			String testOutput = writer.toString();

			json.org.json.JSONArray metadataArray;
			try {
				metadataArray = new json.org.json.JSONArray(testOutput);
				json.org.json.JSONArray metadataArrayInner = metadataArray.optJSONArray(0);
				if (metadataArrayInner != null) {
					String errorCode = metadataArrayInner.optString(4);
					return Integer.parseInt(errorCode);
				}

			} catch (json.org.json.JSONException | NumberFormatException e) {
			}

			// Read sync check result.
			return SYNC_CHECK_FAIL;
		} catch (IOException e) {
		}

		return SYNC_CHECK_FAIL;
	}

	@Override
	public void storeHashMapOnDisk(LinkedHashMap<String, String> tablesCheckSum) {
		String filePath = AndroidContext.ApplicationContext.getDataBaseSyncFilePath();

		FileOutputStream fos;
		try {
			fos = new FileOutputStream(filePath);

			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(tablesCheckSum);
			oos.close();

		} catch (IOException e) {
		}
	}

	private void storeJsonOnDisk(JSONArray jsonParameters) {
		String filePath = AndroidContext.ApplicationContext.getDataBaseSyncHashesFilePath();

		FileOutputStream fos;
		try {
			fos = new FileOutputStream(filePath);
			OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
			osw.write(jsonParameters.toString());
			osw.close();
		} catch (IOException e) {
		}
	}

	@SuppressWarnings("unchecked")
	private LinkedHashMap<String, String> readHashMapFromDisk() {
		LinkedHashMap<String, String> tablesCheckSum = new LinkedHashMap<>();

		String filePath = AndroidContext.ApplicationContext.getDataBaseSyncFilePath();
		File file = new File(filePath);

		try {

			if (file.exists()) {
				FileInputStream fis = new FileInputStream(filePath);

				ObjectInputStream ois = new ObjectInputStream(fis);
				tablesCheckSum = (LinkedHashMap<String, String>) ois.readObject();
				ois.close();
			}

		} catch (ClassNotFoundException | IOException e) {
		}
		return tablesCheckSum;
	}

	@Override
	public JSONArray readJsonArrayFromDisk() {
		JSONArray jsonArray = new JSONArray();

		String filePath = AndroidContext.ApplicationContext.getDataBaseSyncHashesFilePath();
		File file = new File(filePath);

		try {

			if (file.exists()) {
				FileInputStream fis = new FileInputStream(filePath);
				String fileContent = "";
				InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
				BufferedReader buffreader = new BufferedReader(isr);
				String line;

				// read every line of the file into the line-variable, on line at the time
				while ((line = buffreader.readLine()) != null) {
					// do something with the settings from the file
					fileContent += line;
				}
				jsonArray = new JSONArray(fileContent);
				isr.close();
			}

		} catch (IOException | JSONException e) {
		}
		return jsonArray;
	}

	// [["TABLE1","bv834j22bjRqh9kh54EUrUXvxcAc+yNmY"],["TABLE2","bwR0SVLUk8wLKaMhy5ZRtl45T4rKS5KrJlrpZ4t"],["TABLE3",..."], ....]
	private JSONArray convertHashToJsonArray(LinkedHashMap<String, String> tablesCheckSum) {
		JSONArray jsonParameters = new JSONArray(); //should contains tables checksum

		for (String key : tablesCheckSum.keySet()) {
			JSONArray tableInfo = new JSONArray();
			tableInfo.put(key);
			tableInfo.put(tablesCheckSum.get(key));

			//Services.Log.debug("key: " + key + " value "+ tablesCheckSum.get(key) );

			jsonParameters.put(tableInfo);
		}

		Services.Log.debug(LogCategory.SYNCHRONIZATION, LOG_TAG, "jsonParameters" + jsonParameters.toString());

		return jsonParameters;
	}

	@Override
	public LinkedHashMap<String, String> convertJsonArraytoHash(JSONArray jsonParameters) {
		LinkedHashMap<String, String> tablesCheckSum = new LinkedHashMap<>(); //should contains tables checksum

		for (int index = 0; index < jsonParameters.length(); index++) {
			JSONArray tableinfo = jsonParameters.optJSONArray(index);
			if (tableinfo != null) {
				String tableName = tableinfo.optString(0);
				String tableChecksum = tableinfo.optString(1);

				tablesCheckSum.put(tableName, tableChecksum);

			}
		}

		return tablesCheckSum;
	}

	//Called inside a proc only
	@Override
	public short syncReceive() {
		//Call From a Gx Procedure, should close transaction first
		LocalUtils.commit();
		LocalUtils.endTransaction();

		Services.Log.debug(LogCategory.SYNCHRONIZATION, LOG_TAG, "callSynchronizer (Sync.Receive) from Procedure code ");
		int result = callSynchronizer(true, true);

		//Start proc transaction again
		LocalUtils.beginTransaction();

		return (short) result;
	}

	//Called inside a proc only
	@Override
	public short syncSend() {
		//Call From a Gx Procedure, should close transaction first
		LocalUtils.commit();
		LocalUtils.endTransaction();

		Services.Log.debug(LogCategory.SYNCHRONIZATION, LOG_TAG, "callOfflineReplicator (Sync.Send) from Procedure code");
		int result = SynchronizationSendHelper.callOfflineReplicator();

		//Start proc transaction again
		LocalUtils.beginTransaction();

		return (short) result;
	}

	//Called inside a proc only
	@Override
	public short syncStatus() {
		int result = callSynchronizerCheck();
		return (short) result;
	}

	//Called inside a procedure only
	@Override
	public short syncCheckPoint() {
		return (short) saveCheckPoint();
	}

	// create a check point in pending events.
	@Override
	public int saveCheckPoint() {
		//Only Save if Save Pending Events is enable.
		if (AndroidContext.ApplicationContext.getSynchronizerSavePendingEvents()) {
			SdtGxPendingEvent sdtTrn = new SdtGxPendingEvent(AndroidContext.ApplicationContext.getRemoteHandle());

			// set a new GUID for the table id
			sdtTrn.setgxTv_SdtGxPendingEvent_Pendingeventid(java.util.UUID.randomUUID());

			//now in UTC
			Date nowDate = getNowInUTC();
			sdtTrn.setgxTv_SdtGxPendingEvent_Pendingeventtimestamp(nowDate);

			//	all data in this pending event empty
			sdtTrn.setgxTv_SdtGxPendingEvent_Pendingeventbc(Strings.EMPTY);
			sdtTrn.setgxTv_SdtGxPendingEvent_Pendingeventaction((short) 1);  //just a valid action

			sdtTrn.setgxTv_SdtGxPendingEvent_Pendingeventdata(CHECKPOINT_DATA); //json
			sdtTrn.setgxTv_SdtGxPendingEvent_Pendingeventstatus(CHECKPOINT_STATUS); //check point
			sdtTrn.setgxTv_SdtGxPendingEvent_Pendingeventfiles(Strings.EMPTY); // array of files

			if (sdtTrn.getTransaction() != null) {
				// save event
				sdtTrn.getTransaction().Save();
				if (sdtTrn.Fail()) {
					// if fail just print error for now.
					Services.Log.error("Cannot save check point ");
					Services.Log.error(sdtTrn.getmessages().toString());
				}
			}
		}
		return 0;
	}

	@Override
	public void processBCBlobsBeforeSaved(String bcName, String data, String dataOlds, TreeMap<String, String> mapFilesToSend) {
		// replace \ for . , because in prolog modules are separeceted by \ and in FC metadata we are using .
		bcName = bcName.replace("\\", ".");
		StructureDefinition bcDef = mGenexusApplication.getDefinition().getBusinessComponent(bcName);
		if (bcDef != null) {
			List<DataItem> items = bcDef.getItems();
			for (DataItem def : items) {
				if (def.isMediaOrBlob()) {
					String attBlobName = def.getName();

					try {
						JSONObject dataObject = new JSONObject(dataOlds);
						String attBlobGXIValue = dataObject.optString(attBlobName + "_GXI", null);

						mapFilesToSend.put(attBlobName, attBlobGXIValue);

					} catch (json.org.json.JSONException e) {
					}
				}
			}
		} else {
			Services.Log.error(LogCategory.SYNCHRONIZATION, LOG_TAG, "Error in method processBCBlobsBeforeSaved, BC not found" + bcName);
		}
	}

	@Override
	public String replaceBCBlobsAfterSave(String bcName, String action, String data, String dataOlds, TreeMap<String, String> mapFilesToSend, json.org.json.JSONArray arrayFilesToSend) {
		// replace \ for . , because in prolog modules are separeceted by \ and in FC metadata we are using .
		bcName = bcName.replace("\\", ".");
		int arrayIndex = 0;
		StructureDefinition bcDef = mGenexusApplication.getDefinition().getBusinessComponent(bcName);
		if (bcDef != null) {
			List<DataItem> items = bcDef.getItems();
			for (DataItem def : items) {
				if (def.isMediaOrBlob()) {
					String attBlobName = def.getName();

					try {
						JSONObject dataObject = new JSONObject(data);
						String attBlobValue = dataObject.getString(attBlobName);

						//get the file from hashMap
						String attBlobPreviusGXIValue = mapFilesToSend.get(attBlobName);

						JSONObject dataObjectOlds = new JSONObject(dataOlds);
						String attBlobGXIValue = dataObjectOlds.optString(attBlobName + "_GXI", null);

						// not send always , only when change, if not change , don't send the image.
						if (attBlobGXIValue != null && attBlobPreviusGXIValue != null &&
								attBlobGXIValue.equalsIgnoreCase(attBlobPreviusGXIValue) &&
								action.equalsIgnoreCase("upd")) {
							// not sent image
							dataObject.remove(attBlobName);
							data = dataObject.toString();
						} else {
							Uri localFile = Uri.parse(attBlobValue);
							if (ContentResolver.SCHEME_FILE.equalsIgnoreCase(localFile.getScheme())) {
								attBlobValue = localFile.getPath();
							}

							File file = new File(attBlobValue);

							if (file.exists()) {
								// copy file to upload Directory.
								String filename = file.getName();
								String outputFile = AndroidContext.ApplicationContext.getFilesSubApplicationDirectory("upload") + "/" + filename;
								try {
									FileUtils.copyFile(file, new File(outputFile));
								} catch (IOException e) {
								}
								// add file to upload list.
								file = new File(outputFile);
								if (file.exists()) {
									//	replace path with placeholder.
									String valuePlaceHolder = String.format(SYNC_BLOB_PLACEHOLDER, String.valueOf(arrayIndex));
									dataObject.put(attBlobName, valuePlaceHolder);
									// use relative path for android M and up support
									String relativeOutputFile = "./" + filename;
									arrayFilesToSend.put(relativeOutputFile);
									arrayIndex++;
									data = dataObject.toString();
								}
							}
						}
					} catch (json.org.json.JSONException e) {
					}
				}
			}
			// if something get replaced return data modified
		} else {
			Services.Log.error(LogCategory.SYNCHRONIZATION, LOG_TAG, "Error in method replaceBCBlobsAfterSave, BC not found" + bcName);
		}
		return data;
	}

	@Override
	public void restorePendingToDatabase(EntityList paramEntityList) {
		@SuppressWarnings("rawtypes")
		Iterator localIterator = paramEntityList.iterator();
		while (true) {
			if (!localIterator.hasNext())
				return;
			Entity localEntity = (Entity) localIterator.next();
			SdtGxPendingEvent localSdtGxPendingEvent = new SdtGxPendingEvent(mGenexusApplication.getRemoteHandle());
			UUID localUUID = UUID.fromString(localEntity.optStringProperty("EventId"));
			localSdtGxPendingEvent.setgxTv_SdtGxPendingEvent_Pendingeventid(localUUID);
			localSdtGxPendingEvent.setgxTv_SdtGxPendingEvent_Pendingeventaction(Short.parseShort(localEntity.optStringProperty("EventAction")));
			localSdtGxPendingEvent.setgxTv_SdtGxPendingEvent_Pendingeventtimestamp(GXutil.charToTimeREST(localEntity.optStringProperty("EventTimestamp")));
			localSdtGxPendingEvent.setgxTv_SdtGxPendingEvent_Pendingeventbc(localEntity.optStringProperty("EventBC"));
			localSdtGxPendingEvent.setgxTv_SdtGxPendingEvent_Pendingeventdata(localEntity.optStringProperty("EventData"));
			localSdtGxPendingEvent.setgxTv_SdtGxPendingEvent_Pendingeventextras(localEntity.optStringProperty("EventExtras"));
			String status = localEntity.optStringProperty("EventStatus");
			localSdtGxPendingEvent.setgxTv_SdtGxPendingEvent_Pendingeventstatus(Short.parseShort(status));
			localSdtGxPendingEvent.setgxTv_SdtGxPendingEvent_Pendingeventerrors(localEntity.optStringProperty("EventErrors"));
			localSdtGxPendingEvent.setgxTv_SdtGxPendingEvent_Pendingeventfiles(localEntity.optStringProperty("EventFiles"));
			if (localSdtGxPendingEvent.getTransaction() == null)
				continue;
			try {
				LocalUtils.beginTransaction();
				localSdtGxPendingEvent.getTransaction().SetMode("INS");
				localSdtGxPendingEvent.getTransaction().Save();
				if (localSdtGxPendingEvent.success())
					LocalUtils.commit();
				Services.Log.debug(LogCategory.SYNCHRONIZATION, LOG_TAG, "Save sucessfully " + localUUID + " , " + status);
			} finally {
				LocalUtils.endTransaction();
			}
		}
	}

	private @NonNull Date getNowInUTC() {
		Date nowDate = new Date();
		long offset = TimeZone.getDefault().getOffset(nowDate.getTime());
		nowDate.setTime(nowDate.getTime() - offset);
		return nowDate;
	}

	@Override
	public void cleanExistingHashes() {
		cleanExistingHashes(null, null);
	}

	@Override
	public void cleanExistingHashes(String tempChecksum, String tempSync) {
		if (Strings.hasValue(tempChecksum))
			deleteFile(tempChecksum);
		else
			deleteFile(AndroidContext.ApplicationContext.getDataBaseSyncHashesFilePath());

		if (Strings.hasValue(tempSync))
			deleteFile(tempSync);
		else
			deleteFile(AndroidContext.ApplicationContext.getDataBaseSyncFilePath());
	}

	@Override
	public void cleanBlobs(String tempBlobsDirectory) {
		String originalDirectoryPath = AndroidContext.ApplicationContext.getFilesBlobsApplicationDirectory();
		if (Strings.hasValue(tempBlobsDirectory))
			deleteDir(tempBlobsDirectory);
		else if (Strings.hasValue(originalDirectoryPath))
			deleteDir(originalDirectoryPath);
	}
}
