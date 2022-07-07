package com.genexus.android.core.externalobjects;

import androidx.annotation.NonNull;

import com.genexus.android.core.actions.ApiAction;
import com.genexus.android.core.base.metadata.loader.SyncManager;
import com.artech.base.services.AndroidContext;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.synchronization.SynchronizationHelper;
import com.genexus.android.core.common.FileHelper;
import com.genexus.android.core.common.ZipHelper;
import com.genexus.android.core.externalapi.ExternalApiResult;
import com.genexus.android.core.externalapi.superapps.SuperAppExternalApi;
import com.genexus.Application;
import com.genexus.ClientContext;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class DataBaseAPI extends SuperAppExternalApi {

    // GeneXus API Object Name
    public static final String OBJECT_NAME = "GeneXus.SD.Offline.DataBase";

    // API Method Names
    private static final String METHOD_BACKUP = "backup";
    private static final String METHOD_RESTORE = "restore";

	// return result constants.
	private static final short RESULT_OK = 0;
	private static final short RESULT_APP_NOT_OFFLINE = 1;
	private static final short RESULT_PATH_INCORRECT = 2;
	private static final short RESULT_DB_MD5_NOTMATCH = 3;
	public static final short RESULT_INTERNAL_ERROR = 9;

	private static final String TMP_DIRECTORY = "tmp";
	private static final String BLOB_ZIP_FILE = "blobs.zip";

	public DataBaseAPI(ApiAction action) {
        super(action);
        addMethodHandler(METHOD_BACKUP, 1, mBackupDB);
        addMethodHandler(METHOD_RESTORE, 1, mRestoreDB);
    }

    @SuppressWarnings("FieldCanBeLocal")
    private final IMethodInvoker mBackupDB = new IMethodInvoker() {
        @Override
        public @NonNull ExternalApiResult invoke(List<Object> parameters) {
            final String parValue = (String) parameters.get(0);
			short result = backupAllDB(parValue);
            return ExternalApiResult.success(result) ;
        }
    };

    @SuppressWarnings("FieldCanBeLocal")
    private final IMethodInvoker mRestoreDB = new IMethodInvoker() {
        @Override
        public @NonNull ExternalApiResult invoke(List<Object> parameters) {
            final String parValue = (String) parameters.get(0);
            short result = restoreAllDB(parValue);
            return ExternalApiResult.success(result);
        }
    };


    public static short backupAllDB(String path) {
        Services.Log.debug(OBJECT_NAME, "backup db");

		if (!Services.Application.get().isOfflineApplication())
			return RESULT_APP_NOT_OFFLINE;

		if (!Services.Strings.hasValue(path))
		{
			Services.Log.debug(OBJECT_NAME, "path is null or empty");
			return RESULT_PATH_INCORRECT;
		}

		File dbBackup = new File(path);

		String dirBackup = dbBackup.getParent();
        if (dirBackup==null)
        	return RESULT_PATH_INCORRECT;

        ZipHelper.dirCheckOrCreate(dirBackup);

		// Create temp dir
		String dirBackupTemp = getBackupTmpDir(dbBackup, dirBackup);

		if (!disconnectDB())
			return RESULT_INTERNAL_ERROR;

		if (!backupDB(dirBackupTemp))
			return RESULT_INTERNAL_ERROR;

        // zip images folder
		String fileBackupImgTemp = dirBackupTemp + BLOB_ZIP_FILE;
		if (!zipDBImagesFolder(fileBackupImgTemp))
			return RESULT_INTERNAL_ERROR;

		// zip all backup in one File
		if (!zipDBBackup(path, dirBackupTemp))
			return RESULT_INTERNAL_ERROR;

		// clean temp dir
		FileHelper.deleteDir(dirBackupTemp);

		reconnectDB();

		return RESULT_OK; // ALL OK
    }

	public static short restoreAllDB(String path) {
        Services.Log.debug(OBJECT_NAME, "restore db");

		if (!Services.Application.get().isOfflineApplication())
			return RESULT_APP_NOT_OFFLINE;

		if (!Services.Strings.hasValue(path))
		{
			Services.Log.debug(OBJECT_NAME, "path is null or empty");
			return RESULT_PATH_INCORRECT;
		}

		File dbBackup = new File(path);
		if (!dbBackup.exists())
			return RESULT_PATH_INCORRECT;

		String dirBackup = dbBackup.getParent();
		ZipHelper.dirCheckOrCreate(dirBackup);

		// Create temp dir
		String dirBackupTemp = getBackupTmpDir(dbBackup, dirBackup);

		// unzip backup folder
		if (!unzipDBBackup(path, dirBackupTemp))
			return RESULT_INTERNAL_ERROR;

		if (!restoreMD5(dirBackupTemp))
			return RESULT_DB_MD5_NOTMATCH;

		// now disconect DB and restore it
		if (!disconnectDB())
			return RESULT_INTERNAL_ERROR;

		if (!restoreDB(dirBackupTemp))
			return RESULT_INTERNAL_ERROR;

        // unzip images folder
		String fileBackupImgTemp = dirBackupTemp + BLOB_ZIP_FILE;
		if (!unzipDBImagesFolder(fileBackupImgTemp))
			return RESULT_INTERNAL_ERROR;

		// clean temp dir
		FileHelper.deleteDir(dirBackupTemp);

		reconnectDB();

		return RESULT_OK; // ALL OK
    }

    private static boolean backupDB(@NonNull String outDirPath) {

        //database path
        final String inFileName = AndroidContext.ApplicationContext.getDataBaseFilePath();
		File dbFile = new File(inFileName);

		String outFileName = outDirPath + dbFile.getName();
		try {
			Services.Log.info("DB Backup sqlite file");
			if (FileHelper.copyFile(inFileName, outFileName, false)) {
				Services.Log.debug("DB Backup wal file ");
				FileHelper.copyFile(inFileName + "-wal", outFileName + "-wal", true);
				Services.Log.debug("DB Backup shm file ");
				FileHelper.copyFile(inFileName + "-shm", outFileName + "-shm", true);

				// backups hashes
				final String inHashesFileName = AndroidContext.ApplicationContext.getDataBaseSyncHashesFilePath();
				Services.Log.debug("DB Backup hashes file ");
				File dbHashesFile = new File(inHashesFileName);
				String outHashesFileName = outDirPath + dbHashesFile.getName();
				FileHelper.copyFile(inHashesFileName, outHashesFileName, true);

				// backup md5
				Services.Log.debug("DB Backup md5 file ");
				String dbName = FilenameUtils.removeExtension(dbFile.getName());
				String md5File = outDirPath + dbName + ".md5";
				if (!FileHelper.writeStringAsFile(Services.Application.get().getReorMD5Hash(), md5File))
					return false;

				Services.Log.info("Backup Completed");
				return true;
			}
		} catch (IOException ex) {
            Services.Log.error( "Unable to backup database. " + ex.getMessage());
        }
		return false;
    }

	private static boolean restoreMD5(@NonNull String inDirPath) {

		final String outFileName = AndroidContext.ApplicationContext.getDataBaseFilePath();
		File dbFile = new File(outFileName);

		// backup md5
		String dbName = FilenameUtils.removeExtension(dbFile.getName());
		String md5File = inDirPath + dbName + ".md5";
		String md5 = FileHelper.readFileAsString(md5File);
		Services.Log.debug("md5 in backup = " + md5);

		if (md5!=null
			&& md5.equalsIgnoreCase(Services.Application.get().getReorMD5Hash())
		)
			return true;

		Services.Log.debug("DB Backup md5 do not match " );
		return false;

	}

	private static boolean restoreDB(@NonNull String inDirPath) {

        final String outFileName = AndroidContext.ApplicationContext.getDataBaseFilePath();
		File dbFile = new File(outFileName);

		String inFileName = inDirPath + dbFile.getName();
		try {

			Services.Log.info("DB Restore sqlite file");
			if (FileHelper.copyFile(inFileName, outFileName, false))
			{
				Services.Log.debug("DB Restore wal file ");
				FileHelper.copyFile(inFileName + "-wal", outFileName + "-wal", true);
				Services.Log.debug("DB Restore shm file ");
				FileHelper.copyFile(inFileName + "-shm", outFileName + "-shm", true);

				final String outHashesFileName = AndroidContext.ApplicationContext.getDataBaseSyncHashesFilePath();
				Services.Log.debug("DB Restore hashes file ");
				File dbHashesFile = new File(outHashesFileName);
				String inHashesFileName = inDirPath + dbHashesFile.getName();
				FileHelper.copyFile(inHashesFileName, outHashesFileName, true);

				Services.Log.info("Restore Completed");
				return true;
			}
        } catch (IOException ex) {
            Services.Log.error( "Unable to restore database. " + ex.getMessage());
        }
		return false;
    }


	private static boolean zipDBImagesFolder(@NonNull String filePathBackup) {

		File directory = new File(AndroidContext.ApplicationContext.getFilesBlobsApplicationDirectory());
		File[] files = directory.listFiles();
		String[] filesPaths = new String[files.length];
		Services.Log.debug("Files quantity : "+ files.length);
		if (files.length>0) {
			for (int i = 0; i < files.length; i++) {
				Services.Log.debug("Files FileName: " + files[i].getName());
				filesPaths[i] = files[i].getAbsolutePath();
			}
			// calling the zip function
			if (ZipHelper.zip(filesPaths, filePathBackup))
			{
				Services.Log.debug("Zip file created " + filePathBackup);
				return true;
			}
			else
				return false;
		}
		else
			return true; // no files, no zip created.
	}

	private static boolean zipDBBackup(@NonNull String filePathBackup, @NonNull String dirBackupTemp) {

		File directory = new File(dirBackupTemp);
		File[] files = directory.listFiles();
		String[] filesPaths = new String[files.length];
		Services.Log.debug("Files quantity : "+ files.length);
		if (files.length==0)
			return false; // backup with no file is not correct.

		for (int i = 0; i < files.length; i++) {
			Services.Log.debug("Files FileName: " + files[i].getName());
			filesPaths[i] = files[i].getAbsolutePath();
		}
		// calling the zip function
		if (ZipHelper.zip(filesPaths, filePathBackup)) {
			Services.Log.debug("Zip file created " + filePathBackup);
			return true;
		}
		else
			return false;

	}

	private static boolean unzipDBImagesFolder(@NonNull String filePathBackup) {
		File fileBackup = new File(filePathBackup);
		if (fileBackup.exists()) {
			// calling the zip function
			if (ZipHelper.unzip(filePathBackup, AndroidContext.ApplicationContext.getFilesBlobsApplicationDirectory() + File.separator)) {
				Services.Log.debug("UnZip file successfully ");
				return true;
			}
			else
				return false; // unzip fails
		}
		return true; // no image backup, no file to extract.
	}

	private static boolean unzipDBBackup(@NonNull String filePathBackup, @NonNull String dirBackupTemp) {
		File fileBackup = new File(filePathBackup);
		if (fileBackup.exists()) {
			// calling the zip function
			if (ZipHelper.unzip(filePathBackup, dirBackupTemp)) {
				Services.Log.debug("UnZip file successfully ");
				return true;
			}
			else
				return false; // unzip fails
		}
		else
			return false; // if backup not exits fails
	}

	public static @NonNull String getBackupTmpDir(@NonNull File dbBackup, @NonNull String dirBackup) {
		// Create temp dir
		String backupName = FilenameUtils.removeExtension(dbBackup.getName());
		String dirBackupTemp = dirBackup + File.separator + backupName + TMP_DIRECTORY + File.separator;
		ZipHelper.dirCheckOrCreate(dirBackupTemp);
		return dirBackupTemp;
	}

	private static boolean disconnectDB() {
		String filePath = AndroidContext.ApplicationContext.getDataBaseFilePath();
		File file = new File(filePath);
		return SyncManager.closeDBConnection(file);
	}

	private static void reconnectDB() {
		String filePath = AndroidContext.ApplicationContext.getDataBaseFilePath();
		File file = new File(filePath);

		// drop before connection because it not useful any more, use a new one with correct auto commit mode
		int remoteHandle = Application.getNewRemoteHandle(ClientContext.getModelContext());
		//set this handle as app handle, store in as int in App. Use in all reflection calls
		Services.Application.get().setRemoteHandle(remoteHandle);

		// begin connection to new database at app startup
		Services.Log.debug("begin connection to DB at startup : " + file.getAbsolutePath());
		Services.Sync.getPendingEventsList(SynchronizationHelper.PENDING_STATUS_STRING); // Pending
	}


}
