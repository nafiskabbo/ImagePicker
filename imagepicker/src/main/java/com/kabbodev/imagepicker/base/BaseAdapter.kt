/*
 * Copyright (c) 2021 Image Picker
 * Author: Nafis Islam Kabbo <kabboandreigns@gmail.com>
 */

package com.kabbodev.imagepicker.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

/**
 * @param dataBinding The Item Layout File Data Binding
 * @param T data class of list
 */
abstract class BaseAdapter<dataBinding : ViewDataBinding, T> : RecyclerView.Adapter<BaseAdapter<dataBinding, T>.MyViewHolder>() {


    abstract fun getLayoutBinding(inflater: LayoutInflater, container: ViewGroup?): dataBinding

    abstract fun getDiffer(): AsyncListDiffer<T>

    abstract fun areItemsSame(oldItem: T, newItem: T): Boolean

    abstract fun areContentsSame(oldItem: T, newItem: T): Boolean

    abstract fun bindView(holder: MyViewHolder, item: T)


    fun getDiffUtilCallback(): DiffUtil.ItemCallback<T> = diffUtilCallBack

    fun updateList(updated: List<T>) {
        if (updated.isEmpty()) {
            getDiffer().submitList(emptyList())
            return
        }
        getDiffer().submitList(updated)
    }

    fun addItem(item: T) {
        val list: ArrayList<T> = ArrayList(getDiffer().currentList)
        list.add(item)

        updateList(list)
    }

    fun removeItem(pos: Int) {
        val list: ArrayList<T> = ArrayList(getDiffer().currentList)
        list.removeAt(pos)

        updateList(list)
    }


    private val diffUtilCallBack = object : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T, newItem: T): Boolean = areItemsSame(oldItem, newItem)

        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean = areContentsSame(oldItem, newItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder =
        MyViewHolder(getLayoutBinding(LayoutInflater.from(parent.context), parent))

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getDiffer().currentList[holder.bindingAdapterPosition]
        bindView(holder, item)
    }

    override fun getItemCount(): Int = getDiffer().currentList.size

    inner class MyViewHolder(val viewBinding: dataBinding) : RecyclerView.ViewHolder(viewBinding.root)

}