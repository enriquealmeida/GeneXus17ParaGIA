package com.genexus.android.gam;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Activity that receives the redirect Uri sent by the OpenID endpoint (via the browser).
 * It forwards the data received as part of this redirect to {@link AuthManagementActivity} with
 * a CLEAR_TOP intent (which also destroys the browser activity in the stack) before
 * returning the result to the actual activity that started the login.
 */

public class AuthRedirectActivity extends AppCompatActivity
{
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		startActivity(AuthManagementActivity.createResponseHandlingIntent(this, getIntent().getData()));
		finish();
	}
}
