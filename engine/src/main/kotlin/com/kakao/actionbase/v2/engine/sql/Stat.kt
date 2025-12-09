package com.kakao.actionbase.v2.engine.sql

interface Stat<T> {
    val name: StatKey
    val value: T
}

data class StatLong(
    override val name: StatKey,
    override val value: Long,
) : Stat<Long>
