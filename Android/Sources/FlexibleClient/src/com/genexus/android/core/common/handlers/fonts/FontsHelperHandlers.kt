package com.genexus.android.core.common.handlers.fonts

import android.graphics.Paint
import android.graphics.Typeface
import android.widget.TextView

class FontsHelperHandlers {
    class SetTextViewHandler(private val view: TextView) : FontsForPostOnUiThread() {
        override fun posted(t: Typeface?) {
            view.typeface = t
        }
    }

    class SetPaintHandler(private val view: Paint) : FontsForPostOnUiThread() {
        override fun posted(t: Typeface?) {
            view.typeface = t
        }
    }
}
