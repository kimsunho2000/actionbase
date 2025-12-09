package com.kakao.actionbase.v2.engine.warmup

data class WarmUpConfig(
    val enabled: Boolean = false,
    val count: Int = 0,
    val concurrency: Int = 1,
)
