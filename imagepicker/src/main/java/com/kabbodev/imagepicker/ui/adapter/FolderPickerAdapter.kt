/*
 * Copyright (c) 2021 Image Picker
 * Author: Nafis Islam Kabbo <kabboandreigns@gmail.com>
 */

package com.kabbodev.imagepicker.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import com.kabbodev.imagepicker.base.BaseAdapter
import com.kabbodev.imagepicker.databinding.ItemImagePickerFolderBinding
import com.kabbodev.imagepicker.listeners.OnFolderClickListener
import com.kabbodev.imagepicker.model.Folder

class FolderPickerAdapter(private val listener: OnFolderClickListener) : BaseAdapter<ItemImagePickerFolderBinding, Folder>() {

    private val differ = AsyncListDiffer(this, getDiffUtilCallback())


    override fun getLayoutBinding(inflater: LayoutInflater, container: ViewGroup?): ItemImagePickerFolderBinding =
        ItemImagePickerFolderBinding.inflate(inflater, container, false)

    override fun getDiffer(): AsyncListDiffer<Folder> = differ

    override fun areItemsSame(oldItem: Folder, newItem: Folder): Boolean = oldItem.bucketId == newItem.bucketId

    override fun areContentsSame(oldItem: Folder, newItem: Folder): Boolean = oldItem == newItem

    override fun bindView(holder: MyViewHolder, item: Folder) {
        holder.viewBinding.apply {
            setItem(item)
            root.setOnClickListener { listener.onFolderClick(item) }
            executePendingBindings()
        }
    }

}