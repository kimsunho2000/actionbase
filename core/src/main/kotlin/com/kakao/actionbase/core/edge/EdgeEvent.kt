package com.kakao.actionbase.core.edge

import com.kakao.actionbase.core.state.Event

data class EdgeEvent(
    val source: Any,
    val target: Any,
    val event: Event,
)
