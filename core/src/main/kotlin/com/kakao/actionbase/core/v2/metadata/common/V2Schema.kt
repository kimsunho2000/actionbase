package com.kakao.actionbase.core.v2.metadata.common

data class V2Schema(
    val src: V2Field,
    val tgt: V2Field,
    val fields: List<V2StructField>,
)
