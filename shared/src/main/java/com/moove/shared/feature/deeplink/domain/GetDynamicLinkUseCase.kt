package com.moove.shared.feature.deeplink.domain

class GetDynamicLinkUseCase(
    private val dynamicLinkRepository: DynamicLinkRepository,
) {

    suspend operator fun invoke(uri: String): String? {
        return dynamicLinkRepository.parseLink(uri)
    }
}
