package com.driveinto.ladyj.app

import android.app.Application
import net.danlew.android.joda.JodaTimeAndroid

class LadyJApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        JodaTimeAndroid.init(this)
    }
}