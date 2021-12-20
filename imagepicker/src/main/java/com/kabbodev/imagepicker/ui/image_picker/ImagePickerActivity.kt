/*
 * Copyright (c) 2021 Image Picker
 * Author: Nafis Islam Kabbo <kabboandreigns@gmail.com>
 */

package com.kabbodev.imagepicker.ui.image_picker

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.kabbodev.imagepicker.R
import com.kabbodev.imagepicker.base.BaseActivity
import com.kabbodev.imagepicker.databinding.ActivityImagePickerBinding
import com.kabbodev.imagepicker.listeners.OnFolderClickListener
import com.kabbodev.imagepicker.listeners.OnImageSelectListener
import com.kabbodev.imagepicker.model.Folder
import com.kabbodev.imagepicker.model.Image
import com.kabbodev.imagepicker.model.ImagePickerConfig
import com.kabbodev.imagepicker.utils.Constants
import com.kabbodev.imagepicker.utils.PermissionUtils
import com.kabbodev.imagepicker.utils.StorageUtils

class ImagePickerActivity : BaseActivity<ActivityImagePickerBinding, ImagePickerViewModel>(), OnFolderClickListener, OnImageSelectListener {

    private lateinit var config: ImagePickerConfig


    override fun getActivityBinding(inflater: LayoutInflater) = ActivityImagePickerBinding.inflate(inflater)

    override fun getViewModel(): Class<ImagePickerViewModel> = ImagePickerViewModel::class.java

    override fun setupAppTheme() {
        if (intent == null) {
            finish()
            return
        }

        config = intent.getParcelableExtra(Constants.EXTRA_CONFIG)!!
        config.initDefaultValues(this@ImagePickerActivity)

        // Setup status bar theme
        window.statusBarColor = Color.parseColor(config.statusBarColor)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = config.isLightStatusBar
    }

    override fun setupTheme() {
        viewModel.setConfig(config)
        viewModel.selectedImages.observe(this) {
            binding.toolbar.showDoneButton(config.isAlwaysShowDoneButton || it.isNotEmpty())
        }

        binding.toolbar.apply {
            config(config)
            setOnBackClickListener { onBackPressed() }
            setOnCameraClickListener { captureImageWithPermission() }
            setOnDoneClickListener { onDone() }
        }

        val initialFragment = if (config.isFolderMode) FolderFragment.newInstance(config.folderGridCount) else ImagesFragment.newInstance(config.imageGridCount)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, initialFragment)
            .commit()
    }

    override fun setupClickListeners() {

    }

    override fun onResume() {
        super.onResume()
        fetchDataWithPermission()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val fragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
        if (fragment != null && fragment is FolderFragment) binding.toolbar.setTitle(config.folderTitle)
    }

    private fun fetchDataWithPermission() {
        val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        PermissionUtils.checkPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            object : PermissionUtils.PermissionAskListener {
                override fun onNeedPermission() {
                    PermissionUtils.requestAllPermissions(this@ImagePickerActivity, permissions, Constants.RC_READ_EXTERNAL_STORAGE_PERMISSION)
                }

                override fun onPermissionPreviouslyDenied() {
                    PermissionUtils.requestAllPermissions(this@ImagePickerActivity, permissions, Constants.RC_READ_EXTERNAL_STORAGE_PERMISSION)
                }

                override fun onPermissionDisabled() {
                    binding.snackbar.visibility = View.VISIBLE
                    binding.snackbar.show(R.string.imagepicker_msg_no_external_storage_permission) {
                        PermissionUtils.openAppSettings(this@ImagePickerActivity)
                        binding.snackbar.visibility = View.GONE
                    }
                }

                override fun onPermissionGranted() {
                    fetchData()
                }
            })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            Constants.RC_READ_EXTERNAL_STORAGE_PERMISSION -> if (PermissionUtils.hasGranted(grantResults)) fetchData() else finish()
            Constants.RC_WRITE_EXTERNAL_STORAGE_PERMISSION -> if (PermissionUtils.hasGranted(grantResults)) captureImage()
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun fetchData() {
        viewModel.fetchImages()
    }

    private fun onDone() {
        val selectedImages = viewModel.selectedImages.value
        finishPickImages(selectedImages ?: arrayListOf())
    }

    private fun captureImageWithPermission() {
        StorageUtils.sdk29AndUp {
            captureImage()
        } ?: run {
            val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            PermissionUtils.checkPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                object : PermissionUtils.PermissionAskListener {
                    override fun onNeedPermission() {
                        PermissionUtils.requestAllPermissions(this@ImagePickerActivity, permissions, Constants.RC_WRITE_EXTERNAL_STORAGE_PERMISSION)
                    }

                    override fun onPermissionPreviouslyDenied() {
                        PermissionUtils.requestAllPermissions(this@ImagePickerActivity, permissions, Constants.RC_WRITE_EXTERNAL_STORAGE_PERMISSION)
                    }

                    override fun onPermissionDisabled() {
                        binding.snackbar.visibility = View.VISIBLE
                        binding.snackbar.show(R.string.imagepicker_msg_no_external_storage_permission) {
                            PermissionUtils.openAppSettings(this@ImagePickerActivity)
                            binding.snackbar.visibility = View.GONE
                        }
                    }

                    override fun onPermissionGranted() {
                        captureImage()
                    }
                })
        }
    }

    private fun captureImage() {

    }

    private fun finishPickImages(images: ArrayList<Image>) {
        val data = Intent()
        data.putParcelableArrayListExtra(Constants.EXTRA_IMAGES, images)
        setResult(Activity.RESULT_OK, data)
        finish()
    }

    override fun onFolderClick(folder: Folder) {
        supportFragmentManager.beginTransaction()
            .add(R.id.fragmentContainer, ImagesFragment.newInstance(folder.bucketId, config.imageGridCount))
            .addToBackStack(null)
            .commit()
        binding.toolbar.setTitle(folder.name)
    }

    override fun onSelectedImagesChanged(selectedImages: ArrayList<Image>) {
        viewModel.selectedImages.value = selectedImages
    }

    override fun onSingleModeImageSelected(image: Image) {
        finishPickImages(StorageUtils.singleListFromImage(image))
    }

}