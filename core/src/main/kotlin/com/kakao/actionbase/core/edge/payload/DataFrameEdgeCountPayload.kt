package com.kakao.actionbase.core.edge.payload

data class DataFrameEdgeCountPayload(
    val counts: List<EdgeCountPayload>,
    val count: Int,
    val context: Map<String, Any?>,
)
