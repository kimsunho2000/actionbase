package com.kakao.actionbase.core.codec

import com.kakao.actionbase.core.types.PrimitiveType

data class Field(
    val type: PrimitiveType,
    val value: String,
) {
    fun cast() = type.cast(value)
}
