package com.genexus.android.core.base.services;

import java.io.IOException;
import java.io.InputStream;

import org.json.JSONException;

import com.genexus.android.R;
import com.genexus.android.core.base.serialization.INodeObject;
import com.genexus.android.core.common.DataRequest;

@SuppressWarnings("checkstyle:MemberName")
public class ServiceResponse
{
	public int HttpCode = 0;
	public String Message = null;
	public InputStream Stream = null;
	public INodeObject Data = null;

	public int StatusCode = DataRequest.ERROR_NONE;
	public String ErrorMessage = null;
	public String WarningMessage = null;

	public ServiceResponse() { }

	public ServiceResponse(JSONException ex)
	{
		HttpCode = -1;
		ErrorMessage = Services.Strings.getResource(R.string.GXM_ApplicationServerError, JSONException.class.getName());
	}

	public ServiceResponse(IOException ex)
	{
		HttpCode = -1;
		ErrorMessage = Services.HttpService.getNetworkErrorMessage(ex);
	}

	public boolean getResponseOk()
	{
		return (HttpCode >= 200 && HttpCode < 300) && !isOtpResponse() && !is2FAResponse();
	}

	private boolean isOtpResponse() {
		return HttpCode == 202 && (StatusCode == DataRequest.ERROR_SECURITY_OTP_REQUIRED || StatusCode == DataRequest.ERROR_SECURITY_OTP_LINK);
	}

	private boolean is2FAResponse() {
		return HttpCode == 202 && StatusCode == DataRequest.ERROR_SECURITY_2FA_REQUIRED;
	}

	public String get(String name) {
		return (Data != null ? Data.optString(name) : null);
	}

	public boolean has(String name) {
		return Data != null && Data.has(name);
	}
}
