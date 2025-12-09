package com.kakao.actionbase.core.metadata.common

import com.kakao.actionbase.core.types.PrimitiveType

data class Field(
    val type: PrimitiveType,
    val comment: String,
)
