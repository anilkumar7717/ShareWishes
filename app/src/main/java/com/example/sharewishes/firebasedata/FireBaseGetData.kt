package com.example.sharewishes.firebasedata

import android.util.Log
import com.example.sharewishes.interfaces.IFireBaseCallBack
import com.example.sharewishes.interfaces.IFireBaseCategoryCallBack
import com.example.sharewishes.models.CategoryModel
import com.example.sharewishes.models.HomeModel
import com.example.sharewishes.utils.AppConstants
import com.example.sharewishes.utils.Utils.parseIntValue
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore


class FireBaseGetData private constructor() {
    // Access a Cloud FireStore INSTANCE from your Activity
    var db = FirebaseFirestore.getInstance()
    private var iFireBaseCallBack: IFireBaseCallBack? = null
    private var iFireBaseCategoryCallBack: IFireBaseCategoryCallBack? = null


    object Holder {
        val value = synchronized(FireBaseGetData::class.java) { FireBaseGetData() }
    }

    /*-- creating singleton --*/
    companion object {
        val instance: FireBaseGetData by lazy { Holder.value }
    }

    @Suppress("UNCHECKED_CAST")
    fun getQuotesFromFireStore() {
        iFireBaseCallBack?.showProgress()
        db.collection(FireBaseConstants.COLLECTION_PARENT)
            .get()
            .addOnSuccessListener { result ->
                iFireBaseCallBack?.hideProgress()
                for (document in result) {
                    prepareData(listOf(document.data))
                }
            }
            .addOnFailureListener { exception ->
                iFireBaseCallBack?.showMessage(AppConstants.NO_DATA)
                Log.w("collection error", "Error getting documents.", exception)
            }
    }

    private fun prepareData(list: List<Map<String, Any>>) {
        println("----0---" + list[0])
        val homeData = mutableListOf<HomeModel>()
        for (data in list) {
            val contentType = parseIntValue(data[FireBaseConstants.FIELD_CONTENT_TYPE].toString())
            if (contentType != 0) {
                val homeModel = HomeModel(
                    data[FireBaseConstants.FIELD_CATEGORY_ID].toString(),
                    data[FireBaseConstants.FIELD_ID].toString(),
                    contentType,
                    data[FireBaseConstants.FIELD_CONTENT].toString()
                    ,
                    data[FireBaseConstants.FIELD_FAVOURITE].toString()
                )
                homeData.add(homeModel)
            }
        }

        if (iFireBaseCallBack != null) {
            iFireBaseCallBack?.fireBaseData(homeData)
        }
    }


    fun initializeFireBaseCallBack(iFireBaseCallBack: IFireBaseCallBack) {
        this.iFireBaseCallBack = iFireBaseCallBack
    }

    fun initializeFireBaseCategoryCallBack(iFireBaseCategoryCallBack: IFireBaseCategoryCallBack) {
        this.iFireBaseCategoryCallBack = iFireBaseCategoryCallBack
    }

    fun updateFields(id: String, favourite: String) {
        val ref = db.collection(FireBaseConstants.COLLECTION_PARENT)
            .document(id)
        ref.update(FireBaseConstants.FIELD_FAVOURITE, favourite).addOnSuccessListener {
            Log.i("fieldUpdated", "field has been updated")
        }.addOnFailureListener {
            Log.i("fieldUpdateError", "field update error")
        }

    }

    fun removeFields(id: String) {
        val ref = db.collection(FireBaseConstants.COLLECTION_PARENT)
            .document(id)
        val updates = HashMap<String, Any>()
        updates[FireBaseConstants.FIELD_FAVOURITE] = FieldValue.delete()

        ref.update(updates).addOnCompleteListener {
            Log.i("fieldRemoved", "field has been removed")
        }.addOnFailureListener {
            Log.i("fieldRemovedError", "field removing error")
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun getCategory() {
        iFireBaseCategoryCallBack?.showProgress()
        db.collection(FireBaseConstants.COLLECTION_CATEGORY)
            .get()
            .addOnSuccessListener { result ->
                iFireBaseCategoryCallBack?.hideProgress()
                for (document in result) {
                    prepareCategoryData(listOf(document.data))
                }
            }
            .addOnFailureListener { exception ->
                iFireBaseCategoryCallBack?.showMessage(AppConstants.NO_DATA)
                Log.w("collection error", "Error getting documents.", exception)
            }
    }

    private fun prepareCategoryData(list: List<Map<String, Any>>) {
        val categoryData = mutableListOf<CategoryModel>()
        for (data in list) {
            val categoryModel = CategoryModel(
                data[FireBaseConstants.FIELD_CATEGORY_ID].toString(),
                data[FireBaseConstants.FIELD_CATEGORY].toString()
            )
            categoryData.add(categoryModel)
        }

        if (iFireBaseCategoryCallBack != null) {
            iFireBaseCategoryCallBack?.fireBaseCategoryData(categoryData)
        }
    }

    fun getFilteredQuotes(filterSet: MutableSet<String>) {
        // queried in for loop because no alternate available to query from set
        for (set in filterSet) {
            iFireBaseCallBack?.showProgress()
            db.collection(FireBaseConstants.COLLECTION_PARENT)
                .whereEqualTo("categoryId", set).get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        iFireBaseCallBack?.hideProgress()
                        prepareData(listOf(document.data))
                    }
                }
                .addOnFailureListener { exception ->
                    iFireBaseCallBack?.showMessage(AppConstants.NO_DATA)
                    Log.w("collection error", "Error getting documents.", exception)
                }
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun getFavouriteQuotes() {
        iFireBaseCallBack?.showProgress()
        db.collection(FireBaseConstants.COLLECTION_PARENT)
            .whereEqualTo("favourite","true").get()
            .addOnSuccessListener { result ->
                iFireBaseCallBack?.hideProgress()
                for (document in result) {
                    prepareData(listOf(document.data))
                }
            }
            .addOnFailureListener { exception ->
                iFireBaseCallBack?.showMessage(AppConstants.NO_DATA)
                Log.w("collection error", "Error getting documents.", exception)
            }
    }
}

