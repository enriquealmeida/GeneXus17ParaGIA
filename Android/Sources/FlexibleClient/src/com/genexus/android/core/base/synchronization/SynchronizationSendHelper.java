package com.genexus.android.core.base.synchronization;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.sqldroid.SQLDroidDriver;

import com.artech.base.services.AndroidContext;
import com.genexus.android.core.base.application.IProcedure;
import com.genexus.android.core.base.application.OutputResult;
import com.genexus.android.core.base.metadata.StructureDataType;
import com.genexus.android.core.base.metadata.StructureDefinition;
import com.genexus.android.core.base.metadata.VariableDefinition;
import com.genexus.android.core.base.metadata.enums.Connectivity;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.model.EntityFactory;
import com.genexus.android.core.base.model.EntityList;
import com.genexus.android.core.base.model.PropertiesObject;
import com.genexus.android.core.base.providers.IApplicationServer;
import com.genexus.android.core.base.services.LogCategory;
import com.genexus.android.core.base.services.Services;
import com.artech.base.synchronization.bc.SdtGxPendingEvent;
import com.genexus.android.core.base.utils.ResultDetail;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.layers.LocalUtils;
import com.genexus.GXutil;

public class SynchronizationSendHelper
{
	private static final String LOG_TAG = "SyncSendHelper";

	public static boolean isRunningSendBackground = false;
	public static boolean isRunningSend = false;

	// SYNC send constants result START
	private static final int SYNC_SEND_OK = 0;
	private static final int SYNC_SEND_ERROROPENTRANSACTIONS = 1;
	private static final int SYNC_SEND_ERRORUNKNOWN = 2;
	private static final int SYNC_SEND_REPLICATORNOTFOUND = 3;

	private static final int SYNC_SEND_OKWITHERRORS_UPLOAD = 5;
	private static final int SYNC_SEND_OKWITHERRORS_SAVE = 6;
	private static final int SYNC_SEND_OKWITHERRORS_MAPPINGS = 7;
	private static final int SYNC_SEND_ERROR_ALREADYRUNNING = 8;
	// SYNC send constants result END

	public static int callOfflineReplicator()
	{
		int result = SYNC_SEND_OK;
		if (isRunningSend)
		{
			Services.Log.warning(LogCategory.SYNCHRONIZATION, LOG_TAG, "callOfflineReplicator not run because Send is already running");
			return SYNC_SEND_ERROR_ALREADYRUNNING;
		}
		SynchronizationHelper.isRunningSendOrReceive = true;
		isRunningSend = true;
		
		IProcedure procedure = Services.Application.getApplicationServer(Connectivity.Online).getProcedure("GeneXus.SD.Synchronization.OfflineEventReplicator");

		Services.Log.debug(LogCategory.SYNCHRONIZATION, LOG_TAG, "Call OfflineEventReplicator.");

		// check if has checkpoint
		EntityList pendings = Services.Sync.getPendingEventsList(SynchronizationHelper.CHECKPOINT_STATUS_STRING); // check point "51"
		if (pendings.size()>0)
		{
			Services.Log.debug(LogCategory.SYNCHRONIZATION, LOG_TAG, "Has Checkpoint, sent by batches.");
			// if has check point process by batches until finish and return the result at the end.

			// get all pending including check points.
			pendings = Services.Sync.getPendingEventsListWithCheckPoints(SynchronizationHelper.PENDING_STATUS_STRING); // Pending "1"

			// while ok and have more to send
			while ( (result == SYNC_SEND_OK || result == SYNC_SEND_OKWITHERRORS_SAVE || result == SYNC_SEND_OKWITHERRORS_MAPPINGS)
					&& pendings.size()>0)
			{

				// calculate batch to send
				ArrayList<String> checkpointToDelete = new ArrayList<String>();
				EntityList pendingsToSend = new EntityList();

				boolean startBatch = false;
				for (Entity pendingEventToSend : pendings)
				{
					// add element to pendingsToSend until next check point
					String pendingEventStatus = pendingEventToSend.getProperty("EventStatus").toString();
					Short pendingEventStatusShort = Short.parseShort(pendingEventStatus);

					if (pendingEventStatusShort==SynchronizationHelper.CHECKPOINT_STATUS)
					{
						// check point
						checkpointToDelete.add(pendingEventToSend.getProperty("EventId").toString());
						// already has this batch ready
						if (startBatch)
							break;
					}
					else
					{
						// real pending event.
						pendingsToSend.add(pendingEventToSend);
						startBatch= true;
					}
				}

				Services.Log.debug(LogCategory.SYNCHRONIZATION, LOG_TAG, "Process Batch send. Events. " + pendingsToSend.size() + " checkpoints " + checkpointToDelete.size());

				if (pendingsToSend.size()>0)
					result = processPendingEvents(procedure, pendingsToSend);

				// delete check point of this batch
				if ((result == SYNC_SEND_OK || result == SYNC_SEND_OKWITHERRORS_SAVE || result == SYNC_SEND_OKWITHERRORS_MAPPINGS))
				{
					for (String checkPointId : checkpointToDelete )
					{
						deletePendingEvent(checkPointId);
					}
				}

				// get all pending including check points again.
				pendings = Services.Sync.getPendingEventsListWithCheckPoints(SynchronizationHelper.PENDING_STATUS_STRING); // Pending
			}
		}
		else
		{
			pendings = Services.Sync.getPendingEventsList(SynchronizationHelper.PENDING_STATUS_STRING); // Pending
			result = processPendingEvents(procedure, pendings);
		}

		Services.Log.debug(LogCategory.SYNCHRONIZATION, LOG_TAG, "End Call ProcedureReplicator.");
		SynchronizationHelper.isRunningSendOrReceive = false;
		isRunningSend = false;
		return result;
	}

