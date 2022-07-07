package com.genexus.android;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import androidx.annotation.NonNull;

public class ManifestHelper {
    public static boolean hasPermissionDeclared(@NonNull Context context,
                                                @NonNull String permission) {
        String packageName = context.getPackageName();

        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(packageName, PackageManager.GET_PERMISSIONS);
            String[] declaredPermissions = packageInfo.requestedPermissions;
            if (declaredPermissions != null) {
                for (String declaredPermission : declaredPermissions) {
                    if (declaredPermission.equals(permission)) {
                        return true;
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }

        return false;
    }
}
