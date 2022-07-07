package com.genexus.android.core.externalobjects;

import com.genexus.android.core.layers.LocalUtils;

public class DataBaseAPIOffline {

	// static method to be called inside a offline procedure
	public static short backup(String path) {
		if (!isAllowed())
			return DataBaseAPI.RESULT_INTERNAL_ERROR;

		//Call From a Gx Procedure, should close transaction first
		LocalUtils.commit();
		LocalUtils.endTransaction();

		short result = DataBaseAPI.backupAllDB(path);

		//Start procedure transaction again
		LocalUtils.beginTransaction();

		return result;
	}

	// static method to be called inside a offline procedure
	public static short restore(String path) {
		if (!isAllowed())
			return DataBaseAPI.RESULT_INTERNAL_ERROR;

		//Call From a Gx Procedure, should close transaction first
		LocalUtils.commit();
		LocalUtils.endTransaction();

		short result = DataBaseAPI.restoreAllDB(path);

		//Start procedure transaction again
		LocalUtils.beginTransaction();

		return result;
	}

	private static boolean isAllowed() {
		return new DataBaseAPI(null).allowedByContext(null);
	}
}
