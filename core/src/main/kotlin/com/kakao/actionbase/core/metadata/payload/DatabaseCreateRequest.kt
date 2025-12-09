package com.kakao.actionbase.core.metadata.payload

data class DatabaseCreateRequest(
    val database: String,
    val comment: String,
    val active: Boolean = true,
    val version: Long = System.currentTimeMillis(),
)
