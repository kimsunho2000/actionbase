package com.kakao.actionbase.v2.engine.storage.nil

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class NilOptions(
    val wal: Boolean = false,
)
