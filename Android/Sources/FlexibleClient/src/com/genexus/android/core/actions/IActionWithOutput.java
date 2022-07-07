package com.genexus.android.core.actions;

import android.app.Activity;

import com.genexus.android.core.base.application.OutputResult;

public interface IActionWithOutput
{
	Activity getActivity();
	UIContext getContext();
	OutputResult getOutput();
}
