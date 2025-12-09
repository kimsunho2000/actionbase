package com.kakao.actionbase.core.edge.payload

data class EdgePayload(
    val version: Long,
    val source: Any,
    val target: Any,
    val properties: Map<String, Any?>,
    val context: Map<String, Any?>,
)
