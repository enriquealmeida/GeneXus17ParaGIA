package com.genexus.android.core.controls

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.Toolbar
import com.genexus.android.core.base.services.Services

/**
 * Custom Toolbar based on https://stackoverflow.com/a/30425787
 * to handle click/tap events correctly.
 */
class GxToolbar : Toolbar {
    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    var transparentMode: Boolean = false

    /** Do not eat touch events, like super does. Instead map an ACTION_DOWN to a click on this
     * view. If no click listener is set (default), we do not consume the click  */
    override fun onTouchEvent(ev: MotionEvent): Boolean {
        if (transparentMode) {
            Services.Log.debug("App bar onTouchEvent in Transparent mode")
            return if (ev.action == MotionEvent.ACTION_DOWN || ev.action == MotionEvent.ACTION_POINTER_DOWN) {
                performClick()
            } else false
        }
        return super.onTouchEvent(ev)
    }
}
