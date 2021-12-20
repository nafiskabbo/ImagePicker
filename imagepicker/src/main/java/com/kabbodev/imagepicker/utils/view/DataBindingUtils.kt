/*
 * Copyright (c) 2021 Image Picker
 * Author: Nafis Islam Kabbo <kabboandreigns@gmail.com>
 */

package com.kabbodev.imagepicker.utils.view

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.kabbodev.imagepicker.model.Image
import com.kabbodev.imagepicker.utils.ImageUtils.loadCenterCropImage

@BindingAdapter("centerCropImageSrc")
fun ImageView.loadImageSrc(imageSrc: Any?) {
    this.loadCenterCropImage(
        if (imageSrc is ArrayList<*> && imageSrc.firstOrNull() is Image) (imageSrc.firstOrNull() as Image).uri else imageSrc
    )
}