package com.kakao.actionbase.core.codec

import net.jpountz.xxhash.XXHash32
import net.jpountz.xxhash.XXHashFactory

class XXHash32Wrapper private constructor(
    private val seed: Int,
) {
    companion object {
        private val xxHash32: XXHash32 = XXHashFactory.fastestInstance().hash32()

        private const val INSERT_TS_KEY_NAME = "__InsertTs__"
        private const val DELETE_TS_KEY_NAME = "__DeleteTs__"

        @JvmStatic
        fun create(seed: Int = 0) = XXHash32Wrapper(seed)

        @JvmStatic
        val default = create()
    }

    val insertTsHash = stringHash(string = INSERT_TS_KEY_NAME)
    val deleteTsHash = stringHash(string = DELETE_TS_KEY_NAME)

    fun hash(
        bytes: ByteArray,
        offset: Int,
        length: Int,
    ) = xxHash32.hash(bytes, offset, length, seed)

    fun stringHash(string: String): Int {
        val bytes = string.toByteArray()
        return xxHash32.hash(bytes, 0, bytes.size, seed)
    }
}
