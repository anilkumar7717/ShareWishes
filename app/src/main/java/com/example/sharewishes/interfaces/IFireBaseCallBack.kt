package com.example.sharewishes.interfaces

import com.example.sharewishes.models.HomeModel

interface IFireBaseCallBack {
    fun showProgress()
    fun hideProgress()
    fun showMessage(message: String)
    fun fireBaseData(fireBaseData: MutableList<HomeModel>)
}