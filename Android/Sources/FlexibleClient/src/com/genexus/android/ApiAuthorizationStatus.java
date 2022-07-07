package com.genexus.android;

import android.content.Context;
import androidx.annotation.NonNull;

/**
 * Mapping to GX ApiAuthorizationStatus from its Android counterpart.
 */
public class ApiAuthorizationStatus {
	public static final int UNDETERMINED = 0;
	public static final int RESTRICTED = 1;
	public static final int DENIED = 2;
	public static final int AUTHORIZED = 3;
	public static final int AUTHORIZED_WHEN_IN_USE = 4;

	public static int getStatus(@NonNull Context context, @NonNull String[] permissions) {
		boolean result = PermissionUtil.checkSelfPermissions(context, permissions);
		return (result ? AUTHORIZED : DENIED);
	}
}
