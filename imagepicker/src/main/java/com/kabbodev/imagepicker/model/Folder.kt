/*
 * Copyright (c) 2021 Image Picker
 * Author: Nafis Islam Kabbo <kabboandreigns@gmail.com>
 */

package com.kabbodev.imagepicker.model

import androidx.annotation.Keep
import java.util.ArrayList

@Keep
data class Folder(
    var bucketId: Long,
    var name: String,
    var images: ArrayList<Image> = arrayListOf()
)