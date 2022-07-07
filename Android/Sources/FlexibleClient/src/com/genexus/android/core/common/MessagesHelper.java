package com.genexus.android.core.common;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;

import com.genexus.android.R;
import com.genexus.android.core.base.services.IMessages;
import com.genexus.android.core.base.services.Services;

public class MessagesHelper implements IMessages {
	private final Context mAppContext;

	public MessagesHelper(Context appContext) {
		mAppContext = appContext;
	}

	@Override
	public void showMessage(CharSequence text) {
		showMessage(mAppContext, text);
	}

	@Override
	public void showMessage(int resourceId) {
		showMessage(mAppContext, resourceId);
	}

	@Override
	public void showMessage(Context context, CharSequence text) {
		if (Services.Strings.hasValue(text)) {
			Services.Device.runOnUiThread(() -> Toast.makeText(context, text, Toast.LENGTH_LONG).show());
		}
	}

	@Override
	public void showMessage(Context context, @StringRes int resourceId) {
		showMessage(context, mAppContext.getResources().getString(resourceId));
	}

	@Override
	public void showErrorDialog(Context context, String text) {
		new AlertDialog.Builder(context)
				.setTitle(R.string.GXM_errtitle)
				.setMessage(text)
				.setPositiveButton(R.string.GXM_button_ok, null)
				.show();
	}

}
