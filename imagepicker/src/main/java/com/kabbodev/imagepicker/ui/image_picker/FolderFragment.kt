/*
 * Copyright (c) 2021 Image Picker
 * Author: Nafis Islam Kabbo <kabboandreigns@gmail.com>
 */

package com.kabbodev.imagepicker.ui.image_picker

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.kabbodev.imagepicker.R
import com.kabbodev.imagepicker.base.BaseFragment
import com.kabbodev.imagepicker.databinding.FragmentImagePickerBinding
import com.kabbodev.imagepicker.listeners.OnFolderClickListener
import com.kabbodev.imagepicker.model.CallbackStatus
import com.kabbodev.imagepicker.model.GridCount
import com.kabbodev.imagepicker.model.PickerResult
import com.kabbodev.imagepicker.ui.adapter.FolderPickerAdapter
import com.kabbodev.imagepicker.utils.LayoutManagerUtils
import com.kabbodev.imagepicker.utils.StorageUtils
import com.kabbodev.imagepicker.utils.view.GridSpacingItemDecoration

class FolderFragment : BaseFragment<FragmentImagePickerBinding, ImagePickerViewModel>() {

    companion object {
        const val GRID_COUNT = "GridCount"

        fun newInstance(gridCount: GridCount): FolderFragment {
            val fragment = FolderFragment()
            val args = Bundle()
            args.putParcelable(GRID_COUNT, gridCount)
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var folderAdapter: FolderPickerAdapter

    private lateinit var gridCount: GridCount
    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var itemDecoration: GridSpacingItemDecoration


    override fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?) = FragmentImagePickerBinding.inflate(inflater, container, false)

    override fun getViewModel() = ImagePickerViewModel::class.java

    override fun setupTheme() {
        gridCount = arguments?.getParcelable(GRID_COUNT)!!

        val config = viewModel.config
        folderAdapter = FolderPickerAdapter(activity as OnFolderClickListener)
        gridLayoutManager = LayoutManagerUtils.newInstance(requireContext(), gridCount)
        itemDecoration = GridSpacingItemDecoration(gridLayoutManager.spanCount, resources.getDimension(R.dimen.image_picker_grid_spacing).toInt())

        binding.apply {
            root.setBackgroundColor(Color.parseColor(config.backgroundColor))
            progressIndicator.setIndicatorColor(Color.parseColor(config.progressIndicatorColor))

            recyclerView.apply {
                setHasFixedSize(true)
                layoutManager = gridLayoutManager
                addItemDecoration(itemDecoration)
                adapter = folderAdapter
            }
        }

        viewModel.result.observe(viewLifecycleOwner) { pickerResult ->
            pickerResult?.let {
                handleResult(it)
            }
        }
    }

    override fun setupClickListeners() {

    }

    private fun handleResult(result: PickerResult) {
        if (result.status is CallbackStatus.SUCCESS && result.images.isNotEmpty()) {
            val folders = StorageUtils.folderListFromImages(result.images)
            folderAdapter.updateList(folders)
            binding.recyclerView.visibility = View.VISIBLE
        } else {
            binding.recyclerView.visibility = View.GONE
        }

        binding.apply {
            emptyText.visibility = if (result.status is CallbackStatus.SUCCESS && result.images.isEmpty()) View.VISIBLE else View.GONE
            progressIndicator.visibility = if (result.status is CallbackStatus.FETCHING) View.VISIBLE else View.GONE
        }
    }

    override fun handleOnConfigurationChanged() {
        binding.recyclerView.removeItemDecoration(itemDecoration)

        val newSpanCount = LayoutManagerUtils.getSpanCountForCurrentConfiguration(requireContext(), gridCount)
        itemDecoration = GridSpacingItemDecoration(gridLayoutManager.spanCount, resources.getDimension(R.dimen.image_picker_grid_spacing).toInt())
        gridLayoutManager.spanCount = newSpanCount
        binding.recyclerView.addItemDecoration(itemDecoration)
    }

}