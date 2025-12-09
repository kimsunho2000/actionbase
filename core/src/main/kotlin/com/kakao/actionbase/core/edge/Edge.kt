package com.kakao.actionbase.core.edge

data class Edge(
    val version: Long,
    val source: Any,
    val target: Any,
    val properties: Map<String, Any?> = emptyMap(),
)
