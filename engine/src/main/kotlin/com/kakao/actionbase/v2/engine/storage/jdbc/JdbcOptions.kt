package com.kakao.actionbase.v2.engine.storage.jdbc

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class JdbcOptions(
    val mock: Boolean = false,
    val url: String = "",
)
