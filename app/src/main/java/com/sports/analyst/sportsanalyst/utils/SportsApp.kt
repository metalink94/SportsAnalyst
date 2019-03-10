package com.sports.analyst.sportsanalyst.utils

import android.app.Application
import com.facebook.FacebookSdk
import com.facebook.LoggingBehavior
import com.facebook.appevents.AppEventsLogger
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging

class SportsApp: Application() {

    lateinit var logger: AppEventsLogger

    override fun onCreate() {
        FirebaseApp.initializeApp(this)
        AppEventsLogger.activateApp(this)
        super.onCreate()
        FirebaseMessaging.getInstance().isAutoInitEnabled = true
        //    if (BuildConfig.DEBUG) {
        FacebookSdk.setIsDebugEnabled(true)
        FacebookSdk.addLoggingBehavior(LoggingBehavior.APP_EVENTS)
//    }
        logger = AppEventsLogger.newLogger(this)
    }
}