	public static int processPendingEvents(IProcedure procedure, EntityList pendings )
	{
		PropertiesObject parameter = new PropertiesObject();
		int result = SYNC_SEND_OK;
		Services.Log.debug(LogCategory.SYNCHRONIZATION, LOG_TAG, "OfflineEventReplicator sending " + pendings.size() + " events.");

		if (pendings.size()>0)
		{
			//StructureDefinition defSdt = pendings.get(0).getDefinition();
			Services.Log.debug(LogCategory.SYNCHRONIZATION, LOG_TAG, "Call ProcedureReplicator. input: " + String.valueOf(pendings.size()));

			// Upload local blobs to server if has any
			// and modified the pending to add blobs file send to server.
			for (Entity pendingEventToSend : pendings)
			{
				uploadFilesFromPendingEvents(pendingEventToSend);
			}

			parameter.setProperty("GxPendingEvents", pendings);

			// Add GxAppVersion parameter to sync send
			String versionMetadata = Services.Application.get().getMajorVersion() + "." + Services.Application.get().getMinorVersion();

			// Set GxSynchroInfo parameter
			Entity myEntityParm = EntityFactory.newSdt("GeneXus.SD.Synchronization.SynchronizationInfo");

			myEntityParm.setProperty("GxAppVersion", versionMetadata);
			myEntityParm.setProperty("Synchronizer", Services.Application.get().getSynchronizer());

			parameter.setProperty("GxSyncroInfo", myEntityParm);

			// Log pending events to send
			//Services.Log.debug(LogCategory.SYNCHRONIZATION, LOG_TAG, "OfflineEventReplicator sending Json: " + pendings.toString() + " .");

			OutputResult procResult = procedure.executeReplicator(parameter);

			if (procResult.isOk())
			{
				//set last time send
				Services.Sync.setSendLastTime(new Date().getTime());

				//Services.Log.debug(LogCategory.SYNCHRONIZATION, LOG_TAG, "GxPendingEvents result: " + parameter.toString());

				// create a temp var to desearialize json result, cannot start with gx.
				StructureDataType defStructureOutput = Services.Application.get().getDefinition().getSDT("GeneXus.SD.Synchronization.SynchronizationEventResultList");
				if (defStructureOutput==null)
				{
					Services.Log.error(LogCategory.SYNCHRONIZATION, LOG_TAG, "Error calling ProcedureReplicator , output SDT not found");
					isRunningSendBackground = false;
					SynchronizationHelper.isRunningSendOrReceive = false;
					isRunningSend = false;
					return SYNC_SEND_REPLICATORNOTFOUND;
				}

				StructureDefinition defSdtOutput = defStructureOutput.getStructure();
				VariableDefinition varSdtOutput = new VariableDefinition("PedingsEvents", true, defSdtOutput);
				StructureDefinition localVars = new StructureDefinition(Strings.EMPTY);
				localVars.Root.Items.add(varSdtOutput);

				Entity localResult = EntityFactory.newEntity(localVars);

				//localResult.load(new NodeObject(jsonBC));

				localResult.setProperty(varSdtOutput.getName(), parameter.getProperty("EventResults"));

				// process proc output to GxPending Events.
				EntityList results = (EntityList)localResult.getProperty(varSdtOutput.getName());
				EntityList listMappingsWithErrors = new EntityList();

				for (Entity pendingEvent : results)
				{

					String pendingId = pendingEvent.getProperty("EventId").toString();
					UUID pendingEventId = GXutil.strToGuid(pendingId);

					SdtGxPendingEvent sdtTrn = new SdtGxPendingEvent(Services.Application.get().getRemoteHandle());

					LocalUtils.beginTransaction();

					try
					{
						sdtTrn.Load(pendingEventId);

						if (sdtTrn.Success())
						{
							//update pending event with status and error message.
							String pendingEventStatus = pendingEvent.getProperty("EventStatus").toString();
							Short pendingEventStatusShort = Short.parseShort(pendingEventStatus);
							sdtTrn.setgxTv_SdtGxPendingEvent_Pendingeventstatus(pendingEventStatusShort); //status

							String pendingEventErrors = pendingEvent.getProperty("EventErrors").toString();
							sdtTrn.setgxTv_SdtGxPendingEvent_Pendingeventerrors(pendingEventErrors); //errors

							if (sdtTrn.getTransaction()!=null)
							{
								// TODO Apply mapping from  pendingEvent results.
								EntityList listMappings = pendingEvent.getLevel("Mappings");

								if (listMappings!=null && listMappings.size()>0)
								{
									applyMappings(listMappings, listMappingsWithErrors);
									//if (!applyMappings(listMappings))
									//	result = SYNC_SEND_OKWITHERRORS_MAPPINGS;
								}

								if (pendingEventStatusShort == 3) // Server sucessfully, delete it after apply mapping.
									sdtTrn.getTransaction().SetMode("DLT");

								// save event
								sdtTrn.getTransaction().Save();
								if (sdtTrn.success())
								{
									LocalUtils.commit();

									deleteFilesIfNecessary(sdtTrn, pendingEventStatusShort);
								}
								else
								{
									Services.Log.error(LogCategory.SYNCHRONIZATION, LOG_TAG, "Save failed, not commit " + sdtTrn.getTransaction().GetMessages().toString());
									//if (sdtTrn.getTransaction().GetMessages().size()>0)
									//	Services.Log.Error("ProcedureReplicator" , "Save failed, message " + sdtTrn.getTransaction().GetMessages().get(0).toString());
								}
								Services.Log.debug(LogCategory.SYNCHRONIZATION, LOG_TAG, "Save sucessfully " + pendingId + " , " + pendingEventStatus + " , " + pendingEventErrors );
							}
						}
						else
						{
							Services.Log.error(LogCategory.SYNCHRONIZATION, LOG_TAG, "Result not found" + pendingEvent.toString());
						}
					}
					finally
					{
						LocalUtils.endTransaction();
					}

					//end for
				}

				if (listMappingsWithErrors.size()>0)
				{
					try
					{
						LocalUtils.beginTransaction();

						EntityList listFinalMappingsWithErrors = new EntityList();
						// 	apply failing mappings if possible.
						if (!applyMappings(listMappingsWithErrors, listFinalMappingsWithErrors))
						{
							Services.Log.error(LogCategory.SYNCHRONIZATION, LOG_TAG, "Error applying some mappings " + listFinalMappingsWithErrors.size());
							result = SYNC_SEND_OKWITHERRORS_MAPPINGS;
						}
						else
						{
							Services.Log.debug(LogCategory.SYNCHRONIZATION, LOG_TAG, "Applying All mappings ok");
						}
						LocalUtils.commit();
					}
					finally
					{
						SynchronizationHelper.isRunningSendOrReceive = false;
						isRunningSend = false;
						LocalUtils.endTransaction();
					}
				}


			}
			else
			{
				Services.Log.error(LogCategory.SYNCHRONIZATION, LOG_TAG, "Error calling ProcedureReplicator " + procResult.getErrorText());
				result = SYNC_SEND_REPLICATORNOTFOUND;
			}
		}
		return result;
	}


