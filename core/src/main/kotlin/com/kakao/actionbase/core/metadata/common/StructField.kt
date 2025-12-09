package com.kakao.actionbase.core.metadata.common

import com.kakao.actionbase.core.types.PrimitiveType

data class StructField(
    val name: String,
    val type: PrimitiveType,
    val comment: String,
    val nullable: Boolean,
)
