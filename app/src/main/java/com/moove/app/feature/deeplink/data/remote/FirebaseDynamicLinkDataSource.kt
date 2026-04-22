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
    private val firebaseDynamicLinks: FirebaseDynamicLinks? = null,
    private val backgroundDispatcher: CoroutineDispatcher = Dispatchers.IO,
) {

    suspend fun parseLink(uri: String): String? = withContext(backgroundDispatcher) {
        val sdk = firebaseDynamicLinks ?: return@withContext null
        if (uri.matchesPattern(host).not()) return@withContext null
        try {
            sdk.getDynamicLink(Uri.parse(uri)).await().link?.toString()
        } catch (e: Exception) {
            throw DynamicLinkParseException(cause = e)
        }
    }
}
