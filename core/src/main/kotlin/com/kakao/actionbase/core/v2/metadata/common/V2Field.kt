package com.kakao.actionbase.core.v2.metadata.common

import com.kakao.actionbase.core.v2.types.V2PrimitiveType

data class V2Field(
    val type: V2PrimitiveType,
    val desc: String,
)
