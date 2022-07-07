package com.genexus.android.uitesting.idlingresources

import android.app.Activity
import android.app.Dialog
import android.view.View
import com.genexus.android.ViewHierarchyVisitor
import com.genexus.android.core.activities.GenexusActivity
import com.genexus.android.core.controls.LoadingIndicatorView
import com.genexus.android.core.utils.UITestingUtils.Companion.dialogPresent

internal object IdleChecking {
    @JvmStatic
    fun isLoadingIndicatorIdle(activity: Activity): Boolean {
        val views = ViewHierarchyVisitor.getViews(LoadingIndicatorView::class.java, activity.findViewById(android.R.id.content))
        for (view in views)
            if (view.visibility == View.VISIBLE)
                return false

        return true
    }

    @JvmStatic
    fun isAlertDialogIdle(activity: Activity?, dialog: Dialog?): Boolean {
        if (activity !is GenexusActivity)
            return false

        // This means that an AlertDialog was created from ClientStart event (blocking execution, LoadingIndicator still present),
        // so we need to let the test tap on it in order to continue execution
        val controller = activity.controller.getController(activity.mainFragment)
        return controller != null && !controller.isStarted && dialogPresent || dialog == null || !dialog.isShowing
    }
}
