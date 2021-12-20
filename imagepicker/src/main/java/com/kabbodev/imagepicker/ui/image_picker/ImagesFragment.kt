/*
 * Copyright (c) 2021 Image Picker
 * Author: Nafis Islam Kabbo <kabboandreigns@gmail.com>
 */

package com.kabbodev.imagepicker.ui.image_picker

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.kabbodev.imagepicker.R
import com.kabbodev.imagepicker.base.BaseFragment
import com.kabbodev.imagepicker.databinding.FragmentImagePickerBinding
import com.kabbodev.imagepicker.listeners.OnImageSelectListener
import com.kabbodev.imagepicker.model.CallbackStatus
import com.kabbodev.imagepicker.model.GridCount
import com.kabbodev.imagepicker.model.Image
import com.kabbodev.imagepicker.model.PickerResult
import com.kabbodev.imagepicker.ui.adapter.ImagePickerAdapter
import com.kabbodev.imagepicker.utils.LayoutManagerUtils
import com.kabbodev.imagepicker.utils.StorageUtils
import com.kabbodev.imagepicker.utils.view.GridSpacingItemDecoration

class ImagesFragment : BaseFragment<FragmentImagePickerBinding, ImagePickerViewModel>() {

    companion object {
        const val BUCKET_ID = "BucketId"
        const val GRID_COUNT = "GridCount"

        fun newInstance(bucketId: Long, gridCount: GridCount): ImagesFragment {
            val fragment = ImagesFragment()
            val args = Bundle()
            args.putLong(BUCKET_ID, bucketId)
            args.putParcelable(GRID_COUNT, gridCount)
            fragment.arguments = args
            return fragment
        }

        fun newInstance(gridCount: GridCount): ImagesFragment {
            val fragment = ImagesFragment()
            val args = Bundle()
            args.putParcelable(GRID_COUNT, gridCount)
            fragment.arguments = args
            return fragment
        }
    }

    private var bucketId: Long? = null
    private lateinit var imageAdapter: ImagePickerAdapter
    private lateinit var gridCount: GridCount
    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var itemDecoration: GridSpacingItemDecoration

    private val selectedImageObserver = object : Observer<ArrayList<Image>> {
        override fun onChanged(it: ArrayList<Image>) {
            imageAdapter.updateSelectedImages(it)
            viewModel.selectedImages.removeObserver(this)
        }
    }


    override fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?) = FragmentImagePickerBinding.inflate(inflater, container, false)

    override fun getViewModel() = ImagePickerViewModel::class.java

    override fun setupTheme() {
        bucketId = arguments?.getLong(BUCKET_ID)
        gridCount = arguments?.getParcelable(GRID_COUNT)!!

        val config = viewModel.config

        imageAdapter = ImagePickerAdapter(config, activity as OnImageSelectListener)
        gridLayoutManager = LayoutManagerUtils.newInstance(requireContext(), gridCount)
        itemDecoration = GridSpacingItemDecoration(gridLayoutManager.spanCount, resources.getDimension(R.dimen.image_picker_grid_spacing).toInt())

        binding.apply {
            root.setBackgroundColor(Color.parseColor(config.backgroundColor))
            progressIndicator.setIndicatorColor(Color.parseColor(config.progressIndicatorColor))
            recyclerView.apply {
                setHasFixedSize(true)
                layoutManager = gridLayoutManager
                addItemDecoration(itemDecoration)
                adapter = imageAdapter
            }
        }

        viewModel.apply {
            result.observe(viewLifecycleOwner) { pickerResult ->
                pickerResult?.let {
                    handleResult(it)
                }
            }
            selectedImages.observe(viewLifecycleOwner, selectedImageObserver)
        }
    }

    override fun setupClickListeners() {

    }

    private fun handleResult(result: PickerResult) {
        if (result.status is CallbackStatus.SUCCESS) {
            val images = StorageUtils.filterImages(result.images, bucketId)
            if (images.isNotEmpty()) {
                imageAdapter.updateList(images)
                binding.recyclerView.visibility = View.VISIBLE
            } else {
                binding.recyclerView.visibility = View.GONE
            }
        } else {
            binding.recyclerView.visibility = View.GONE
        }

        binding.apply {
            emptyText.visibility = if (result.status is CallbackStatus.SUCCESS && result.images.isEmpty()) View.VISIBLE else View.GONE
            progressIndicator.visibility = if (result.status is CallbackStatus.FETCHING) View.VISIBLE else View.GONE
        }
    }

    override fun handleOnConfigurationChanged() {
        val newSpanCount = LayoutManagerUtils.getSpanCountForCurrentConfiguration(requireContext(), gridCount)
        itemDecoration = GridSpacingItemDecoration(gridLayoutManager.spanCount, resources.getDimension(R.dimen.image_picker_grid_spacing).toInt())
        gridLayoutManager.spanCount = newSpanCount
        binding.recyclerView.addItemDecoration(itemDecoration)
    }

}