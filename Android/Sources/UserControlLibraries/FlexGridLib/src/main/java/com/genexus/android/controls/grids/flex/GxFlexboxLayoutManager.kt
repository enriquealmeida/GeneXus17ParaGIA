package com.genexus.android.controls.grids.flex

import android.content.Context
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.AlignContent
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager

class GxFlexboxLayoutManager(context: Context) : FlexboxLayoutManager(context) {
    private var alignContent = AlignContent.STRETCH

    override fun getAlignContent() = alignContent

    override fun setAlignContent(@AlignContent alignContent: Int) {
        if (this.alignContent != alignContent) {
            this.alignContent = alignContent
            requestLayout()
        }
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        super.onLayoutChildren(recycler, state)

        if (childCount > 0) {
            val isColumn = flexDirection == FlexDirection.COLUMN || flexDirection == FlexDirection.COLUMN_REVERSE
            val isRtl = layoutDirection == ViewCompat.LAYOUT_DIRECTION_RTL

            val beforeCrossSpace: Int
            val afterCrossSpace: Int
            if (isColumn) {
                var lowestLeft = Int.MAX_VALUE
                var highestRight = Int.MIN_VALUE
                for (index in 0 until childCount) {
                    val view = getChildAt(index)
                    lowestLeft = Math.min(lowestLeft, view!!.left)
                    highestRight = Math.max(highestRight, view.right)
                }
                beforeCrossSpace = lowestLeft
                afterCrossSpace = width - highestRight
            } else {
                var lowestTop = Int.MAX_VALUE
                var highestBottom = Int.MIN_VALUE
                for (index in 0 until childCount) {
                    val view = getChildAt(index)
                    lowestTop = Math.min(lowestTop, view!!.top)
                    highestBottom = Math.max(highestBottom, view.bottom)
                }
                beforeCrossSpace = lowestTop
                afterCrossSpace = height - highestBottom
            }

            val crossSpace = beforeCrossSpace + afterCrossSpace
            if (crossSpace > 0) {
                val linesCount = flexLines.count()
                var before = -beforeCrossSpace
                var between = 0
                var extra = 0
                when (alignContent) {
                    AlignContent.FLEX_START -> {
                        // The items are packed flush to each other against the start edge of the
                        // alignment container in the cross axis.
                        if (isColumn && isRtl)
                            before = afterCrossSpace
                    }
                    AlignContent.FLEX_END -> {
                        // The items are packed flush to each other against the end edge of the
                        // alignment container in the cross axis.
                        if (!isColumn || !isRtl)
                            before = afterCrossSpace
                    }
                    AlignContent.CENTER -> {
                        // The items are packed flush to each other in the center of the alignment
                        // container along the cross axis.
                        before += crossSpace / 2
                    }
                    AlignContent.SPACE_AROUND -> {
                        // The items are evenly distributed within the alignment container along
                        // the cross axis. The spacing between each pair of adjacent items is the
                        // same. The empty space before the first and after the last item equals
                        // half of the space between each pair of adjacent items.
                        between = crossSpace / linesCount
                        before += between / 2
                    }
                    AlignContent.SPACE_BETWEEN -> {
                        // The items are evenly distributed within the alignment container along
                        // the cross axis. The spacing between each pair of adjacent items is the
                        // same. The first item is flush with the start edge of the alignment
                        // container in the cross axis, and the last item is flush with the end
                        // edge of the alignment container in the cross axis.
                        if (linesCount > 1)
                            between = crossSpace / (linesCount - 1)
                    }
                    else -> { // AlignContent.STRETCH (default)
                        // If the combined size of the items along the cross axis is less than the
                        // size of the alignment container, any auto-sized items have their size
                        // increased equally (not proportionally), so that the combined size
                        // exactly fills the alignment container along the cross axis.
                        between = crossSpace / linesCount
                        extra = between
                    }
                }

                var sum = before
                for (flexLine in flexLines) {
                    if (flexLine.firstIndex > 0) {
                        sum += between
                    }
                    for (index in 0 until flexLine.itemCount) {
                        getChildAt(flexLine.firstIndex + index)?.let { view ->
                            if (isColumn)
                                view.layout(view.left + sum, view.top, view.right + sum + extra, view.bottom)
                            else
                                view.layout(view.left, view.top + sum, view.right, view.bottom + sum + extra)
                        }
                    }
                }
            }
        }
    }
}
