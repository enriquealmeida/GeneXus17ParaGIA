package com.genexus.android.core.externalobjects;

import java.util.List;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import androidx.annotation.NonNull;

import com.genexus.android.core.actions.ApiAction;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.ResultRunnable;
import com.genexus.android.core.externalapi.ExternalApi;
import com.genexus.android.core.externalapi.ExternalApiResult;

public class ClipboardAPI extends ExternalApi {
	public static final String OBJECT_NAME = "GeneXus.Common.Clipboard";

	private static final String METHOD_GET_TEXT = "getText";
	private static final String METHOD_SET_TEXT = "setText";

	private static ClipboardManager sClipboard;

	public ClipboardAPI(ApiAction action) {
		super(action);
	}

	@Override
	public @NonNull ExternalApiResult execute(String method, List<Object> parameters) {
		if (sClipboard == null) {
			sClipboard = Services.Device.invokeOnUiThread(new ResultRunnable<ClipboardManager>() {

				@Override
				public ClipboardManager run() {
					return getSystemClipboard();
				}
			});
		}

		if (METHOD_GET_TEXT.equalsIgnoreCase(method) && parameters.size() == 0) {
			return ExternalApiResult.success(getClipboardText());
		} else if (METHOD_SET_TEXT.equalsIgnoreCase(method) && parameters.size() == 1) {
			String text = (String) parameters.get(0);
			setClipboardText(text);
			return ExternalApiResult.SUCCESS_CONTINUE;
		} else
			return ExternalApiResult.failureUnknownMethod(this, method);
	}

	private ClipboardManager getSystemClipboard() {
		Context context = getActivity().getApplicationContext();
		return (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
	}

	private String getClipboardText() {
		String text = "";

		if (sClipboard.hasPrimaryClip()) {
			ClipData.Item dataClipItem = sClipboard.getPrimaryClip().getItemAt(0);
			if (dataClipItem.getText() != null) {
				text = dataClipItem.getText().toString();
			}
		}

		return text;
	}

	private void setClipboardText(String text) {
		ClipData clipData = ClipData.newPlainText("PlainText", text);
		sClipboard.setPrimaryClip(clipData);
	}
}
