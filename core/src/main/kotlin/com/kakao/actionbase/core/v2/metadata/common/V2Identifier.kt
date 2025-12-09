package com.kakao.actionbase.core.v2.metadata.common

data class V2Identifier(
    val service: String,
    val name: String,
) {
    companion object {
        fun of(value: String): V2Identifier {
            val parts = value.split(".")
            if (parts.size == 2) {
                return V2Identifier(parts[0], parts[1])
            } else {
                throw IllegalArgumentException("Invalid identifier format: $value")
            }
        }
    }
}
