package com.example.sharewishes.interfaces

import com.example.sharewishes.models.CategoryModel

interface IFireBaseCategoryCallBack {
    fun showProgress()
    fun hideProgress()
    fun showMessage(message: String)
    fun fireBaseCategoryData(fireBaseData: MutableList<CategoryModel>)
}