	private static void uploadFilesFromPendingEvents(Entity pendingEventToSend) 
	{
		String eventFiles = pendingEventToSend.getProperty("EventFiles").toString();
		// if has files to send.
		if (Services.Strings.hasValue(eventFiles))
		{
			try {
				JSONArray eventFilesArray = new JSONArray(eventFiles);
				if (eventFilesArray.length()>0)
				{
					String eventData = pendingEventToSend.getProperty("EventData").toString();

					for(int arrayIndex = 0; arrayIndex < eventFilesArray.length(); arrayIndex++)
					{
						Services.Log.debug(LogCategory.SYNCHRONIZATION, LOG_TAG, "Uploading blob : " + String.valueOf(arrayIndex + 1) + " of " + String.valueOf(eventFilesArray.length()));

						String fileToSendPath = eventFilesArray.getString(arrayIndex);
						// Convert relative path to absolute
						if (fileToSendPath.startsWith("./"))
						{
							//relative path in android file system.
							String blobBasePath = AndroidContext.ApplicationContext.getFilesSubApplicationDirectory("upload");
							// Local path in sdcard.
							fileToSendPath = blobBasePath + fileToSendPath.substring(1);
						}

						IApplicationServer serverApp = Services.Application.getApplicationServer(Connectivity.Online);
						File file = new File(fileToSendPath);

						ResultDetail<String> uploadResult = serverApp.uploadBinary(file, Services.Application.get().getSynchronizer(),null);

						String valuePlaceHolder = String.format(SynchronizationHelper.SYNC_BLOB_PLACEHOLDER, String.valueOf(arrayIndex));

						if (uploadResult.getResult())
						{
							String binaryToken = uploadResult.getData();
							eventData = eventData.replace(valuePlaceHolder, binaryToken);
						}
						else
						{
							// TODO: Shouldn't we abort the sync in this case?
							Services.Log.error(LogCategory.SYNCHRONIZATION, LOG_TAG, "Error uploading binary file! " + uploadResult.getMessage());
						}
					}

					pendingEventToSend.setProperty("EventData", eventData);
				}
			}
			catch (JSONException | IOException e)
			{
			}
		}
	}

