package com.example.sharewishes.views.fragments

import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.support.v4.content.res.ResourcesCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.sharewishes.R
import com.example.sharewishes.camera.CameraController
import com.example.sharewishes.interfaces.ICameraResultCallBack
import ja.burhanrashid52.photoeditor.OnPhotoEditorListener
import ja.burhanrashid52.photoeditor.PhotoEditor
import ja.burhanrashid52.photoeditor.ViewType
import kotlinx.android.synthetic.main.fragment_design.*
import kotlinx.android.synthetic.main.include_editor.*
import java.io.File


class DesignFragment : Fragment(), OnPhotoEditorListener, View.OnClickListener,
    ICameraResultCallBack {
    lateinit var mPhotoEditor: PhotoEditor
    private lateinit var cameraController: CameraController
    private val TAG = "DesignFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_design, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cameraController = activity?.let { CameraController(it, texture) }!!
        camera.setOnClickListener(this)
        brush.setOnClickListener(this)
        textEditor.setOnClickListener(this)
        eraser.setOnClickListener(this)
        sticker.setOnClickListener(this)
        emoji.setOnClickListener(this)
        discard.setOnClickListener(this)
        addQuote.setOnClickListener(this)
        cameraController.registerFragmentCommunication(this)
        texture.surfaceTextureListener = cameraController.textureListener
        //Use custom font using latest support library
        val mTextRobotoTf = activity?.let { ResourcesCompat.getFont(it, R.font.roboto_medium) }
        //loading font from assest
        val mEmojiTypeFace = Typeface.createFromAsset(resources.assets, "emojione_android.ttf")

        mPhotoEditor = PhotoEditor.Builder(activity, photoEditorView)
            .setPinchTextScalable(true)
            .setDefaultTextTypeface(mTextRobotoTf)
            .setDefaultEmojiTypeface(mEmojiTypeFace)
            .build()
        mPhotoEditor.setOnPhotoEditorListener(this)
    }

    override fun onEditTextChangeListener(rootView: View?, text: String?, colorCode: Int) {
        mPhotoEditor.editText(rootView, text, colorCode)
    }

    override fun onStartViewChangeListener(viewType: ViewType?) {
    }

    override fun onRemoveViewListener(numberOfAddedViews: Int) {

    }

    override fun onRemoveViewListener(viewType: ViewType?, numberOfAddedViews: Int) {
    }

    override fun onAddViewListener(viewType: ViewType?, numberOfAddedViews: Int) {
    }

    override fun onStopViewChangeListener(viewType: ViewType?) {
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == cameraController.REQUEST_CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                // close the app
                Toast.makeText(
                    activity,
                    "Sorry!!!, you can't use this app without granting permission",
                    Toast.LENGTH_LONG
                ).show()
                activity?.finish()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onClick(view: View?) {
        when (view) {
            camera -> {
                if (photoEditorView.visibility != View.VISIBLE) {
                    cameraController.takePicture()
                }
            }
            brush -> {
                mPhotoEditor.setBrushDrawingMode(true)
                mPhotoEditor.brushColor = resources.getColor(R.color.colorPrimary)
            }

            textEditor -> {
                mPhotoEditor.addText("hello", resources.getColor(R.color.colorPrimary))
            }

            eraser -> {
                mPhotoEditor.brushEraser()
            }

            sticker -> {
                mPhotoEditor.addImage(
                    BitmapFactory.decodeResource(
                        resources,
                        R.drawable.splash_logo
                    )
                )
            }

            emoji -> {
                mPhotoEditor.addEmoji(getEmojiByUnicode(0x1F60A))
            }

            discard -> {
                texture.visibility = View.VISIBLE
                photoEditorView.source.setImageURI(Uri.parse(""))
                photoEditorView.visibility = View.GONE
            }
        }
    }

    override fun onCaptureResult(file: File) {
        photoEditorView.visibility = View.VISIBLE
        photoEditorView.source.setImageURI(Uri.parse(file.absolutePath))
    }

    private fun getEmojiByUnicode(unicode: Int): String {
        return String(Character.toChars(unicode))
    }

}