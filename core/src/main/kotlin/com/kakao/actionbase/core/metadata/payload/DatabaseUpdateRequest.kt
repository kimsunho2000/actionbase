package com.kakao.actionbase.core.metadata.payload

data class DatabaseUpdateRequest(
    val comment: String,
    val version: Long = System.currentTimeMillis(),
)
