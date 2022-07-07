package com.artech.android.remotenotification;

import android.content.Context;

public interface IBadgeNotification {
    void setBadgeCount(Context context, int number);
}
