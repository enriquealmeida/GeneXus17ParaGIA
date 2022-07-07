package com.genexus.android.core.base.metadata.loader;

import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;

import com.artech.base.services.AndroidContext;
import com.genexus.android.core.base.metadata.GenexusApplication;
import com.genexus.android.core.base.model.EntityList;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.synchronization.SynchronizationAlarmReceiver;
import com.genexus.android.core.base.synchronization.SynchronizationHelper;
import com.genexus.android.core.layers.GxObjectFactory;
import com.genexus.android.core.layers.LocalUtils;
import com.genexus.Application;
import com.genexus.ApplicationContext;
import com.genexus.ClientContext;
import com.genexus.GXReorganization;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.sqldroid.SQLDroidDriver;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;

import static com.genexus.android.core.common.FileHelper.renameToTemporal;
import static com.genexus.android.core.common.FileHelper.undoTemporalRename;

public class SyncManager {
	private static final String REOR_VER_STAMP = "reor_ver_stamp";
	private static final String REOR_MD5_HASH = "reor_md5_hash";

	public interface Listener {
		void onSyncStarted();
		void onSyncFinished(boolean failed, boolean delayed);
	}

	public static boolean createSyncDatabase(GenexusApplication application) {
		// Create only if database file not exists.
		String filePath = AndroidContext.ApplicationContext.getDataBaseFilePath();
		File file = new File(filePath);

		//Time Stamp
		String reorTimeStamp = Application.getClientContext().getClientPreferences().getREORG_TIME_STAMP();
		String currentDBreorTimeStamp = Services.Preferences.getString(REOR_VER_STAMP);

		//Md5 File
		String reorMd5Hash = application.getReorMD5Hash();
		String currentDBreorMd5Hash = Services.Preferences.getString(REOR_MD5_HASH);

		Services.Log.debug("Reor Time Stamp: " + reorTimeStamp + " DB Time Stamp: " + currentDBreorTimeStamp);
		Services.Log.debug("Reor MD5 Hash: " + reorMd5Hash + " DB MD5 Hash: " + currentDBreorMd5Hash);

		//if not exist,
		// or exits an is an old version and a different reor.
		if (!file.exists()
				|| (Services.Strings.hasValue(reorMd5Hash) && !reorMd5Hash.equalsIgnoreCase(currentDBreorMd5Hash))) {
			Services.Log.debug("Create database in: " + file.getAbsolutePath());
			Services.Log.debug("Reor Time Stamp: " + reorTimeStamp + " DB Time Stamp: " + currentDBreorTimeStamp);
			Services.Log.debug("Reor MD5 Hash: " + reorMd5Hash + " DB MD5 Hash: " + currentDBreorMd5Hash);

			EntityList pendingsEventsInDb = null;

			//if file exits
			if (file.exists()) {
				Services.Log.debug("Creating new database, create backup of old database: " + file.getAbsolutePath() + ".backup");
				try {
					FileUtils.copyFile(file, new File(file.getAbsolutePath() + ".backup"));
				} catch (IOException e) {
					Services.Log.debug("Error backing up database.", e);
				}

				// keep pendings events if exists to restore after create.
				pendingsEventsInDb = Services.Sync.getPendingEventsList("0"); // All
			}

			// create via reorganization or copy from raw if exist.
			boolean[] ret = createDatabaseOrCopyFromRaw(application, file);
			boolean executeReor = ret[0];
			boolean failed = ret[1];

			if (failed)
				return false;

			// Set autocommit in false again, if properties say that.
			if (pendingsEventsInDb != null && executeReor) {
				// drop before connection because it not useful any more, use a new one with correct auto commit mode
				int remoteHandle = Application.getNewRemoteHandle(ClientContext.getModelContext());
				//set this handle as app handle, store in as int in App. Use in all reflection calls
				application.setRemoteHandle(remoteHandle);
			}

			if (pendingsEventsInDb != null && pendingsEventsInDb.size() > 0) {
				// restore pending events saved before create database.
				Services.Log.debug("Restore previous pending events to new db");
				Services.Sync.restorePendingToDatabase(pendingsEventsInDb);
			}

			// reor sucessfully, save time stamp.
			Services.Preferences.setString(REOR_VER_STAMP, reorTimeStamp);
			Services.Preferences.setString(REOR_MD5_HASH, reorMd5Hash);

			if (executeReor) {
				Services.Log.debug("clean existing hashes after db create ");
				Services.Sync.cleanExistingHashes();
			}
			Services.Log.debug("set last sync time to empty after db create or copy ");
			//set last sync time to empty.
			Services.Sync.setSyncLastTime(0);
		} else {
			//if file exits, check if database has Pending Events last version.
			if (file.exists()) {
				Services.Log.debug("Check PendingEvents table in database : " + file.getAbsolutePath());

				try {
					// begin connection if not exists at app startup
					Services.Sync.getPendingEventsList(SynchronizationHelper.PENDING_STATUS_STRING); // Pending
				} catch (@SuppressWarnings("checkstyle:IllegalCatch") Exception e) {
					// TODO: We should investigate why and which exception we're catching here
					// ignore error if PendingEventFiles att not exits.
				}

				//check for att
				String sqlSentToExecute = "PRAGMA table_info(GXPendingEvent)";

				PreparedStatement statement;
				try {
					LocalUtils.beginTransaction();

					statement = SQLDroidDriver.getCurrentConnection().prepareStatement(sqlSentToExecute);
					Services.Log.debug("Check PendingEvents Table atts.");

					boolean hasFilesToSendAttribute = false;
					ResultSet resultSet = statement.executeQuery();
					while (resultSet.next()) {
						String data = resultSet.getString("name");
						hasFilesToSendAttribute = data.equalsIgnoreCase("PendingEventFiles");
						if (hasFilesToSendAttribute)
							break;
					}
					statement.close();

					//add it if necessary
					if (!hasFilesToSendAttribute) {
						Services.Log.debug("Add PendingEventFiles to PendingEvents Table.");
						sqlSentToExecute = "ALTER TABLE [GXPendingEvent] ADD COLUMN [PendingEventFiles] TEXT NOT NULL DEFAULT ''";
						statement = SQLDroidDriver.getCurrentConnection().prepareStatement(sqlSentToExecute);
						statement.execute();
						statement.close();
						LocalUtils.commit();
					}

				} catch (SQLException e) {
				} finally {
					LocalUtils.endTransaction();
				}
				//end
			}
		}

		Services.Log.debug("Using database : " + file.getAbsolutePath());

		return true;
	}

