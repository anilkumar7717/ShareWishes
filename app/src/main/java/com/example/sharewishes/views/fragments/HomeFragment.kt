package com.example.sharewishes.views.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.sharewishes.MainActivity
import com.example.sharewishes.R
import com.example.sharewishes.adapter.HomeAdapter
import com.example.sharewishes.firebasedata.FireBaseGetData
import com.example.sharewishes.interfaces.IFireBaseCallBack
import com.example.sharewishes.interfaces.IFragmentCommunication
import com.example.sharewishes.interfaces.IRecyclerViewClickCallBack
import com.example.sharewishes.models.HomeModel
import com.example.sharewishes.prefs
import com.example.sharewishes.utils.AppConstants
import com.example.sharewishes.utils.AppConstants.IMAGE_DIALOG_TAG
import com.example.sharewishes.utils.Utils
import com.example.sharewishes.views.dailogs.FullScreenImageDialog
import com.google.android.youtube.player.YouTubeStandalonePlayer
import kotlinx.android.synthetic.main.include_recyclerview.*


class HomeFragment : Fragment(), IFireBaseCallBack, IRecyclerViewClickCallBack,
    IFragmentCommunication {
    lateinit var homeAdapter: HomeAdapter
    lateinit var homeData: MutableList<HomeModel>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_homefragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_contents.layoutManager = LinearLayoutManager(context)
        homeData = mutableListOf()
        homeAdapter = HomeAdapter(homeData, this)
        rv_contents.adapter = homeAdapter
        FireBaseGetData.instance.initializeFireBaseCallBack(this)
        if (prefs.getFilterData()!!.isEmpty()) {
            FireBaseGetData.instance.getQuotesFromFireStore()
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

    override fun fireBaseData(fireBaseData: MutableList<HomeModel>) {
        homeData.addAll(fireBaseData)
        homeAdapter.notifyDataSetChanged()
    }

    override fun clickAction(content: String, contentType: Int) {
        if (contentType == HomeAdapter.IMAGE) {
            val activeFragment = fragmentManager?.findFragmentByTag(IMAGE_DIALOG_TAG)
            val fragmentTransaction = fragmentManager?.beginTransaction()
            // checking activeFragment!=null with let then removing already activeFragment
            activeFragment?.let { fragmentTransaction?.remove(activeFragment) }
            fragmentTransaction?.addToBackStack(null)
            val fullScreenImageDialog = FullScreenImageDialog()
            val bundle = Bundle()
            bundle.putString(AppConstants.EXTRA_CONTENT, content)
            fullScreenImageDialog.arguments = bundle
            fullScreenImageDialog.show(fragmentTransaction, IMAGE_DIALOG_TAG)
        } else {
            val intent = YouTubeStandalonePlayer.createVideoIntent(
                activity, getString(R.string.developer_key), content,
                0, true, true
            )
            startActivity(intent)
        }
    }

    override fun addToFav(id: String, favourite: String) {
        FireBaseGetData.instance.updateFields(id, favourite)
    }

    override fun removeFromFav(id: String) {
        FireBaseGetData.instance.removeFields(id)
    }

    override fun onResume() {
        super.onResume()
        if (prefs.getFilterData()!!.isNotEmpty()) {
            FireBaseGetData.instance.getFilteredQuotes(prefs.getFilterData()!!)
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        (activity as MainActivity).registerDataUpdateListener(this)
    }

    override fun onDataUpdate() {
        homeData.clear()
        FireBaseGetData.instance.getFilteredQuotes(prefs.getFilterData()!!)
    }
}