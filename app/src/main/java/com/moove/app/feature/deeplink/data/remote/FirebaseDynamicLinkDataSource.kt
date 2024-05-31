package com.moove.app.feature.deeplink.data.remote

import android.net.Uri
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.moove.core.kotlin.text.matchesPattern
import com.moove.shared.feature.deeplink.domain.exceptions.DynamicLinkParseException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FirebaseDynamicLinkDataSource(
    private val host: String,
//    private val firebaseDynamicLinks: FirebaseDynamicLinks,
    private val backgroundDispatcher: CoroutineDispatcher = Dispatchers.IO,
) {

    suspend fun parseLink(uri: String): String? = withContext(backgroundDispatcher) {
        if (uri.matchesPattern(host).not()) return@withContext null
        try {
//            firebaseDynamicLinks.getDynamicLink(Uri.parse(uri)).await().link?.toString()
            "https://moove.page.link/45hj45j"
        } catch (e: Exception) {
            throw DynamicLinkParseException(cause = e)
        }
    }
}
