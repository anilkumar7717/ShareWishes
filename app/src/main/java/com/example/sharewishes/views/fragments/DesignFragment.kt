package com.example.sharewishes.views.fragments

import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.support.annotation.RequiresApi
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.app.Fragment
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.sharewishes.R
import com.example.sharewishes.adapter.BottomSheetAdapter
import com.example.sharewishes.camera.CameraController
import com.example.sharewishes.interfaces.IBottomSheetListenerCallBack
import com.example.sharewishes.interfaces.ICameraResultCallBack
import ja.burhanrashid52.photoeditor.OnPhotoEditorListener
import ja.burhanrashid52.photoeditor.PhotoEditor
import ja.burhanrashid52.photoeditor.ViewType
import kotlinx.android.synthetic.main.fragment_design.*
import kotlinx.android.synthetic.main.include_bottom_sheet.*
import kotlinx.android.synthetic.main.include_editor.*
import java.io.File
import java.io.IOException
import java.net.URL


class DesignFragment : Fragment(), OnPhotoEditorListener, View.OnClickListener,
    ICameraResultCallBack, IBottomSheetListenerCallBack {
    lateinit var mPhotoEditor: PhotoEditor
    private lateinit var cameraController: CameraController
    private val TAG = "DesignFragment"
    private lateinit var behavior: BottomSheetBehavior<*>

    private lateinit var bottomSheetAdapter: BottomSheetAdapter
    private lateinit var iconsList: MutableList<*>
    private lateinit var stickerList: ArrayList<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_design, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val policy = StrictMode.ThreadPolicy.Builder()
            .permitAll().build()
        StrictMode.setThreadPolicy(policy)
        cameraController = activity?.let { CameraController(it, texture) }!!
        camera.setOnClickListener(this)
        brush.setOnClickListener(this)
        textEditor.setOnClickListener(this)
        eraser.setOnClickListener(this)
        sticker.setOnClickListener(this)
        emoji.setOnClickListener(this)
        discard.setOnClickListener(this)
        addQuote.setOnClickListener(this)
        val viewa: View = coordinator.findViewById(R.id.bottom_sheet)
        behavior = BottomSheetBehavior.from(viewa)
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

        behavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
            }

        })
    }

    override fun onEditTextChangeListener(rootView: View?, text: String?, colorCode: Int) {
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
                mPhotoEditor.addText(
                    "Hello",
                    resources.getColor(R.color.colorPrimary)
                )
            }

            eraser -> {
                mPhotoEditor.brushEraser()
            }

            sticker -> {
                /*mPhotoEditor.addImage(
                    BitmapFactory.decodeResource(
                        resources,
                        R.drawable.splash_logo
                    )
                )*/
                if (photoEditorView.visibility == View.VISIBLE) {
                    getIcons("sticker")
                }
            }

            emoji -> {
                if (photoEditorView.visibility == View.VISIBLE) {
                    getIcons("emoji")
                }
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

    private fun getIcons(iconType: String) {
        bottomSheetRecycler.setHasFixedSize(true)
        if (iconType == "emoji") {
            bottomSheetRecycler.layoutManager = GridLayoutManager(activity, 7)
            iconsList = PhotoEditor.getEmojis(activity)
            bottomSheetAdapter = BottomSheetAdapter(iconsList, this, iconType)
        } else if (iconType == "sticker") {
            bottomSheetRecycler.layoutManager = GridLayoutManager(activity, 2)
            stickerList = ArrayList(10)
            stickerList.add(
                0,
                "https://i1.wp.com/helmspta.org/wp-content/uploads/2017/10/happy-bday.png?fit=248%2C203&ssl=1&w=1400"
            )
            stickerList.add(
                1,
                "https://banner2.kisspng.com/20171127/91b/happy-birthday-transparent-sticker-with-pink-balloon-5a1c6db0d40395.4050977415118125288684.jpg"
            )
            stickerList.add(
                2,
                "http://southhallcrossfit.com/wp-content/uploads/2016/12/christmas_lights22long.png"
            )
            stickerList.add(
                3,
                "https://ih1.redbubble.net/image.454254746.9511/st%2Csmall%2C215x235-pad%2C210x230%2Cf8f8f8.lite-1u1.jpg"
            )
            stickerList.add(
                4,
                "https://i1.wp.com/helmspta.org/wp-content/uploads/2017/10/happy-bday.png?fit=248%2C203&ssl=1&w=1400"
            )
            stickerList.add(
                5,
                "https://png.pngtree.com/element_pic/00/16/09/0257c8593c82482.jpg"
            )
            stickerList.add(
                6,
                "https://png.pngtree.com/element_pic/16/11/10/afba435306ddc6d8b5b8cbd8e44963d6.jpg"
            )
            stickerList.add(
                7,
                "http://dl.glitter-graphics.com/pub/2814/2814359c3q66tcwus.gif"
            )
            iconsList = stickerList
            bottomSheetAdapter = BottomSheetAdapter(iconsList, this, iconType)
        }
        bottomSheetRecycler.adapter = bottomSheetAdapter
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun iconCode(icon: String, iconType: String) {
        if (iconType == "emoji") {
            mPhotoEditor.addEmoji(icon)
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
        } else if (iconType == "sticker") {
            println("--path---" + Uri.parse(icon))
            try {
                val url = URL(icon)
                val image = BitmapFactory.decodeStream(url.openConnection().getInputStream())
                mPhotoEditor.addImage(image)
            } catch (e: IOException) {
                System.out.println(e)
            }
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

}