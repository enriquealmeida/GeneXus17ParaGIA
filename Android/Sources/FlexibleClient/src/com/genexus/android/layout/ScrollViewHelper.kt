package com.genexus.android.layout

import android.graphics.Point
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import androidx.core.widget.NestedScrollView
import com.genexus.android.core.base.services.Services

class ScrollViewHelper(private val view: View) {

    private val scrollView = findScrollView(view)
    private val mainScrollView = findScrollView(scrollView) as? CustomScrollView?
    private val screenRect = Rect(
        0, 0, Services.Device.dipsToPixels(Services.Device.screenWidth),
        Services.Device.dipsToPixels(Services.Device.screenHeight)
    )

    fun performScroll(verticalPosition: String?, horizontalPosition: String?) {
        if (scrollView == null) {
            Services.Log.error("Scrollable View wasn't found in layout")
            return
        }

        if (!view.isFullyVisible())
            return

        // Nested scroll when Vertical Position = Middle is a special case
        val middleNestedScroll = mainScrollView != null && !mainScrollView.isUseless && isMiddle(verticalPosition)
        val controlOffset = Point()
        getChildOffset(scrollView, view.parent, view, controlOffset)
        val controlScrollX = getScrollX(view, controlOffset.x, scrollView.width, horizontalPosition)
        val controlScrollY = getScrollY(view, controlOffset.y, scrollView.height, verticalPosition, middleNestedScroll)
        scrollView.post { scrollView.smoothScrollTo(controlScrollX, controlScrollY) }

        mainScrollView?.let {
            val scrollOffset = Point()
            getChildOffset(mainScrollView, scrollView.parent, scrollView, scrollOffset)
            val scrollScrollX = getScrollX(scrollView, scrollOffset.x, mainScrollView.width, horizontalPosition)
            val scrollScrollY = getScrollY(scrollView, scrollOffset.y, mainScrollView.height, verticalPosition, false)
            mainScrollView.post { mainScrollView.smoothScrollTo(scrollScrollX, scrollScrollY) }
        }
    }

    private fun getChildOffset(mainParent: ViewGroup, parent: ViewParent, child: View, accumulatedOffset: Point) {
        val parentGroup = parent as? ViewGroup
        accumulatedOffset.x += child.left
        accumulatedOffset.y += child.top
        if (parentGroup == null || parentGroup == mainParent)
            return

        getChildOffset(mainParent, parentGroup.parent, parentGroup, accumulatedOffset)
    }

    private fun findScrollView(view: View?): NestedScrollView? {
        if (view == null)
            return null

        var scrollView: NestedScrollView?
        var nextView = view
        do {
            nextView = nextView?.parent as? View
            scrollView = nextView as? NestedScrollView
        } while (scrollView == null && nextView != null)

        return scrollView
    }

    private fun getScrollY(view: View, originalOffsetY: Int, scrollViewHeight: Int, verticalPosition: String?, middleNestedScroll: Boolean): Int {
        val newOffset = originalOffsetY - scrollViewHeight + view.height
        if (middleNestedScroll)
            return (originalOffsetY + view.height) / 3 * 2

        return when {
            verticalPosition == null -> originalOffsetY
            verticalPosition.equals(VERTICAL_MIDDLE, ignoreCase = true) -> newOffset + view.height
            verticalPosition.equals(VERTICAL_BOTTOM, ignoreCase = true) -> newOffset
            else -> originalOffsetY
        }
    }

    private fun getScrollX(view: View, originalOffsetX: Int, scrollViewWidth: Int, horizontalPosition: String?): Int {
        val newOffset = originalOffsetX - scrollViewWidth + view.width
        return when {
            horizontalPosition == null -> originalOffsetX
            horizontalPosition.equals(HORIZONTAL_CENTER, ignoreCase = true) -> newOffset + view.width
            horizontalPosition.equals(HORIZONTAL_RIGHT, ignoreCase = true) -> newOffset
            else -> originalOffsetX
        }
    }

    private fun View.isFullyVisible(): Boolean {
        if (!isShown) {
            Services.Log.warning("Target view isn't currently visible")
            return false
        }

        val scrollViewRect = Rect()
        scrollView?.getGlobalVisibleRect(scrollViewRect)
        if (!Rect.intersects(screenRect, scrollViewRect)) {
            Services.Log.warning("Target view isn't currently on screen")
            return false
        }

        return true
    }

    private fun isMiddle(verticalPosition: String?): Boolean {
        return verticalPosition != null && verticalPosition.equals(VERTICAL_MIDDLE, true)
    }

    companion object {
        private const val VERTICAL_TOP = "top"
        private const val VERTICAL_MIDDLE = "middle"
        private const val VERTICAL_BOTTOM = "bottom"
        private const val HORIZONTAL_LEFT = "left"
        private const val HORIZONTAL_CENTER = "center"
        private const val HORIZONTAL_RIGHT = "right"
    }
}
