/*
 * Copyright (c) 2021 Image Picker
 * Author: Nafis Islam Kabbo <kabboandreigns@gmail.com>
 */

package com.kabbodev.imagepicker.model

import androidx.annotation.Keep

@Keep
sealed class ImageStatus {
    object ImageSelectedOrUpdated : ImageStatus()
    object ImageUnSelected : ImageStatus()
}
