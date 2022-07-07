package com.genexus.android.media.video;

import android.os.AsyncTask;
import android.util.Log;

import com.artech.base.services.Services;
import com.arthenica.mobileffmpeg.Config;
import com.arthenica.mobileffmpeg.FFmpeg;

import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_CANCEL;
import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS;

public class FFmpegExecuteAsyncTask extends AsyncTask<Void, Integer, Integer> {

	private final ExecuteCallback mExecuteCallback;
	private final String mCommand;
	private final String mOriginalVideoName;
	private final String mModifiedVideoName;

	public FFmpegExecuteAsyncTask(String command, String originalVideo, String modifiedVideo,
								  ExecuteCallback executeCallback) {
		this.mCommand = command;
		this.mExecuteCallback = executeCallback;
		this.mOriginalVideoName = originalVideo;
		this.mModifiedVideoName = modifiedVideo;
	}

	@Override
	protected Integer doInBackground(final Void... unused) {
		return FFmpeg.execute(mCommand);
	}

	@Override
	protected void onPostExecute(final Integer resultCode) {
		if (mExecuteCallback != null) {
			String resultVideo;
			if (resultCode == RETURN_CODE_SUCCESS) {
				Services.Log.debug("Command execution completed successfully.");
				resultVideo = mModifiedVideoName;
			} else if (resultCode == RETURN_CODE_CANCEL) {
				Services.Log.debug("Command execution cancelled by user.");
				resultVideo = mOriginalVideoName;
			} else {
				Services.Log.error(String.format("Command execution failed with resultCode=%d and output below:", resultCode));
				Config.printLastCommandOutput(Log.INFO);
				resultVideo = null;
			}
			mExecuteCallback.onCommandExecuted(resultCode, resultVideo);
		}
	}
}
