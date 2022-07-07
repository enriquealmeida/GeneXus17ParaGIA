package com.genexus.android.controls.grids.smart

import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.genexus.android.core.controls.GxHorizontalSeparator

class GxDividerItemDecoration(private val separator: GxHorizontalSeparator) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.bottom = separator.height
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val divider = separator.drawable ?: return // draw nothing if there is no divider

        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val top: Int = child.bottom + params.bottomMargin
            val bottom: Int = top + separator.height
            divider.setBounds(left, top, right, bottom)
            divider.draw(c)
        }
    }
}
