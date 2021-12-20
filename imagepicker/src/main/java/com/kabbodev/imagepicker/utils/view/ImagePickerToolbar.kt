/*
 * Copyright (c) 2021 Image Picker
 * Author: Nafis Islam Kabbo <kabboandreigns@gmail.com>
 */

package com.kabbodev.imagepicker.utils.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import com.kabbodev.imagepicker.R
import com.kabbodev.imagepicker.model.ImagePickerConfig

class ImagePickerToolbar : RelativeLayout {

    private lateinit var titleText: TextView
    private lateinit var doneText: TextView
    private lateinit var backImage: AppCompatImageView
    private lateinit var cameraImage: AppCompatImageView

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context)
    }

    private fun init(context: Context) {
        View.inflate(context, R.layout.image_picker_toolbar, this)
        if (isInEditMode) {
            return
        }
        titleText = findViewById(R.id.text_toolbar_title)
        doneText = findViewById(R.id.text_toolbar_done)
        backImage = findViewById(R.id.image_toolbar_back)
        cameraImage = findViewById(R.id.image_toolbar_camera)
    }

    fun config(config: ImagePickerConfig) {
        setBackgroundColor(Color.parseColor(config.toolbarColor))

        titleText.text = if (config.isFolderMode) config.folderTitle else config.imageTitle
        titleText.setTextColor(Color.parseColor(config.toolbarTextColor))

        doneText.text = config.doneTitle
        doneText.setTextColor(Color.parseColor(config.toolbarTextColor))
        doneText.visibility = if (config.isAlwaysShowDoneButton) View.VISIBLE else View.GONE

        backImage.setColorFilter(Color.parseColor(config.toolbarIconColor))

        cameraImage.setColorFilter(Color.parseColor(config.toolbarIconColor))
        cameraImage.visibility = if (config.isShowCamera) View.VISIBLE else View.GONE
    }

    fun setTitle(title: String?) {
        titleText.text = title
    }

    fun showDoneButton(isShow: Boolean) {
        doneText.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    fun setOnBackClickListener(clickListener: OnClickListener) {
        backImage.setOnClickListener(clickListener)
    }

    fun setOnCameraClickListener(clickListener: OnClickListener) {
        cameraImage.setOnClickListener(clickListener)
    }

    fun setOnDoneClickListener(clickListener: OnClickListener) {
        doneText.setOnClickListener(clickListener)
    }
}