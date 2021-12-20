/*
 * Copyright (c) 2021 Image Picker
 * Author: Nafis Islam Kabbo <kabboandreigns@gmail.com>
 */

package com.kabbodev.imagepicker.ui.adapter

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import com.kabbodev.imagepicker.R
import com.kabbodev.imagepicker.base.BaseAdapter
import com.kabbodev.imagepicker.databinding.ItemImagePickerImageBinding
import com.kabbodev.imagepicker.listeners.OnImageSelectListener
import com.kabbodev.imagepicker.model.Image
import com.kabbodev.imagepicker.model.ImagePickerConfig
import com.kabbodev.imagepicker.model.ImageStatus
import com.kabbodev.imagepicker.utils.ImageUtils.loadCenterCropImage
import com.kabbodev.imagepicker.utils.StorageUtils

class ImagePickerAdapter(
    private val config: ImagePickerConfig,
    private val listener: OnImageSelectListener
) : BaseAdapter<ItemImagePickerImageBinding, Image>() {

    private val differ = AsyncListDiffer(this, getDiffUtilCallback())
    private val selectedImagesList: ArrayList<Image> = ArrayList()


    override fun getLayoutBinding(inflater: LayoutInflater, container: ViewGroup?): ItemImagePickerImageBinding =
        ItemImagePickerImageBinding.inflate(inflater, container, false)

    override fun getDiffer(): AsyncListDiffer<Image> = differ

    override fun areItemsSame(oldItem: Image, newItem: Image): Boolean = oldItem.bucketId == newItem.bucketId

    override fun areContentsSame(oldItem: Image, newItem: Image): Boolean = oldItem == newItem

    override fun onBindViewHolder(holder: MyViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)

        } else {
            when {
                payloads.any { it is ImageStatus.ImageSelectedOrUpdated } -> {
                    if (config.isShowNumberIndicator) {
                        val image = getDiffer().currentList[holder.bindingAdapterPosition]
                        val selectedIndex = StorageUtils.findImageIndex(image, selectedImagesList)

                        holder.viewBinding.textSelectedNumber.text = (selectedIndex + 1).toString()
                        holder.viewBinding.textSelectedNumber.visibility = View.VISIBLE
                        holder.viewBinding.imageSelectedIcon.visibility = View.GONE

                    } else {
                        holder.viewBinding.imageSelectedIcon.visibility = View.VISIBLE
                        holder.viewBinding.textSelectedNumber.visibility = View.GONE
                    }
                    setupItemForeground(holder.viewBinding.imageThumbnail, true)
                }
                payloads.any { it is ImageStatus.ImageUnSelected } -> {
                    if (config.isShowNumberIndicator)
                        holder.viewBinding.textSelectedNumber.visibility = View.GONE
                    else
                        holder.viewBinding.imageSelectedIcon.visibility = View.GONE

                    setupItemForeground(holder.viewBinding.imageThumbnail, false)
                }
                else -> onBindViewHolder(holder, position)
            }
        }
    }

    override fun bindView(holder: MyViewHolder, item: Image) {
        holder.viewBinding.apply {
            val selectedIndex = StorageUtils.findImageIndex(item, selectedImagesList)
            val isSelected = config.isMultipleMode && selectedIndex != -1

            imageThumbnail.loadCenterCropImage(item.uri)
            setupItemForeground(imageThumbnail, isSelected)

            gifIndicator.visibility = if (StorageUtils.isGifFormat(item)) View.VISIBLE else View.GONE
            imageSelectedIcon.visibility = if (isSelected && !config.isShowNumberIndicator) View.VISIBLE else View.GONE
            textSelectedNumber.visibility = if (isSelected && config.isShowNumberIndicator) View.VISIBLE else View.GONE
            if (textSelectedNumber.visibility == View.VISIBLE) textSelectedNumber.text = (selectedIndex + 1).toString()

            root.setOnClickListener { selectOrRemoveImage(root.context, holder.bindingAdapterPosition, item) }
            executePendingBindings()
        }
    }

    fun updateSelectedImages(selectedImages: ArrayList<Image>) {
        this.selectedImagesList.clear()
        this.selectedImagesList.addAll(selectedImages)

        val list = getDiffer().currentList
        getDiffer().submitList(emptyList())
        getDiffer().submitList(list)
    }

    private fun setupItemForeground(view: View, isSelected: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            view.foreground = if (isSelected) ColorDrawable(ContextCompat.getColor(view.context, R.color.image_picker_black_alpha_30)) else null
        }
    }

    private fun selectOrRemoveImage(context: Context, pos: Int, image: Image) {
        if (config.isMultipleMode) {
            val selectedIndex = StorageUtils.findImageIndex(image, selectedImagesList)

            if (selectedIndex != -1) {
                selectedImagesList.removeAt(selectedIndex)
                notifyItemChanged(pos, ImageStatus.ImageUnSelected)

                val indexes = StorageUtils.findImageIndexes(selectedImagesList, ArrayList<Image>(getDiffer().currentList))
                for (index in indexes) {
                    notifyItemChanged(index, ImageStatus.ImageSelectedOrUpdated)
                }

            } else {
                if (selectedImagesList.size >= config.maxSize) {
                    val message = if (config.limitMessage != null)
                        config.limitMessage!!
                    else
                        String.format(context.resources.getString(R.string.imagepicker_msg_limit_images), config.maxSize)

                    Toast.makeText(context.applicationContext, message, Toast.LENGTH_SHORT).show()
                    return

                } else {
                    selectedImagesList.add(image)
                    notifyItemChanged(pos, ImageStatus.ImageSelectedOrUpdated)
                }
            }
            listener.onSelectedImagesChanged(selectedImagesList)

        } else {
            listener.onSingleModeImageSelected(image)
        }
    }

}