package com.kakao.actionbase.v2.engine.client.web.impl

import com.kakao.actionbase.v2.engine.client.web.WebClient
import com.kakao.actionbase.v2.engine.client.web.WebClientFactory
import com.kakao.actionbase.v2.engine.util.getLogger

object DefaultWebClientFactory : WebClientFactory {
    val logger = getLogger()

    override fun create(
        baseUrl: String,
        headers: Map<String, String>,
    ): WebClient {
        logger.warn("Do not use DefaultWebClientFactory in production")
        return DefaultWebClient(baseUrl, headers)
    }
}
