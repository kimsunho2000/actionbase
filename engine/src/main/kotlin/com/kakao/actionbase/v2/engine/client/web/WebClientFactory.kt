package com.kakao.actionbase.v2.engine.client.web

interface WebClientFactory {
    fun create(
        baseUrl: String,
        headers: Map<String, String>,
    ): WebClient
}
