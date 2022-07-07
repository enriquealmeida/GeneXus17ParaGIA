package com.genexus.android;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;

import com.genexus.android.core.base.utils.Strings;

import java.io.FileNotFoundException;

import static android.content.Context.DOWNLOAD_SERVICE;

public class FileDownloader {
	private final FileDownloaderListener mListener;
	private final DownloadManager mDownloadManager;
	private final long mDownloadId;

	@SuppressWarnings("deprecation")
	private FileDownloader(Context context, Uri uri, String dirType, FileDownloaderListener listener) {
		mListener = listener;
		mDownloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);

		DownloadManager.Request downloadRequest = new DownloadManager.Request(uri);

		if (Strings.hasValue(dirType)) {
			String fileName = uri.toString().substring(uri.toString().lastIndexOf("/") + 1);
			downloadRequest.setTitle("Downloading needed file...");
			downloadRequest.setDescription("");
			downloadRequest.setDestinationInExternalFilesDir(context, dirType, fileName);
		}

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
			downloadRequest.setVisibleInDownloadsUi(true);
		}

		assert mDownloadManager != null;
		mDownloadId = mDownloadManager.enqueue(downloadRequest);

		IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
		context.registerReceiver(mReceiver, intentFilter);

		Uri downloadUri = Uri.parse("content://downloads/my_downloads/" + mDownloadId);

		context.getContentResolver().registerContentObserver(downloadUri, false, mContentObserver);
	}

	public static void enqueue(Uri uri, Context context, FileDownloaderListener listener) {
		enqueue(uri, context, null, listener);
	}

	public static void enqueue(Uri uri, Context context, String dirType, FileDownloaderListener listener) {
		new FileDownloader(context, uri, dirType, listener);
	}

	private ContentObserver mContentObserver = new ContentObserver(null) {
		@Override
		public void onChange(boolean selfChange) {
			DownloadManager.Query query = new DownloadManager.Query();
			query.setFilterById(mDownloadId);

			try (Cursor cursor = mDownloadManager.query(query)) {
				if (!cursor.moveToFirst()) {
					return;
				}

				int bytesDownloadedIndex = cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
				int bytesTotalIndex = cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);

				long bytesDownloaded = cursor.getLong(bytesDownloadedIndex);
				long bytesTotal = cursor.getLong(bytesTotalIndex);

				int progressPercentage = (int) Math.round(((double) bytesDownloaded / (double) bytesTotal) * 100);
				mListener.onDownloadProgressUpdate(progressPercentage);
			}
		}
	};

	private BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0L);

			if (downloadId != mDownloadId || !DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
				return;
			}

			context.unregisterReceiver(mReceiver);

			DownloadManager.Query query = new DownloadManager.Query();
			query.setFilterById(downloadId);

			try (Cursor cursor = mDownloadManager.query(query)) {
				if (!cursor.moveToFirst()) {
					mListener.onDownloadFailed();
					return;
				}

				try {
					mDownloadManager.openDownloadedFile(downloadId);
				} catch (FileNotFoundException e) {
					mListener.onDownloadFailed();
					return;
				}

				int statusIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
				if (DownloadManager.STATUS_SUCCESSFUL != cursor.getInt(statusIndex)) {
					mListener.onDownloadFailed();
					return;
				}

				int uriIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);
				String downloadUri = cursor.getString(uriIndex);

				int titleIndex = cursor.getColumnIndex(DownloadManager.COLUMN_TITLE);
				String fileName = cursor.getString(titleIndex);
				mListener.onDownloadSuccessful(Uri.parse(downloadUri), fileName);
			}
		}
	};

	public interface FileDownloaderListener {
		void onDownloadProgressUpdate(int progressPercentage);
		void onDownloadSuccessful(Uri fileUri, String fileName);
		void onDownloadFailed();
	}
}
