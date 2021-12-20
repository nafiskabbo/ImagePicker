/*
 * Copyright (c) 2021 Image Picker
 * Author: Nafis Islam Kabbo <kabboandreigns@gmail.com>
 */

package com.kabbodev.imagepicker.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.kabbodev.imagepicker.R

object ImageUtils {

    fun ImageView.loadCenterCropImage(imageSrc: Any?) {
        val requestOptions = RequestOptions()
            .placeholder(R.drawable.image_picker_image_placeholder)
            .error(R.drawable.image_picker_image_error)
            .centerCrop()
        loadImage(imageSrc, requestOptions)
    }

    private fun ImageView.loadImage(imageSrc: Any?, requestOptions: RequestOptions) {
        Glide
            .with(this.context)
            .load(imageSrc ?: "")
            .transition(DrawableTransitionOptions.withCrossFade())
            .apply(requestOptions)
            .into(this)
    }

}