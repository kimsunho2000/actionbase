package com.kakao.actionbase.v2.engine.label

import com.kakao.actionbase.v2.engine.sql.Row

data class ScanResult(
    val rows: List<Row>,
    val offset: String?,
    val hasNext: Boolean,
)
