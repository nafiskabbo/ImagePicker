/*
 * Copyright (c) 2021 Image Picker
 * Author: Nafis Islam Kabbo <kabboandreigns@gmail.com>
 */

package com.kabbodev.imagepicker.listeners

import com.kabbodev.imagepicker.model.Folder

interface OnFolderClickListener {
    fun onFolderClick(folder: Folder)
}