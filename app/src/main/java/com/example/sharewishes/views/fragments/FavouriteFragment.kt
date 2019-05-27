package com.example.sharewishes.views.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sharewishes.R
import com.example.sharewishes.adapter.HomeAdapter
import com.example.sharewishes.firebasedata.FireBaseGetData
import com.example.sharewishes.interfaces.IFireBaseCallBack
import com.example.sharewishes.interfaces.IRecyclerViewClickCallBack
import com.example.sharewishes.models.HomeModel
import com.example.sharewishes.utils.AppConstants
import com.example.sharewishes.utils.Utils
import com.example.sharewishes.views.dailogs.FullScreenImageDialog
import com.google.android.youtube.player.YouTubeStandalonePlayer
import kotlinx.android.synthetic.main.include_recyclerview.*

class FavouriteFragment : Fragment(), IFireBaseCallBack, IRecyclerViewClickCallBack {
    lateinit var favouriteAdapter: HomeAdapter
    lateinit var homeData: MutableList<HomeModel>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favouritefragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_contents.layoutManager = LinearLayoutManager(context)
        homeData = mutableListOf()
        favouriteAdapter = HomeAdapter(homeData, this)
        rv_contents.adapter = favouriteAdapter
        FireBaseGetData.instance.initializeFireBaseCallBack(this)
        FireBaseGetData.instance.getFavouriteQuotes()
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
        for (homeModel in fireBaseData) {
            if (homeModel.favourite == "true") {
                homeData.add(homeModel)
                favouriteAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun clickAction(content: String, contentType: Int) {
        if (contentType == HomeAdapter.IMAGE) {
            val activeFragment = fragmentManager?.findFragmentByTag(AppConstants.IMAGE_DIALOG_TAG)
            val fragmentTransaction = fragmentManager?.beginTransaction()
            // checking activeFragment!=null with let then removing already activeFragment
            activeFragment?.let { fragmentTransaction?.remove(activeFragment) }
            fragmentTransaction?.addToBackStack(null)
            val fullScreenImageDialog = FullScreenImageDialog()
            val bundle = Bundle()
            bundle.putString(AppConstants.EXTRA_CONTENT, content)
            fullScreenImageDialog.arguments = bundle
            fullScreenImageDialog.show(fragmentTransaction, AppConstants.IMAGE_DIALOG_TAG)
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
        favouriteAdapter.notifyDataSetChanged()
    }

    override fun removeFromFav(id: String) {
        FireBaseGetData.instance.removeFields(id)
        favouriteAdapter.notifyDataSetChanged()
    }

}