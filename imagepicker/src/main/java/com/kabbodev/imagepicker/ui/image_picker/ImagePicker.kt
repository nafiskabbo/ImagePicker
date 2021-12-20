/*
 * Copyright (c) 2021 Image Picker
 * Author: Nafis Islam Kabbo <kabboandreigns@gmail.com>
 */

package com.kabbodev.imagepicker.ui.image_picker

import android.content.Context
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.kabbodev.imagepicker.model.Image
import com.kabbodev.imagepicker.model.ImagePickerConfig
import com.kabbodev.imagepicker.utils.Constants

typealias ImagePickerCallback = (ArrayList<Image>) -> Unit

class ImagePickerLauncher(
    private val context: () -> Context,
    private val resultLauncher: ActivityResultLauncher<Intent>
) {
    fun launch(config: ImagePickerConfig = ImagePickerConfig()) {
        val intent = createImagePickerIntent(context(), config)
        resultLauncher.launch(intent)
    }

    companion object {
        fun createIntent(context: Context, config: ImagePickerConfig = ImagePickerConfig()): Intent {
            return createImagePickerIntent(context, config)
        }
    }
}

private fun createImagePickerIntent(context: Context, config: ImagePickerConfig): Intent {
    val intent: Intent
    if (config.isCameraOnly) {
        intent = Intent(context, ImagePickerActivity::class.java)
//        intent = Intent(context, CameraActivity::class.java)
//        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
    } else {
        intent = Intent(context, ImagePickerActivity::class.java)
    }
    intent.putExtra(Constants.EXTRA_CONFIG, config)
    return intent
}

fun getImages(data: Intent?): ArrayList<Image> {
    return if (data != null) data.getParcelableArrayListExtra(Constants.EXTRA_IMAGES)!!
    else arrayListOf()
}

fun ComponentActivity.registerImagePicker(
    context: () -> Context = { this },
    callback: ImagePickerCallback
): ImagePickerLauncher {
    return ImagePickerLauncher(context, createLauncher(callback))
}

fun Fragment.registerImagePicker(
    context: () -> Context = { requireContext() },
    callback: ImagePickerCallback
): ImagePickerLauncher {
    return ImagePickerLauncher(context, createLauncher(callback))
}

private fun ComponentActivity.createLauncher(callback: ImagePickerCallback): ActivityResultLauncher<Intent> {
    return registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        val images = getImages(it.data)
        callback(images)
    }
}

private fun Fragment.createLauncher(callback: ImagePickerCallback): ActivityResultLauncher<Intent> {
    return registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        val images = getImages(it.data)
        callback(images)
    }
}


