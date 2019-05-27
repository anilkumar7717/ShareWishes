package com.example.sharewishes

import android.app.Application
import com.example.sharewishes.prefs.Prefs

/*-- prefs will initialize first when it will use first time --*/
val prefs: Prefs by lazy {
    App.prefs!!
}

/**
 * Application class
 */
class App : Application() {

    companion object {
        var prefs: Prefs? = null
    }

    override fun onCreate() {
        prefs = Prefs(applicationContext)
        super.onCreate()
    }
}