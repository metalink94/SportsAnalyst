package com.sports.analyst.sportsanalyst.deeplink

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.airbnb.deeplinkdispatch.DeepLink
import com.airbnb.deeplinkdispatch.DeepLinkEntry
import com.airbnb.deeplinkdispatch.DeepLinkUri

class DeepLinkHelper(private val intent: Intent) {

    fun parseUrl(stringUrl: String?): Intent {
        if (stringUrl.isNullOrEmpty()) return Intent()
        val deepLinkUri = DeepLinkUri.parse(stringUrl)
        val parameterMap = DeepLinkEntry(stringUrl, DeepLinkEntry.Type.METHOD, null, null)
            .getParameters(stringUrl)
        for (queryParameter in deepLinkUri.queryParameterNames()) {
            for (queryParameterValue in deepLinkUri.queryParameterValues(queryParameter)) {
                if (parameterMap.containsKey(queryParameter)) {
                    Log.w("DEEPLINK","Duplicate parameter name in path and query param: $queryParameter")
                }
                parameterMap[queryParameter] = queryParameterValue
            }
        }
        parameterMap[DeepLink.URI] = stringUrl
        val parameters = Bundle()
        for (parameterEntry in parameterMap.entries) {
            parameters.putString(parameterEntry.key, parameterEntry.value)
        }
        val intent = Intent()
        intent.putExtras(parameters)
        intent.putExtra(DeepLink.IS_DEEP_LINK, true)
        intent.putExtra(DeepLink.REFERRER_URI, stringUrl)
        this.intent.putExtras(intent)
        return intent
    }

    companion object {

        fun getIntent(intent: Intent) =
                DeepLinkHelper(intent).parseUrl(intent.data.toString())
    }
}