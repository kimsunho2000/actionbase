package com.kakao.actionbase.v2.engine.storage.local

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class LocalOptions(
    val useGlobal: Boolean = false,
)
