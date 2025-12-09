package com.kakao.actionbase.server.util

import org.springframework.util.MultiValueMap

private val allowSet =
    setOf(
        "x-request-id",
        "x-real-ip",
    )

fun MultiValueMap<String, String>.getAuditInfo(): Map<String, String> = map { (k, v) -> k.lowercase() to v.first() }.filter { (k, _) -> k.lowercase() in allowSet }.toMap()
