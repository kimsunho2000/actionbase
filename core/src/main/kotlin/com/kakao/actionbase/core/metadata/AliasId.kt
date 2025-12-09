package com.kakao.actionbase.core.metadata

data class AliasId(
    val tenant: String,
    val database: String,
    val alias: String,
) : Id
