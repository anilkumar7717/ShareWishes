package com.example.sharewishes.views.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sharewishes.MainActivity
import com.example.sharewishes.R
import com.example.sharewishes.adapter.CategoryAdapter
import com.example.sharewishes.firebasedata.FireBaseGetData
import com.example.sharewishes.interfaces.ICategoryRecyclerViewCallBack
import com.example.sharewishes.interfaces.IFireBaseCategoryCallBack
import com.example.sharewishes.models.CategoryModel
import com.example.sharewishes.prefs
import com.example.sharewishes.utils.Utils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_filter.*
import kotlinx.android.synthetic.main.include_recyclerview.*

class FilterFragment : Fragment(), IFireBaseCategoryCallBack, ICategoryRecyclerViewCallBack {
    lateinit var categoryAdapter: CategoryAdapter
    lateinit var categoryData: MutableList<CategoryModel>
    lateinit var queryData: MutableList<String>
    lateinit var filterText: String
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_contents.layoutManager = LinearLayoutManager(activity)
        categoryData = mutableListOf()
        queryData = mutableListOf()
        categoryAdapter = CategoryAdapter(categoryData, this)
        rv_contents.adapter = categoryAdapter
        FireBaseGetData.instance.initializeFireBaseCategoryCallBack(this)
        FireBaseGetData.instance.getCategory()
        filterBtn.setOnClickListener {
            if (queryData.isNotEmpty()) {
                prefs.setfilterData(queryData)
                (activity as MainActivity).navigation.selectedItemId = R.id.navigation_home
               // (activity as MainActivity).addFragmentToUi(HomeFragment::class.java)
                (activity as MainActivity).iFragmentCommunication.onDataUpdate()
            }
        }
    }

    override fun showProgress() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progressBar.visibility = View.GONE
    }

    override fun showMessage(message: String) {
        Utils.showToastMessage(activity, message)
    }

    override fun fireBaseCategoryData(fireBaseData: MutableList<CategoryModel>) {
        categoryData.addAll(fireBaseData)
        categoryAdapter.notifyDataSetChanged()
    }

    override fun checkBoxAction(categoryId: String, isChecked: Boolean) {
        if (isChecked) {
            filterBtn.visibility = View.VISIBLE
            queryData.add(categoryId)
            filterText = "Search ${queryData.size} Categories"
            filterBtn.text = filterText
        } else {
            queryData.remove(categoryId)
            filterText = "Search ${queryData.size} Categories"
            filterBtn.text = filterText
            if (queryData.isEmpty()) {
                filterBtn.visibility = View.GONE
                prefs.clearSharedPreference()
            }
        }
    }


}


