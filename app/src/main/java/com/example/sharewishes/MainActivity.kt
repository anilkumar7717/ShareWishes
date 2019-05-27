package com.example.sharewishes

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.example.sharewishes.interfaces.IFragmentCommunication
import com.example.sharewishes.utils.Utils
import com.example.sharewishes.views.fragments.DesignFragment
import com.example.sharewishes.views.fragments.FavouriteFragment
import com.example.sharewishes.views.fragments.FilterFragment
import com.example.sharewishes.views.fragments.HomeFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var iFragmentCommunication: IFragmentCommunication
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        toolbar.setTitle(R.string.app_name)
        toolbar.setTitleTextColor(Color.WHITE)
        addFragmentToUi(HomeFragment::class.java)
        navigation.setOnNavigationItemSelectedListener {
            when {
                it.itemId == R.id.navigation_home -> {
                    if (frame_container.tag != null && frame_container.tag != HomeFragment::class.java.simpleName) {
                        addFragmentToUi(HomeFragment::class.java)
                    }
                    return@setOnNavigationItemSelectedListener true
                }
                it.itemId == R.id.navigation_favourite -> {
                    if (frame_container.tag != null && frame_container.tag != FavouriteFragment::class.java.simpleName) {
                        addFragmentToUi(FavouriteFragment::class.java)
                    }
                    return@setOnNavigationItemSelectedListener true
                }
                it.itemId == R.id.navigation_design -> {
                    if (frame_container.tag != null && frame_container.tag != DesignFragment::class.java.simpleName) {
                        addFragmentToUi(DesignFragment::class.java)
                    }
                    return@setOnNavigationItemSelectedListener true
                }
                else -> return@setOnNavigationItemSelectedListener false
            }
        }
    }

    fun addFragmentToUi(className: Class<out Fragment>) {
        Utils.openFragment(supportFragmentManager, className, R.id.frame_container)
        frame_container.tag = className.simpleName
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.filter) {
            if (frame_container.tag != null && frame_container.tag != FilterFragment::class.java.simpleName) {
                addFragmentToUi(FilterFragment::class.java)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    @Synchronized
    fun registerDataUpdateListener(iFragmentCommunication: IFragmentCommunication) {
        this.iFragmentCommunication = iFragmentCommunication
    }

}
