package com.kakao.actionbase.core.edge.payload

import com.kakao.actionbase.core.state.State

data class MultiEdgeMutationStatus(
    val id: Any,
    val count: Int,
    val status: String, // CREATED, IDLE ...
    val before: State,
    val after: State,
    val acc: Long,
)
