package com.genexus.android.core.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.genexus.android.core.base.services.Services;

public class KeyboardUtils
{
	private static final double KEYBOARD_MIN_HEIGHT_RATIO = 0.25;

	public static void showKeyboard(View view)
	{
		if (view != null)
		{
			InputMethodManager imm = (InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
			if (imm != null)
				imm.showSoftInput(view, 0);
		}
	}

	public static void hideKeyboard(Activity activity)
	{
		View focused = activity.getCurrentFocus();
		if (focused != null && focused instanceof EditText)
			hideKeyboard(focused);
	}

	public static void hideKeyboard(View view)
	{
		Services.Log.debug("call hideKeyboard");
		if (view != null)
		{
			InputMethodManager imm = (InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
			if (imm != null) {
				Services.Log.debug("hideKeyboard called");
				imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
			}
		}
	}

	public static boolean isKeyboardVisible(View rootActivityView) {
		if (rootActivityView==null)
			return false;

		// HACK: the is not a straightforward API in Android sdk to check soft keyboard.
		// use Was from : https://medium.com/@munnsthoughts/detecting-if-the-android-keyboard-is-open-using-kotlin-rxjava-2-8aee9fae262c
		// https://stackoverflow.com/a/26964010 or https://github.com/yshrsmz/KeyboardVisibilityEvent
		Rect r = new Rect();
		rootActivityView.getWindowVisibleDisplayFrame(r);

		int screenHeight = rootActivityView.getHeight();
		int heightDiff = rootActivityView.getRootView().getHeight() - r.height();

		Services.Log.debug("Diff height " + heightDiff + " screen ratio " + screenHeight * KEYBOARD_MIN_HEIGHT_RATIO);
		return heightDiff > screenHeight * KEYBOARD_MIN_HEIGHT_RATIO;
	}
}
