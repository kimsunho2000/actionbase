package com.kakao.actionbase.core.metadata

data class TableId(
    val tenant: String,
    val database: String,
    val table: String,
) : Id
