package com.kakao.actionbase.core.edge.payload

import com.kakao.actionbase.core.metadata.common.Direction

data class EdgeAggPayload(
    val start: Any,
    val direction: Direction,
    val value: Long,
    val context: Map<String, Any?>,
)
