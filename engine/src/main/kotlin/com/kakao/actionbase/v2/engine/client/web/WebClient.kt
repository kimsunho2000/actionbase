package com.kakao.actionbase.v2.engine.client.web

import reactor.core.publisher.Mono

interface WebClient {
    fun post(
        path: String,
        body: String,
    ): Mono<String>
}