	public static void resetSyncDatabase(GenexusApplication application) {
		String filePath = AndroidContext.ApplicationContext.getDataBaseFilePath();
		File databaseFile = new File(filePath);

		String renamedBlobsDir = renameToTemporal(AndroidContext.ApplicationContext.getFilesBlobsApplicationDirectory());
		String renamedCheckSum = renameToTemporal(AndroidContext.ApplicationContext.getDataBaseSyncHashesFilePath());
		String renamedSync = renameToTemporal(AndroidContext.ApplicationContext.getDataBaseSyncFilePath());

		boolean[] ret = createDatabaseOrCopyFromRaw(application, databaseFile);
		boolean executeReor = ret[0];
		boolean failed = ret[1];

		if (executeReor && !failed) {
			Services.Sync.cleanExistingHashes(renamedCheckSum, renamedSync);
			Services.Sync.cleanBlobs(renamedBlobsDir);

			// Set autocommit in false again, if properties say that.
			// get a new connection for the new created database file.
			// drop before connection because it not useful any more, use a new one with correct auto commit mode
			int remoteHandle = Application.getNewRemoteHandle(ClientContext.getModelContext());
			//set this handle as app handle, store in as int in App. Use in all reflection calls
			application.setRemoteHandle(remoteHandle);
		} else {
			undoTemporalRename(renamedBlobsDir);
			undoTemporalRename(renamedCheckSum);
			undoTemporalRename(renamedSync);
		}
	}

