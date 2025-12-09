package com.kakao.actionbase.core.edge.payload

data class DataFrameEdgeAggPayload(
    val groups: List<EdgeAggPayload>,
    val count: Int,
    val context: Map<String, Any?>,
)
