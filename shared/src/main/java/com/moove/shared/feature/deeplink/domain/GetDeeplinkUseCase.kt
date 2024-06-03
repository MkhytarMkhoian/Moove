package com.moove.shared.feature.deeplink.domain

class GetDeeplinkUseCase(
    private val deeplinkRepository: DeeplinkRepository,
    private val getDynamicLinkUseCase: GetDynamicLinkUseCase,
) {
    suspend operator fun invoke(uri: String): DeepLink {
        val parsedUri = getDynamicLinkUseCase(uri) ?: uri
        val deepLink = deeplinkRepository.getDeepLink(parsedUri)
        return deepLink
    }
}
