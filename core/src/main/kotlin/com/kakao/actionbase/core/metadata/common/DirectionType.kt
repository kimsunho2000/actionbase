package com.kakao.actionbase.core.metadata.common

enum class DirectionType(
    vararg dirs: Direction,
) {
    BOTH(Direction.OUT, Direction.IN),
    OUT(Direction.OUT),
    IN(Direction.IN),
    ;

    private val directions: List<Direction> = dirs.toList()

    fun directions(): List<Direction> = directions

    companion object {
        fun of(name: String): DirectionType = valueOf(name.uppercase())
    }
}
