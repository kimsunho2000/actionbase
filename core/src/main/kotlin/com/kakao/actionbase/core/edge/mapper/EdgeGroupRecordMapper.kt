package com.kakao.actionbase.core.edge.mapper

import com.kakao.actionbase.core.codec.ByteArrayBuffer
import com.kakao.actionbase.core.codec.ByteArrayBufferPool
import com.kakao.actionbase.core.codec.MapperExtensions.decodeEdgeKeyPrefix
import com.kakao.actionbase.core.codec.MapperExtensions.encodeKeyPrefix
import com.kakao.actionbase.core.codec.XXHash32Wrapper
import com.kakao.actionbase.core.codec.buffer
import com.kakao.actionbase.core.codec.getValue
import com.kakao.actionbase.core.codec.getValueOrNull
import com.kakao.actionbase.core.codec.hasRemaining
import com.kakao.actionbase.core.codec.putValue
import com.kakao.actionbase.core.edge.record.EdgeGroupRecord
import com.kakao.actionbase.core.java.codec.common.hbase.Order
import com.kakao.actionbase.core.metadata.common.Direction

class EdgeGroupRecordMapper private constructor(
    val encoder: Encoder,
    val decoder: Decoder,
) {
    companion object {
        @JvmStatic
        fun create(
            bufferPool: ByteArrayBufferPool = ByteArrayBufferPool.Companion.default,
            xxHash32Wrapper: XXHash32Wrapper = XXHash32Wrapper.default,
        ): EdgeGroupRecordMapper =
            EdgeGroupRecordMapper(
                encoder = Encoder(bufferPool, xxHash32Wrapper),
                decoder = Decoder(),
            )

        val GROUP_RECORD_ORDER = Order.DESC
    }

    class Encoder(
        private val bufferPool: ByteArrayBufferPool,
        private val xxHash32Wrapper: XXHash32Wrapper,
    ) {
        fun encodeKey(key: EdgeGroupRecord.Key): ByteArray =
            bufferPool.use { buffer ->
                buffer
                    .encodeKey(key)
                    .toByteArray()
            }

        private fun ByteArrayBuffer.encodeKey(keyPrefix: EdgeGroupRecord.Key): ByteArrayBuffer {
            this
                .encodeKeyPrefix(xxHash32Wrapper, keyPrefix.directedSource, keyPrefix.tableCode, keyPrefix.recordTypeCode)
                .putValue(keyPrefix.direction.code)
                .putValue(keyPrefix.groupCode)
            return this
        }

        fun encodeQualifier(qualifier: EdgeGroupRecord.Qualifier): ByteArray =
            bufferPool.use { buffer ->
                buffer
                    .encodeQualifier(qualifier)
                    .toByteArray()
            }

        private fun ByteArrayBuffer.encodeQualifier(qualifier: EdgeGroupRecord.Qualifier): ByteArrayBuffer {
            this
                .also {
                    for (groupValue in qualifier.groupValues) {
                        it.putValue(value = groupValue, order = GROUP_RECORD_ORDER)
                    }
                }
            return this
        }
    }

    class Decoder {
        fun decode(
            key: ByteArray,
            qualifier: ByteArray,
            value: ByteArray,
        ): EdgeGroupRecord {
            val edgeKey = decodeKey(key)
            val edgeQualifier = decodeQualifier(qualifier)
            val edgeValue = decodeValue(value)

            return EdgeGroupRecord(
                key = edgeKey,
                qualifier = edgeQualifier,
                value = edgeValue,
            )
        }

        fun decodeKey(key: ByteArray): EdgeGroupRecord.Key {
            val buffer = key.buffer()

            val prefix = buffer.decodeEdgeKeyPrefix()

            val directionCode: Byte = buffer.getValue()
            val groupCode: Int = buffer.getValue()

            return EdgeGroupRecord.Key.of(
                directedSource = prefix.source,
                tableCode = prefix.tableCode,
                direction = Direction.of(directionCode),
                groupCode = groupCode,
            )
        }

        fun decodeQualifier(qualifier: ByteArray): EdgeGroupRecord.Qualifier {
            val buffer = qualifier.buffer()
            val groupValues = mutableListOf<Any?>()

            while (buffer.hasRemaining()) {
                val groupValue: Any? = buffer.getValueOrNull()
                groupValues.add(groupValue)
            }

            return EdgeGroupRecord.Qualifier(groupValues)
        }

        fun decodeValue(value: ByteArray): Long {
            val buffer = value.buffer()
            return buffer.long
        }
    }
}
