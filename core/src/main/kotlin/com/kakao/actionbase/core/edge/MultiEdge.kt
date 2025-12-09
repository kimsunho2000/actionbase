package com.kakao.actionbase.core.edge

data class MultiEdge(
    val version: Long,
    val id: Any,
    val source: Any? = null,
    val target: Any? = null,
    val properties: Map<String, Any?> = emptyMap(),
)
