package com.kakao.actionbase.core.metadata.common

enum class Direction(
    val code: Byte,
) {
    OUT(2),
    IN(3),
    ;

    companion object {
        private val DIRECTION_BY_CODE = entries.associateBy(Direction::code)

        fun of(code: Byte): Direction = DIRECTION_BY_CODE[code] ?: throw IllegalArgumentException("Invalid direction code: $code")
    }
}
