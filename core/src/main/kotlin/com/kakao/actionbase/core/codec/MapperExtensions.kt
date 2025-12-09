package com.kakao.actionbase.core.codec

import com.kakao.actionbase.core.edge.record.EdgeRecord

object MapperExtensions {
    const val SALT_DUMMY: Int = 0
    const val SALT_LENGTH: Int = 4

    fun ByteArrayBuffer.encodeKeyPrefix(
        xxHash32Wrapper: XXHash32Wrapper,
        source: Any,
        tableCode: Int,
        recordTypeCode: Byte,
    ): ByteArrayBuffer {
        val saltPosition = position

        putInt(SALT_DUMMY)

        val offset = position

        putValue(source)
        putValue(tableCode)
        putValue(recordTypeCode)

        val salt =
            xxHash32Wrapper.hash(
                bytes = bytes,
                offset = offset,
                length = position - offset,
            )

        putInt(saltPosition, salt)
        return this
    }

    fun ByteArrayBuffer.decodeEdgeKeyPrefix(): EdgeRecord.Key.CommonPrefix {
        skip(SALT_LENGTH) // skip salt
        val source: Any = getValue()
        val tableCode = getValue<Int>()
        val typeCode: Byte = getValue()

        return EdgeRecord.Key.CommonPrefix(source, tableCode, typeCode)
    }
}