	private static void deleteFilesIfNecessary(SdtGxPendingEvent sdtTrn,
			Short pendingEventStatusShort) 
	{
		// remove successfully upload files. if pendingEventStatusShort is 3 and has files.
		if (pendingEventStatusShort == 3)
		{
			// get the files
			String eventFiles = sdtTrn.getgxTv_SdtGxPendingEvent_Pendingeventfiles();
			
			// if has files to send.
			if (Services.Strings.hasValue(eventFiles))
			{
				try 
				{
					JSONArray eventFilesArray = new JSONArray(eventFiles);
					if (eventFilesArray.length()>0)
					{
						for(int arrayIndex = 0; arrayIndex < eventFilesArray.length(); arrayIndex++)
						{
							String fileToSendPath = eventFilesArray.getString(arrayIndex);
							// Convert relative path to absolute
							if (fileToSendPath!=null && fileToSendPath.startsWith("./"))
							{
								//relative path in android file system.
								String blobBasePath = AndroidContext.ApplicationContext.getFilesSubApplicationDirectory("upload");
								// Local path in sdcard.
								fileToSendPath = blobBasePath + fileToSendPath.substring(1);
							}
							// Delete file
							File file = new File(fileToSendPath);
							if (file.exists())
							{
								file.delete();
							}
						}
					}
				} catch (JSONException e) {
				}
			}	
		}
	}

