package com.example.sharewishes.prefs

import android.content.Context
import android.content.SharedPreferences

class Prefs (context: Context) {
    val PREFS_FILENAME = "com.example.sharewishes.prefs"
    val FILTER_DATA = "filter_list"
    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0);
    val set = HashSet<String>()



    fun setfilterData(filterList: MutableList<String>) {
        set.clear()
        set.addAll(filterList)
        prefs.edit().putStringSet(FILTER_DATA, set).apply()
    }

    fun getFilterData() : MutableSet<String>? {
        return prefs.getStringSet(FILTER_DATA,set)
    }

    /*--function to clear preferences--*/
    fun clearSharedPreference() {
        val editor: SharedPreferences.Editor = prefs.edit()
        editor.clear()
        editor.apply()
    }
}