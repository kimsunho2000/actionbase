package com.kakao.actionbase.v2.engine.client.web.impl

import com.kakao.actionbase.v2.engine.client.web.WebClient

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

import reactor.core.publisher.Mono

class DefaultWebClient(
    baseUrl: String,
    private val defaultHeaders: Map<String, String>,
) : WebClient {
    private val normalizedBaseUrl = if (baseUrl.endsWith("/")) baseUrl.dropLast(1) else baseUrl

    override fun post(
        path: String,
        body: String,
    ): Mono<String> =
        Mono.fromCallable {
            val fullUrl = buildUrl(path)
            val request =
                HttpRequest
                    .newBuilder()
                    .apply {
                        uri(URI.create(fullUrl))
                        for ((key, value) in defaultHeaders) {
                            header(key, value)
                        }
                        method("POST", HttpRequest.BodyPublishers.ofString(body))
                    }.build()
            val response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString())
            response.body()
        }

    private fun buildUrl(path: String): String {
        val normalizedPath = if (path.startsWith("/")) path.drop(1) else path
        return "$normalizedBaseUrl/$normalizedPath"
    }
}
