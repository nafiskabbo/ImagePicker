/*
 * Copyright (c) 2021 Image Picker
 * Author: Nafis Islam Kabbo <kabboandreigns@gmail.com>
 */

package com.kabbodev.imagepicker.ui.image_picker

import android.app.Application
import android.content.ContentUris
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kabbodev.imagepicker.model.CallbackStatus
import com.kabbodev.imagepicker.model.Image
import com.kabbodev.imagepicker.model.ImagePickerConfig
import com.kabbodev.imagepicker.model.PickerResult
import com.kabbodev.imagepicker.utils.StorageUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.lang.ref.WeakReference

class ImagePickerViewModel(application: Application) : AndroidViewModel(application) {

    private var job: Job? = null
    private val contextRef = WeakReference(application.applicationContext)

    private var _config: ImagePickerConfig = ImagePickerConfig()
    val config: ImagePickerConfig get() = _config

    var selectedImages: MutableLiveData<ArrayList<Image>> = MutableLiveData()
    val result = MutableLiveData(PickerResult(CallbackStatus.IDLE, arrayListOf()))

    fun setConfig(updatedConfig: ImagePickerConfig) {
        this._config = updatedConfig
        selectedImages = MutableLiveData(updatedConfig.selectedImages)
    }

    fun fetchImages() {
        if (job != null) return

        result.postValue(PickerResult(CallbackStatus.FETCHING, arrayListOf()))
        job = viewModelScope.launch {
            try {
                val images = fetchImagesFromExternalStorage()
                result.postValue(PickerResult(CallbackStatus.SUCCESS, images))
            } catch (e: Exception) {
                result.postValue(PickerResult(CallbackStatus.SUCCESS, arrayListOf()))
            } finally {
                job = null
            }
        }
    }

    private suspend fun fetchImagesFromExternalStorage(): ArrayList<Image> {
        if (contextRef.get() == null) return arrayListOf()

        return withContext(Dispatchers.IO) {
            val projection = arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.BUCKET_ID,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME
            )

            val imageCollectionUri = StorageUtils.getImageCollectionUri()

            contextRef.get()!!.contentResolver.query(
                imageCollectionUri,
                projection,
                null,
                null,
                MediaStore.Images.Media.DATE_ADDED + " DESC"
            )?.use { cursor ->
                val images = arrayListOf<Image>()

                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
                val bucketIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID)
                val bucketNameColumn = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)

                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idColumn)
                    val name = cursor.getString(nameColumn)
                    val bucketId = cursor.getLong(bucketIdColumn)
                    val bucketName = cursor.getString(bucketNameColumn)

                    val uri = ContentUris.withAppendedId(imageCollectionUri, id)

                    val image = Image(uri, name, bucketId, bucketName)
                    images.add(image)
                }
                cursor.close()
                images

            } ?: throw IOException()
        }
    }

}