	public static void syncData(GenexusApplication application, SyncManager.Listener listener) {
		//must be done after metadata is loaded and the version if the correct one.
		// should run onbackground.
		boolean shouldRunSync = isShouldRunSync(application);
		boolean hasSyncAtStartup = hasSyncAtStartup(application);

		//Run Sync if automatic, or elapsed time and not custom procedure.
		if (application.isOfflineApplication()
				&& 	hasSyncAtStartup && shouldRunSync) {

			listener.onSyncStarted();

			Services.Log.debug("callSynchronizer (Sync.Receive) from Application load ");
			int syncResult = Services.Sync.callSynchronizer(true, false);

			// Only if result is 2 , restore dabase and reintent.
			if (syncResult == SynchronizationHelper.SYNC_FAIL_SERVERREINSTALL) {
				// Is empty and error is 2 we should re install preload database.
				// restore database and initial hashed.
				//Copy Database from raw
				String filePath = AndroidContext.ApplicationContext.getDataBaseFilePath();
				File file = new File(filePath);
				if (copyDatabaseFromRaw(application, file)) {
					Services.Log.debug("Synchronizer failed , retry with app initial data.");
					// App to an initial state, re try sync
					syncResult = Services.Sync.callSynchronizer(true, false);
				} else {
					//Empty hashes.
					Services.Log.debug("Synchronizer failed , retry without local tables hashes.");
					// call sync again, with no hashes.
					syncResult = Services.Sync.callSynchronizer(false, false);
				}
			}

			boolean failed = syncResult != SynchronizationHelper.SYNC_OK;

			listener.onSyncFinished(failed, true);
		}

		if (application.isOfflineApplication() && application.getSynchronizerReceiveAfterElapsedTime()) {
			//if offline , sync auto and sync by time (After Elapsed Time)
			//Schedule Sync Receiver.
			SynchronizationAlarmReceiver alarm = new SynchronizationAlarmReceiver();
			alarm.setAlarm(Services.Application.getAppContext());
			Services.Log.debug("set sync alarm after elapsed time");
		}
	}

	public static boolean isShouldRunSync(GenexusApplication application) {
		// calculate time dif
		long minTimeBetweenSync = application.getSynchronizerMinTimeBetweenSync();
		long nowTime = new Date().getTime();
		long lastSync = Services.Sync.getSyncLastTime();
		boolean shouldRunSync = true;
		// minTimeBetweenSync in seconds
		if (lastSync != 0 && ((nowTime - lastSync) < (minTimeBetweenSync * 1000))) {
			shouldRunSync = false;
			if (application.isOfflineApplication())
				Services.Log.debug("MinTimeBetweenSync time not happened yet.");
		}
		return shouldRunSync;
	}

	public static boolean hasSyncAtStartup(GenexusApplication application) {
		if ( application.getRunSynchronizerAtStartup() ||
			(application.getSynchronizerReceiveAfterElapsedTime() && !Services.Strings.hasValue(application.getSynchronizerReceiveCustomProcedure()))
		)
			return true;
		return false;
	}

	private static boolean[] createDatabaseOrCopyFromRaw(GenexusApplication application, File file) {
		boolean executeReor = false;
		//Copy Database from raw
		if (copyDatabaseFromRaw(application, file)) {
			Services.Log.debug("End Copy file with database from raw : " + file.getAbsolutePath());
			//TODO: remove all files from blobs files directory?
		} else {
			Services.Log.debug("Running reor to create database in: " + file.getAbsolutePath());
			executeReor = true;
			// 	create via reorganization.
			GXReorganization reor = GxObjectFactory.getReorganization();
			if (reor != null) {
				reor.execute(); // Can be null if application does not use tables (rare, but possible).

				Services.Log.debug("Creating event table in database : " + file.getAbsolutePath());
				Services.Sync.callReorCreatePendingEvents(false);

				//TODO: remove all files from blobs files directory?
			} else {
				// if reor is null and App has BCs show an error message.
				if (Services.Application.getDefinition().hasBusinessComponents()) {
					Services.Log.error("Database creation failed: could not find Reorganization programs");
					return new boolean[]{executeReor, true};
				}
				//Only execute reor for pending events, just for apps with no tables.
				Services.Log.debug("Creating only event table in database : " + file.getAbsolutePath());
				Services.Sync.callReorCreatePendingEvents(true);
			}
		}

		//reorganization mode to off., next connection will use correct autocommit mode.
		ApplicationContext.getInstance().setReorganization(false);
		return new boolean[]{executeReor, false};
	}

