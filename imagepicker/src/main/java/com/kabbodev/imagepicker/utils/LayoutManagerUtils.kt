/*
 * Copyright (c) 2021 Image Picker
 * Author: Nafis Islam Kabbo <kabboandreigns@gmail.com>
 */

package com.kabbodev.imagepicker.utils

import android.content.Context
import android.content.res.Configuration
import androidx.recyclerview.widget.GridLayoutManager
import com.kabbodev.imagepicker.model.GridCount

object LayoutManagerUtils {

    fun newInstance(context: Context, gridCount: GridCount): GridLayoutManager {
        val spanCount = getSpanCountForCurrentConfiguration(context, gridCount)
        return GridLayoutManager(context, spanCount)
    }

    fun getSpanCountForCurrentConfiguration(context: Context, gridCount: GridCount): Int {
        val isPortrait = context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
        return if (isPortrait) gridCount.portrait else gridCount.landscape
    }

}