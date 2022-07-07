package com.genexus.android.core.base.services;

import android.content.Context;

import androidx.annotation.StringRes;

public interface IMessages {
    void showMessage(CharSequence text);
    void showMessage(@StringRes int resourceId);
    void showMessage(Context context, CharSequence text);
    void showMessage(Context context, @StringRes int resourceId);
    void showErrorDialog(Context context, String text);
}
