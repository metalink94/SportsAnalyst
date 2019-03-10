package com.sports.analyst.sportsanalyst.deeplink

import android.content.Intent
import android.os.Bundle
import com.sports.analyst.sportsanalyst.screens.splash.SplashScreen
import com.sports.analyst.sportsanalyst.utils.BaseActivity

class DeepLinkActivity: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent.data != null) {
            val deepLinkIntent = DeepLinkHelper.getIntent(intent)
            openSplashScreen(deepLinkIntent)
        }
    }

    private fun openSplashScreen(deepLinkIntent: Intent) {
        startActivity(
            Intent(this, SplashScreen::class.java)
                .putExtras(deepLinkIntent.extras))
        finish()
    }
}