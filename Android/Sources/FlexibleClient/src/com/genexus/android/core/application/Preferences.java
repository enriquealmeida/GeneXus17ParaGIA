package com.genexus.android.core.application;

import android.Manifest;
import android.app.ActionBar;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.genexus.android.R;
import com.genexus.android.core.activities.ActivityHelper;
import com.genexus.android.core.activities.IntentFactory;
import com.genexus.android.core.activities.IntentParameters;
import com.genexus.android.PermissionUtil;
import com.genexus.android.WithPermission;
import com.genexus.android.core.base.metadata.expressions.Expression;
import com.genexus.android.core.base.model.ValueCollection;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.common.ValidateAndSaveDynamicUrl;
import com.genexus.android.core.common.ValidateAppServerUri;
import com.genexus.android.core.common.ValidateAppServerUriListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Preferences extends AppCompatActivity {

	private static final int REQ_CODE_SCAN = 2416;
	private static final int REQ_CODE_PERMISSION = 2417;
	private static final String EXTRA_SHOW_TOAST = "showToast";
	private static final String EXTRA_MESSAGE_TOAST = "messageToast";
	private static final String EXTRA_BARCODE_TYPES = "barcodeTypes"; //From QRScannerActivity.EXTRA_BARCODE_TYPES
	private static final String SCANNER_ACTIVITY = "com.genexus.qrscanner.QRScannerActivity";

	private Dialog mServerUrlDialog;
	private EditText mEditText;
	private String mServerURL;
	private boolean mViewDialog = false;

	private Intent mScanIntent = null;

	public static Intent newIntent(Context context, boolean showToast, int message, String serverURL) {
		Intent intent = new Intent(context, Preferences.class);
		intent.putExtra(EXTRA_SHOW_TOAST, showToast);
		intent.putExtra(EXTRA_MESSAGE_TOAST, message);
		intent.putExtra(IntentParameters.SERVER_URL, serverURL);
		return intent;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preferences);
		ActivityHelper.setSupportActionBarAndShadow(this);
		ActionBar actionBar = getActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setDisplayShowHomeEnabled(true);
		}

		Intent intent = getIntent();
		boolean showToast = intent.getBooleanExtra(EXTRA_SHOW_TOAST, false);
		int messageToast = intent.getIntExtra(EXTRA_MESSAGE_TOAST, R.string.GXM_ServerUrlIncorrect);
		mServerURL = intent.getStringExtra(IntentParameters.SERVER_URL);

		if (showToast)
			Toast.makeText(this, messageToast, Toast.LENGTH_SHORT).show();

		findViewById(R.id.layoutServerUrl).setOnClickListener(v -> createDialog());
	}

	private void createDialog() {
		mServerUrlDialog = new Dialog(Preferences.this);
		mServerUrlDialog.setContentView(R.layout.dynamic_url_dialog);
		mServerUrlDialog.setTitle(R.string.GXM_ServerUrl);

		mEditText = mServerUrlDialog.findViewById(R.id.EditTextServerUrl);
		if ((mServerURL != null) && (mServerURL.length() > 0))
			mEditText.setText(mServerURL);

		mViewDialog = true;

		Button okButton = mServerUrlDialog.findViewById(R.id.OkDialog);
		okButton.setOnClickListener(v -> {
			mServerURL = mEditText.getText().toString();
			mViewDialog = false;
			ValidateAndSaveDynamicUrl.execute(mServerURL, mValidateAppServerUriListener);
		});

		Button cancelButton = mServerUrlDialog.findViewById(R.id.CancelDialog);
		cancelButton.setOnClickListener(v -> {
			mServerUrlDialog.dismiss();
			mViewDialog = false;
		});

		Button scanButton = mServerUrlDialog.findViewById(R.id.ScanDialog);
		scanButton.setOnClickListener(v -> {
			//Class is loaded via reflection so FlexibleClient doesn't have to depend on QRScanner
			try {
				ValueCollection types = new ValueCollection(Expression.Type.STRING);
				types.add("QR");
				mScanIntent = new Intent(Preferences.this.getApplicationContext(), Class.forName(SCANNER_ACTIVITY));
				mScanIntent.putExtra(EXTRA_BARCODE_TYPES, types);
				IntentFactory.setIntentFlagsNewDocument(mScanIntent);
			} catch (ActivityNotFoundException | ClassNotFoundException ex) {
				Services.Log.error(ex);
			}

			if (mScanIntent != null) {
				new WithPermission.Builder<Void>(Preferences.this)
						.require(new String[]{Manifest.permission.CAMERA})
						.setRequestCode(REQ_CODE_PERMISSION)
						.onSuccess(() -> {}) //Dummy Runnable to avoid crash as result will be picked by onRequestPermissionResult
						.build()
						.run();
			}
		});

		mServerUrlDialog.show();
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode != REQ_CODE_PERMISSION)
			return;

		if (PermissionUtil.verifyPermissionsResult(grantResults))
			startActivityForResult(mScanIntent, REQ_CODE_SCAN);
		else
			Services.Log.error("Camera permission not granted for Scanning");
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (resultCode == RESULT_OK && requestCode == REQ_CODE_SCAN) {
			mServerURL = intent.getStringExtra("result"); //QRScannerActivity.EXTRA_RESULT
			mEditText.setText(mServerURL);
		}
	}

	@Override
	public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		if (savedInstanceState.getBoolean("ShowDialog")) {
			mServerURL = savedInstanceState.getString("ServerURL");
			createDialog();
		}
	}

	@Override
	public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
		savedInstanceState.putBoolean("ShowDialog", mViewDialog);
		if (mEditText != null && mEditText.getText() != null) {
			savedInstanceState.putString("ServerURL", mEditText.getText().toString());
		}
		super.onSaveInstanceState(savedInstanceState);
	}

	@Override
	protected void onDestroy() {
		if (mServerUrlDialog != null)
			mServerUrlDialog.dismiss();

		mEditText = null;
		super.onDestroy();
	}

	private final ValidateAppServerUriListener mValidateAppServerUriListener = (url, result) -> Services.Device.runOnUiThread(() -> {
		switch (result) {
			case ValidateAppServerUri.VALID_URL:
				Intent resultIntent = new Intent().putExtra(IntentParameters.SERVER_URL, mServerURL);
				setResult(RESULT_OK, resultIntent);
				finish();
				break;
			case ValidateAppServerUri.INVALID_URL:
				Toast.makeText(getApplicationContext(), R.string.GXM_ServerUrlIncorrect, Toast.LENGTH_SHORT).show();
				break;
			case ValidateAppServerUri.NO_CONNECTION:
				Toast.makeText(getApplicationContext(), R.string.GXM_NoInternetConnection, Toast.LENGTH_SHORT).show();
				break;
	}});
}
