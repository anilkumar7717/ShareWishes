package com.example.sharewishes.interfaces

interface IRecyclerViewClickCallBack {
    fun clickAction(content: String, contentType: Int)
    fun addToFav(id: String, favourite: String)
    fun removeFromFav(id: String)

}