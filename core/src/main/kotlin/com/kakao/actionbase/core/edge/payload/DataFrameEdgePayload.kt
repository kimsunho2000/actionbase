package com.kakao.actionbase.core.edge.payload

data class DataFrameEdgePayload(
    val edges: List<EdgePayload>,
    val count: Int,
    val total: Long,
    val offset: String?,
    val hasNext: Boolean,
    val context: Map<String, Any?>,
)