	private static boolean copyDatabaseFromRaw(GenexusApplication application, File file) {
		InputStream is = AndroidContext.ApplicationContext.getResourceStream(application.getName() + "_sqlite", "raw");
		boolean hasDropOldDatabase = false;

		String appEntryForRaw = Services.Application.get().getAppEntry().toLowerCase(Locale.US).replace(".", "_");

		if (is == null)
			is = AndroidContext.ApplicationContext.getResourceStream( appEntryForRaw + "_sqlite", "raw");

		if (is != null) {
			// Close Current Connection to Database
			// if a connection to DB exists, drop it. it not useful any more

			Services.Log.debug("Close current connection to DB : " + file.getAbsolutePath());
			if (SQLDroidDriver.getCurrentConnection() != null) {
				closeDBConnection(file);

				// erase database files, include journal
				if (SQLiteDatabase.deleteDatabase(file))
					Services.Log.debug("Delete old database files ");
				else
					Services.Log.debug("Delete old database files failed ");

				// drop before connection because it not useful any more, use a new one with correct auto commit mode
				int remoteHandle = Application.getNewRemoteHandle(ClientContext.getModelContext());
				//set this handle as app handle, store in as int in App. Use in all reflection calls
				Services.Application.get().setRemoteHandle(remoteHandle);

				hasDropOldDatabase = true;
			}

			Services.Log.debug("Copy file with data database from raw to: " + file.getAbsolutePath());
			try {
				FileUtils.copyInputStreamToFile(is, file);
			} catch (IOException e) {
				Services.Log.error(e);
			}

			//if checksum files exist, copy to android and insert them.
			String fileCheckSumPath = AndroidContext.ApplicationContext.getDataBaseSyncHashesFilePath();
			File fileCheckSum = new File(fileCheckSumPath);
			is = AndroidContext.ApplicationContext.getResourceStream(appEntryForRaw + "_hashes", "raw");
			if (is != null) {
				Services.Log.debug("Copy file with checksum database from raw to: " + fileCheckSum.getAbsolutePath());
				try {
					FileUtils.copyInputStreamToFile(is, fileCheckSum);
				} catch (IOException e) {
					Services.Log.error(e);
				}

				// read file and copy to check sum file
				// using readJsonFromDisk, convertJsonArraytoHash , storeHashMapOnDisk
				JSONArray array = Services.Sync.readJsonArrayFromDisk();
				LinkedHashMap<String, String> hash = Services.Sync.convertJsonArraytoHash(array);
				Services.Sync.storeHashMapOnDisk(hash);

				// not set last sync time to now, could be a sync after pre load database
				//SynchronizationHelper.setSyncLastTime(new Date().getTime());

				//copy all blobs from assets/blobs to internal storage.
				AssetManager assetManager = Services.Application.getAppContext().getAssets();
				try {
					String[] files = assetManager.list("blobs");
					for (String filename : files) {
						InputStream in = assetManager.open("blobs/" + filename);
						String outputFile = AndroidContext.ApplicationContext.getFilesBlobsApplicationDirectory() + "/" + filename;
						File fileFromAssets = new File(outputFile);
						FileUtils.copyInputStreamToFile(in, fileFromAssets);
					}
				} catch (IOException e) {
					Services.Log.warning("createSyncDatabase", "cannot get assets files " + e.getMessage());
				}
			}

			if (hasDropOldDatabase) {
				// begin connection to new database at app startup
				Services.Log.debug("begin connection to DB at startup : " + file.getAbsolutePath());
				Services.Sync.getPendingEventsList(SynchronizationHelper.PENDING_STATUS_STRING); // Pending
			}

			return true;
		}
		return false;
	}

	public static boolean closeDBConnection(File file)
	{
		if (SQLDroidDriver.getCurrentConnection() != null) {
			try {
				Services.Log.debug("Closing found connection to DB : " + file.getAbsolutePath());
				SQLDroidDriver.getCurrentConnection().close();
				// remove reference to unusable connection.
				SQLDroidDriver.setCurrentConnection(null);
				return true;
			}
			catch (SQLException ex) {
				Services.Log.debug("exception closing connection, reason: " + ex.getMessage());
				return false;
			}
		}
		return true;
	}
}
