package com.kakao.actionbase.v2.engine.edge

import com.kakao.actionbase.v2.engine.label.EdgeOperationStatus

data class MutationResult(
    val result: List<MutationResultItem>,
)

data class MutationResultItem(
    val status: EdgeOperationStatus,
    val traceId: String,
    val edge: HashEdge?,
)
