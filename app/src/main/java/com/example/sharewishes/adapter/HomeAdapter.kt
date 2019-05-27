package com.example.sharewishes.adapter

import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.sharewishes.R
import com.example.sharewishes.interfaces.IRecyclerViewClickCallBack
import com.example.sharewishes.models.HomeModel
import kotlinx.android.synthetic.main.include_favourite.view.*
import kotlinx.android.synthetic.main.row_homeadapter_image.view.*
import kotlinx.android.synthetic.main.row_homeadapter_text.view.*
import kotlinx.android.synthetic.main.row_homeadapter_video.view.*


class HomeAdapter(
    private val data: List<HomeModel>,
    private val callBack: IRecyclerViewClickCallBack
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val TEXT = 1
        const val IMAGE = 2
        const val VIDEO = 3
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        when (viewType) {
            TEXT -> return TextViewHolder(
                layoutInflater.inflate(
                    R.layout.row_homeadapter_text,
                    parent,
                    false
                )
            )
            IMAGE -> return ImageViewHolder(
                layoutInflater.inflate(
                    R.layout.row_homeadapter_image,
                    parent,
                    false
                )
            )
            VIDEO -> return VideoViewHolder(
                layoutInflater.inflate(
                    R.layout.row_homeadapter_video,
                    parent,
                    false
                )
            )
        }
        return object : RecyclerView.ViewHolder(View(parent.context)) {}
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        when (holder.itemViewType) {
            TEXT -> showTextView(holder as TextViewHolder, position)
            IMAGE -> showImageView(holder as ImageViewHolder, position)
            VIDEO -> showVideoView(holder as VideoViewHolder, position)
        }
    }

    private fun showVideoView(videoViewHolder: VideoViewHolder, position: Int) {
        videoViewHolder.videoContent.setImageFromUrl("https://i.ytimg.com/vi/${data[position].content}/default.jpg")
        if (data[position].favourite == "true") {
            videoViewHolder.ivFavourite.setQuotesFavourite()
        }
        videoViewHolder.videoContent.setOnClickListener {
            callBack.clickAction(
                data[videoViewHolder.adapterPosition].content,
                data[videoViewHolder.adapterPosition].viewType
            )
        }
        videoViewHolder.ivFavourite.setOnClickListener {
            if (data[position].favourite == "true") {
                videoViewHolder.ivFavourite.removeQuotesFavourite()
                callBack.removeFromFav(data[videoViewHolder.adapterPosition].documentId)
            } else {
                videoViewHolder.ivFavourite.setQuotesFavourite()
                callBack.addToFav(data[videoViewHolder.adapterPosition].documentId, "true")
            }
        }
    }

    private fun showImageView(imageViewHolder: ImageViewHolder, position: Int) {
        imageViewHolder.imageContent.setImageFromUrl(data[position].content)
        if (data[position].favourite == "true") {
            imageViewHolder.ivFavourite.setQuotesFavourite()
        }
        imageViewHolder.imageContent.setOnClickListener {
            callBack.clickAction(
                data[imageViewHolder.adapterPosition].content,
                data[imageViewHolder.adapterPosition].viewType
            )
        }

        imageViewHolder.ivFavourite.setOnClickListener {
            if (data[position].favourite == "true") {
                imageViewHolder.ivFavourite.removeQuotesFavourite()
                callBack.removeFromFav(data[imageViewHolder.adapterPosition].documentId)
            } else {
                imageViewHolder.ivFavourite.setQuotesFavourite()
                callBack.addToFav(data[imageViewHolder.adapterPosition].documentId, "true")
            }
        }
    }

    private fun showTextView(textViewHolder: TextViewHolder, position: Int) {
        textViewHolder.textContent.text = data[position].content
        if (data[position].favourite == "true") {
            textViewHolder.ivFavourite.setQuotesFavourite()
        }
        textViewHolder.ivFavourite.setOnClickListener {
            if (data[position].favourite == "true") {
                textViewHolder.ivFavourite.removeQuotesFavourite()
                callBack.removeFromFav(data[textViewHolder.adapterPosition].documentId)
            } else {
                textViewHolder.ivFavourite.setQuotesFavourite()
                callBack.addToFav(data[textViewHolder.adapterPosition].documentId, "true")
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return data[position].viewType
    }

    class TextViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textContent: TextView = view.textView
        val ivFavourite: ImageButton = view.ivFavourite
    }

    class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageContent: AppCompatImageView = view.imageView
        val ivFavourite: ImageButton = view.ivFavourite

    }

    class VideoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val videoContent: AppCompatImageView = view.videoImage
        val ivFavourite: ImageButton = view.ivFavourite
    }

    /*-- Extension function for AppCompatImageView --*/
    private fun AppCompatImageView.setImageFromUrl(url: String) {
        Glide.with(this)
            .load(url)
            .override(600, 600)
            .fitCenter()
            .into(this)
    }

    private fun ImageButton.setQuotesFavourite() {
        this.setImageResource(R.drawable.ic_favorite)
    }

    private fun ImageButton.removeQuotesFavourite() {
        this.setImageResource(R.drawable.ic_favorite_border)
    }

}


