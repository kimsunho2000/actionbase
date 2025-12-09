package com.kakao.actionbase.v2.engine.producer

interface Log {
    fun toJsonString(): Pair<String?, String>

    fun toJsonBytes(): Pair<ByteArray?, ByteArray>
}
