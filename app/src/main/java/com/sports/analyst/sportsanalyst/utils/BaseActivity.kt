package com.sports.analyst.sportsanalyst.utils

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.facebook.appevents.AppEventsLogger

open class BaseActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    open fun getLogger(): AppEventsLogger = (application as SportsApp).logger
}