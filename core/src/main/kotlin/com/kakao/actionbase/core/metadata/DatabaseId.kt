package com.kakao.actionbase.core.metadata

data class DatabaseId(
    val tenant: String,
    val database: String,
) : Id