	private static boolean applyMappings(EntityList listMappings, EntityList listMappingsWithErrors)
	{
		int mappingsCount = listMappings.size();
		EntityList listMappingsFail = new EntityList();
		
		Boolean continueApplyingMappings = true;
		
		while (continueApplyingMappings)
		{
			//each entity is a mapping.
			for (Entity mapping : listMappings)
			{
				String conditions = mapping.optStringProperty("Conditions");
				String table = mapping.optStringProperty("Table");
				String updates = mapping.optStringProperty("Updates");

				if (!Services.Strings.hasValue(table))
				{
					Services.Log.error(LogCategory.SYNCHRONIZATION, LOG_TAG, "table name must be of type string.");
					break;
				}

				JSONArray conditionsArray;
				try {
					conditionsArray = new JSONArray(conditions);
				} catch (JSONException e) {
					Services.Log.error(LogCategory.SYNCHRONIZATION, LOG_TAG, "conditions must be of type array.");
					break;
				}

				JSONArray updatesArray;
				try {
					updatesArray = new JSONArray(updates);
				} catch (JSONException e) {
					Services.Log.error(LogCategory.SYNCHRONIZATION, LOG_TAG, "updates must be of type array.");
					break;
				}

				StringBuilder sqlSent = new StringBuilder();
				sqlSent.append("UPDATE [");

				sqlSent.append(table);
				sqlSent.append("] SET ");

				ArrayList<String> stringParametersValues = new ArrayList<>();
			
				if (!appendFromArray(sqlSent, updatesArray, ", ", stringParametersValues))
					break;

				sqlSent.append(" WHERE ");

				if (!appendFromArray(sqlSent, conditionsArray, " AND ", stringParametersValues))
					break;

				String sqlSentToExecute = sqlSent.toString();

				//	execute mapping statements.
				Services.Log.debug(LogCategory.SYNCHRONIZATION, LOG_TAG, "execute mapping:" + sqlSentToExecute);

				try {
					PreparedStatement statement = SQLDroidDriver.getCurrentConnection().prepareStatement(sqlSentToExecute);
					//statement parameters.
					int parIndex = 1;
					for(String parameterValue : stringParametersValues)
					{
						//Services.Log.debug(LogCategory.SYNCHRONIZATION, LOG_TAG, "parameter : " + parIndex + " " + parameterValue);
						statement.setString(parIndex, parameterValue);
						parIndex++;
					}
					//statement execute.
					statement.execute();
				} catch (SQLException e) {
					Services.Log.error(LogCategory.SYNCHRONIZATION, LOG_TAG, "Error applyng mappings " + e.getMessage() + " " + sqlSentToExecute);
					listMappingsFail.add(mapping);
				}

			}
		
			int mappingsFailCount = listMappingsFail.size();
			if (mappingsFailCount > 0)
			{
				if (mappingsFailCount < mappingsCount)
				{
					Services.Log.debug(LogCategory.SYNCHRONIZATION, LOG_TAG, "Fail applyng mappings count: " + mappingsFailCount + " of " + mappingsCount + " retrying");
					listMappings = listMappingsFail;
					mappingsCount = listMappings.size();
					listMappingsFail = new EntityList();
					// continue applying mappings
					continueApplyingMappings = true;
				}
				else
				{
					//return the mappings that fails to process later, could not be processed here.
					Services.Log.debug(LogCategory.SYNCHRONIZATION, LOG_TAG, "Fail to process mappings: " + mappingsFailCount + " of " + mappingsCount + " ");
					continueApplyingMappings = false;
					for (Entity mapping : listMappingsFail)
					{
						listMappingsWithErrors.add(mapping);
					}
					return false;
				}
			}
			else
			{
				continueApplyingMappings = false;
			}
		}
		return true;
	}

	private static boolean appendFromArray(StringBuilder sqlSent, JSONArray attsArray, String separator, ArrayList<String> stringParametersValues)
	{
		for (int i = 0; i < attsArray.length(); i++) {
		    JSONObject row;
			try {
				row = attsArray.getJSONObject(i);
			    String sKey = row.getString("Key");
			    String sValue = row.getString("Value");

			    if (i>0)
			    	sqlSent.append(separator);

			    // value in '' 
			    sqlSent.append("[").append(sKey).append("] = ?");
			    stringParametersValues.add(sValue);
			} catch (JSONException e) {
				Services.Log.error(LogCategory.SYNCHRONIZATION, LOG_TAG, "items must have a Key-Value pair.");
				return false;
			}
		}
		return true;
	}

	private static void deletePendingEvent(String pendingId)
	{
		UUID pendingEventId = GXutil.strToGuid(pendingId);

		SdtGxPendingEvent sdtTrn = new SdtGxPendingEvent(Services.Application.get().getRemoteHandle());

		LocalUtils.beginTransaction();

		try
		{
			sdtTrn.Load(pendingEventId);

			if (sdtTrn.Success())
			{
				//delete pending event .
				if (sdtTrn.getTransaction()!=null)
				{
					sdtTrn.getTransaction().SetMode("DLT");

					// save event
					sdtTrn.getTransaction().Save();
					if (sdtTrn.success())
					{
						LocalUtils.commit();
					}
					else
					{
						Services.Log.error(LogCategory.SYNCHRONIZATION, LOG_TAG, "Delete pending failed, not commit " + sdtTrn.getTransaction().GetMessages().toString());
					}
					Services.Log.debug(LogCategory.SYNCHRONIZATION, LOG_TAG, "Delete sucessfully " + pendingId );
				}
			}
			else
			{
				Services.Log.error(LogCategory.SYNCHRONIZATION, LOG_TAG, "Pending event not found" + pendingId);
			}
		}
		finally
		{
			LocalUtils.endTransaction();
		}
	}
}
