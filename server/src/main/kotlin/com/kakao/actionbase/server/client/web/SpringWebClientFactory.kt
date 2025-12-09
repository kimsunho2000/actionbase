package com.kakao.actionbase.server.client.web

import com.kakao.actionbase.v2.engine.client.web.WebClient
import com.kakao.actionbase.v2.engine.client.web.WebClientFactory

object SpringWebClientFactory : WebClientFactory {
    override fun create(
        baseUrl: String,
        headers: Map<String, String>,
    ): WebClient {
        val springWebClient =
            org.springframework.web.reactive.function.client.WebClient
                .builder()
                .baseUrl(baseUrl)
                .defaultHeaders { headers.forEach { (k, v) -> it.add(k, v) } }
                .build()
        return SpringWebClient(springWebClient)
    }
}
