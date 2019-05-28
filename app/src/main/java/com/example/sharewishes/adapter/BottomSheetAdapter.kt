package com.example.sharewishes.adapter

import android.net.Uri
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.sharewishes.R
import com.example.sharewishes.interfaces.IBottomSheetListenerCallBack
import kotlinx.android.synthetic.main.row_bottom_sheet.view.*

class BottomSheetAdapter(
    private val data: List<*>,
    private val callBack: IBottomSheetListenerCallBack,
    private val iconType: String
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return BottomSheetViewHolder(
            layoutInflater.inflate(
                R.layout.row_bottom_sheet,
                parent,
                false
            )
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as BottomSheetViewHolder
        if (iconType=="emoji") {
            holder.quotesTxt.visibility = View.VISIBLE
            holder.quotesTxt.text = data[position].toString()
            holder.quotesTxt.setOnClickListener {
                callBack.iconCode(data[position].toString(),iconType)
            }
        }

        else if (iconType=="sticker") {
            holder.quotesTxtIcon.visibility = View.VISIBLE
            holder.quotesTxtIcon.setImageFromUrl(data[position].toString())
            holder.quotesTxtIcon.setOnClickListener {
                callBack.iconCode(data[position].toString(),iconType)
            }
        }

    }

    class BottomSheetViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val quotesTxt: TextView = view.bottomSheetTxt
        val quotesTxtIcon: AppCompatImageView = view.bottomSheetIcon
    }

    /*-- Extension function for AppCompatImageView --*/
    private fun AppCompatImageView.setImageFromUrl(url: String) {
        Glide.with(this)
            .load(url)
            .override(600, 600)
            .fitCenter()
            .into(this)
    }

}