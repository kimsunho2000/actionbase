package com.kakao.actionbase.core.v2.metadata.common

data class V2Index(
    val name: String,
    val fields: List<V2IndexField>,
    val desc: String,
)
