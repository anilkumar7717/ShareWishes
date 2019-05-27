package com.example.sharewishes.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import com.example.sharewishes.R
import com.example.sharewishes.interfaces.ICategoryRecyclerViewCallBack
import com.example.sharewishes.models.CategoryModel
import com.example.sharewishes.prefs
import kotlinx.android.synthetic.main.row_filter.view.*

class CategoryAdapter(
    private val data: List<CategoryModel>,
    private val callBack: ICategoryRecyclerViewCallBack
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return CategoryViewHolder (layoutInflater.inflate(R.layout.row_filter,parent,false))
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as CategoryViewHolder
        for (category in prefs.getFilterData()!!){
            if (category == data[position].categoryId){
                holder.selectCategory.performClick()
                callBack.checkBoxAction(category,true)
            }
        }
        holder.category.text = data[position].categoryName
        holder.selectCategory.setOnCheckedChangeListener { buttonView, isChecked ->
            callBack.checkBoxAction(data[position].categoryId,isChecked)
        }
    }

    class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val category: TextView = view.categoryName
        val selectCategory:CheckBox = view.selectCategoryCheck
    }
}