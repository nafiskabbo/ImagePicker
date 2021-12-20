/*
 * Copyright (c) 2021 Image Picker
 * Author: Nafis Islam Kabbo <kabboandreigns@gmail.com>
 */

package com.kabbodev.imagepicker.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

abstract class BaseActivity<dataBinding : ViewDataBinding, viewModel : ViewModel> : AppCompatActivity() {

    protected lateinit var binding: dataBinding
    protected lateinit var viewModel: viewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupAppTheme()
        binding = getActivityBinding(layoutInflater)
        binding.apply {
            lifecycleOwner = this@BaseActivity
        }
        setContentView(binding.root)

        val factory = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        viewModel = ViewModelProvider(this, factory).get(getViewModel())

        setupTheme()
        setupClickListeners()
    }

    abstract fun getActivityBinding(inflater: LayoutInflater): dataBinding

    abstract fun getViewModel(): Class<viewModel>

    abstract fun setupAppTheme()

    abstract fun setupTheme()

    abstract fun setupClickListeners()

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) onBackPressed()
        return super.onOptionsItemSelected(item)
    }

}