package com.kakao.actionbase.server.client.web

import com.kakao.actionbase.v2.engine.client.web.WebClient

import reactor.core.publisher.Mono

class SpringWebClient(
    private val springWebClient: org.springframework.web.reactive.function.client.WebClient,
) : WebClient {
    override fun post(
        path: String,
        body: String,
    ): Mono<String> =
        springWebClient
            .post()
            .uri(path)
            .bodyValue(body)
            .retrieve()
            .bodyToMono(String::class.java)
}
