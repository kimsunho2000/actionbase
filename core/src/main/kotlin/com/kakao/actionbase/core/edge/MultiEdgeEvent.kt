package com.kakao.actionbase.core.edge

import com.kakao.actionbase.core.state.Event

data class MultiEdgeEvent(
    val id: Any,
    val event: Event,
)
