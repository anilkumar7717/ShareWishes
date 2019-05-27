package com.example.sharewishes.views.dailogs

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AppCompatDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.sharewishes.R
import com.example.sharewishes.utils.AppConstants
import kotlinx.android.synthetic.main.dialog_fullscreen_image.*

class FullScreenImageDialog : AppCompatDialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.fullScreenDialog)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_fullscreen_image, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = arguments
        if (bundle != null && bundle.containsKey(AppConstants.EXTRA_CONTENT)) {
            Glide.with(this).load(bundle[AppConstants.EXTRA_CONTENT]).into(iv_fullScreen);
        }

        dialog_frame_container.setOnClickListener {
            dismiss()
        }
    }
}