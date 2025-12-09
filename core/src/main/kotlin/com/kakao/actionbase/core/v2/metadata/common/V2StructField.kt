package com.kakao.actionbase.core.v2.metadata.common

import com.kakao.actionbase.core.v2.types.V2PrimitiveType

data class V2StructField(
    val name: String,
    val type: V2PrimitiveType,
    val nullable: Boolean,
    val desc: String,
)
