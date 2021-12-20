/*
 * Copyright (c) 2021 Image Picker
 * Author: Nafis Islam Kabbo <kabboandreigns@gmail.com>
 */

package com.kabbodev.imagepicker.utils.view

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class GridSpacingItemDecoration(private val spanCount: Int, private val spacing: Int) : RecyclerView.ItemDecoration() {

    private var mNeedLeftSpacing = true

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val frameWidth = (parent.width - spacing * (spanCount - 1)) / spanCount
        val padding = parent.width / spanCount - frameWidth

        val itemPosition = (view.layoutParams as RecyclerView.LayoutParams).bindingAdapterPosition
        outRect.top = if (itemPosition < spanCount) 0 else spacing

        when {
            itemPosition % spanCount == 0 -> {
                outRect.left = 0
                outRect.right = padding
                mNeedLeftSpacing = true
            }
            (itemPosition + 1) % spanCount == 0 -> {
                mNeedLeftSpacing = false
                outRect.right = 0
                outRect.left = padding
            }
            mNeedLeftSpacing -> {
                mNeedLeftSpacing = false
                outRect.left = spacing - padding
                outRect.right = if ((itemPosition + 2) % spanCount == 0) (spacing - padding) else (spacing / 2)
            }
            (itemPosition + 2) % spanCount == 0 -> {
                mNeedLeftSpacing = false
                outRect.left = spacing / 2
                outRect.right = spacing - padding
            }
            else -> {
                mNeedLeftSpacing = false
                outRect.left = spacing / 2
                outRect.right = spacing / 2
            }
        }

        outRect.bottom = 0
    }
}