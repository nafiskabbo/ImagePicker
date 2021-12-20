/*
 * Copyright (c) 2021 Image Picker
 * Author: Nafis Islam Kabbo <kabboandreigns@gmail.com>
 */

package com.kabbodev.imagepicker.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kabbodev.imagepicker.demo.databinding.ActivityMainBinding
import com.kabbodev.imagepicker.model.Image
import com.kabbodev.imagepicker.model.ImagePickerConfig
import com.kabbodev.imagepicker.model.RootDirectory
import com.kabbodev.imagepicker.ui.image_picker.registerImagePicker
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    protected lateinit var binding: ActivityMainBinding
    private var images = ArrayList<Image>()

    private val launcher = registerImagePicker { it ->
        images = it
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.pickImageBtn.setOnClickListener { start() }
    }

    private fun start() {
        val folderMode = binding.folderModeSwitch.isChecked
        val multipleMode = binding.multipleModeSwitch.isChecked
        val cameraOnly = binding.cameraOnlySwitch.isChecked
        val showCamera = binding.showCameraSwitch.isChecked
        val showNumberIndicator = binding.showNumberIndicatorSwitch.isChecked
        val alwaysShowDone = binding.alwaysShowDoneSwitch.isChecked

        val config = ImagePickerConfig(
            statusBarColor = "#00796B",
            isLightStatusBar = false,
            toolbarColor = "#009688",
            toolbarTextColor = "#FFFFFF",
            toolbarIconColor = "#FFFFFF",
            backgroundColor = "#000000",
            progressIndicatorColor = "#009688",
            selectedIndicatorColor = "#2196F3",
            isCameraOnly = cameraOnly,
            isMultipleMode = multipleMode,
            isFolderMode = folderMode,
            doneTitle = "DONE",
            folderTitle = "Albums",
            imageTitle = "Photos",
            isShowCamera = showCamera,
            isShowNumberIndicator = showNumberIndicator,
            isAlwaysShowDoneButton = alwaysShowDone,
            rootDirectory = RootDirectory.DCIM,
            subDirectory = "Example",
            maxSize = 10,
            limitMessage = "You could only select up to 10 photos",
            selectedImages = images
        )

        launcher.launch(config)
    }

}