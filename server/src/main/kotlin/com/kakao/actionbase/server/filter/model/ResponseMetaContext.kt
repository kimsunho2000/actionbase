package com.kakao.actionbase.server.filter.model

import org.springframework.http.ResponseEntity

/**
 * This will be rendered in the response header like below:
 *     Response-Meta: version="1.0",host="abcd",testKey="testValue"
 */
enum class ResponseMetaContext(
    val contextKey: String,
    val metaKey: String,
    val fromHeader: Boolean,
) {
    VERSION("RESPONSE_META_VERSION", "version", fromHeader = false),
    HOSTNAME("RESPONSE_META_HOSTNAME", "host", fromHeader = false),
    RESPONSE_META_TEST("RESPONSE_META_TEST", "testKey", fromHeader = true),
    ;

    companion object {
        val headerEntries = entries.filter { it.fromHeader }

        fun ResponseEntity.BodyBuilder.withResponseMetaTest(value: String): ResponseEntity.BodyBuilder = header(RESPONSE_META_TEST.contextKey, value)
    }
}
