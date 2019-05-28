package com.example.sharewishes.utils

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.widget.Toast
import com.example.sharewishes.R

object Utils {
    fun openFragment(
        fragmentManager: FragmentManager,
        className: Class<out Fragment>,
        frameLayoutId: Int
    ) {
        if (!isFragmentAlreadyAdded(fragmentManager, className)) {
            val visibleFragment = visibleFragment(fragmentManager)
            val fragmentTransaction = fragmentManager.beginTransaction()
            if (visibleFragment != null)
                fragmentTransaction.hide(visibleFragment)
            val replaceFragment = className.newInstance()
            fragmentTransaction.add(
                frameLayoutId,
                replaceFragment,
                className::class.java.simpleName
            )
            fragmentTransaction.commit()
        } else {
            showFragment(fragmentManager, className)
        }
    }

    private fun showFragment(fragmentManager: FragmentManager, className: Class<out Fragment>) {
        val fragmentList = fragmentManager.fragments
        val fragmentTransaction = fragmentManager.beginTransaction()
        for (fragment in fragmentList) {
            fragment.let {
                if (fragment.javaClass.simpleName == className.simpleName)
                    fragmentTransaction.show(fragment)
                else
                    fragmentTransaction.hide(fragment)
            }
        }
        fragmentTransaction.commit()
    }

    private fun isFragmentAlreadyAdded(
        fragmentManager: FragmentManager,
        className: Class<out Fragment>
    ): Boolean {
        val fragmentList = fragmentManager.fragments
        return fragmentList.any { it != null && it.javaClass.simpleName == className.simpleName }
    }

    private fun visibleFragment(fragmentManager: FragmentManager): Fragment? {
        val fragmentList = fragmentManager.fragments
        return fragmentList.firstOrNull { it != null && it.isVisible }
    }

    fun parseIntValue(stringValue: String): Int {
        return try {
            Integer.parseInt(stringValue)
        } catch (exp: NumberFormatException) {
            0
        }
    }

    fun showToastMessage(activity: Context?, message: String) {
        if (message == AppConstants.NO_DATA) {
            if (activity != null) Toast.makeText(
                activity,
                activity.getString(R.string.no_data),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            if (activity != null) Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        }
    }
}