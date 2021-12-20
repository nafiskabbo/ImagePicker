/*
 * Copyright (c) 2021 Image Picker
 * Author: Nafis Islam Kabbo <kabboandreigns@gmail.com>
 */

package com.kabbodev.imagepicker.model

import androidx.annotation.Keep

@Keep
data class PickerResult(
    val status: CallbackStatus,
    val images: ArrayList<Image>
)