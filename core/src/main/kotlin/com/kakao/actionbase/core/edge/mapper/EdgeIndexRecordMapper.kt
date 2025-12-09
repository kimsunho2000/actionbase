package com.kakao.actionbase.core.edge.mapper

import com.kakao.actionbase.core.codec.ByteArrayBuffer
import com.kakao.actionbase.core.codec.ByteArrayBufferPool
import com.kakao.actionbase.core.codec.MapperExtensions.decodeEdgeKeyPrefix
import com.kakao.actionbase.core.codec.MapperExtensions.encodeKeyPrefix
import com.kakao.actionbase.core.codec.OrderedHeader
import com.kakao.actionbase.core.codec.XXHash32Wrapper
import com.kakao.actionbase.core.codec.buffer
import com.kakao.actionbase.core.codec.getValue
import com.kakao.actionbase.core.codec.hasRemaining
import com.kakao.actionbase.core.codec.putValue
import com.kakao.actionbase.core.edge.record.EdgeIndexRecord
import com.kakao.actionbase.core.metadata.common.Direction
import com.kakao.actionbase.core.storage.HBaseRecord

class EdgeIndexRecordMapper private constructor(
    val encoder: Encoder,
    val decoder: Decoder,
) {
    companion object {
        @JvmStatic
        fun create(
            bufferPool: ByteArrayBufferPool = ByteArrayBufferPool.Companion.default,
            xxHash32Wrapper: XXHash32Wrapper = XXHash32Wrapper.default,
        ): EdgeIndexRecordMapper =
            EdgeIndexRecordMapper(
                encoder = Encoder(bufferPool, xxHash32Wrapper),
                decoder = Decoder(),
            )
    }

    class Encoder(
        private val bufferPool: ByteArrayBufferPool,
        private val xxHash32Wrapper: XXHash32Wrapper,
    ) {
        fun encode(record: EdgeIndexRecord): HBaseRecord {
            val key = encodeKey(record.key)
            val value = encodeValue(record.value)
            return HBaseRecord(key = key, value = value)
        }

        fun encodeKey(key: EdgeIndexRecord.Key): ByteArray =
            bufferPool.use { buffer ->
                buffer
                    .encodeKeyPrefix(key.prefix)
                    .encodeKeySuffix(key.suffix)
                    .toByteArray()
            }

        // for scan query
        fun encodeKeyPrefix(keyPrefix: EdgeIndexRecord.Key.Prefix): ByteArray =
            bufferPool.use { buffer ->
                buffer
                    .encodeKeyPrefix(keyPrefix)
                    .toByteArray()
            }

        private fun ByteArrayBuffer.encodeKeyPrefix(keyPrefix: EdgeIndexRecord.Key.Prefix): ByteArrayBuffer {
            this
                .encodeKeyPrefix(xxHash32Wrapper, keyPrefix.directedSource, keyPrefix.tableCode, keyPrefix.recordTypeCode)
                .putValue(keyPrefix.direction.code)
                .putValue(keyPrefix.indexCode)
                .also {
                    for (indexValue in keyPrefix.indexValues) {
                        it.putValue(value = indexValue.value, order = indexValue.order)
                    }
                }
            return this
        }

        private fun ByteArrayBuffer.encodeKeySuffix(keySuffix: EdgeIndexRecord.Key.Suffix): ByteArrayBuffer {
            this
                .also {
                    for (indexValue in keySuffix.restIndexValues) {
                        it.putValue(value = indexValue.value, order = indexValue.order)
                    }
                }.putValue(keySuffix.directedTarget)
            return this
        }

        fun encodeValue(edge: EdgeIndexRecord.Value): ByteArray =
            bufferPool.use { buffer: ByteArrayBuffer ->
                buffer
                    .putValue(edge.version)
                    .also {
                        // properties
                        edge.properties.forEach { keyCode, value: Any? ->
                            it.putValue(keyCode)
                            it.putValue(value)
                        }
                    }.toByteArray()
            }
    }

    class Decoder {
        fun decode(
            key: ByteArray,
            value: ByteArray,
        ): EdgeIndexRecord {
            val edgeKey = decodeKey(key)
            val edgeValue = decodeValue(value)

            return EdgeIndexRecord(
                key = edgeKey,
                value = edgeValue,
            )
        }

        fun decodeKey(key: ByteArray): EdgeIndexRecord.Key {
            val buffer = key.buffer()

            val prefix = buffer.decodeEdgeKeyPrefix()

            val directionCode: Byte = buffer.getValue()
            val indexCode: Int = buffer.getValue()

            val remaining = mutableListOf<EdgeIndexRecord.Key.IndexValue>()

            while (buffer.hasRemaining()) {
                val orderedHeader = OrderedHeader.of(code = buffer.peek())
                val indexValue: Any = buffer.getValue()

                remaining.add(EdgeIndexRecord.Key.IndexValue(value = indexValue, order = orderedHeader.order))
            }

            val targetIndex = remaining.size - 1
            val directedTarget: Any = remaining[targetIndex].value ?: throw IllegalStateException("the target of the edge index is null.")

            return EdgeIndexRecord.Key(
                prefix =
                    EdgeIndexRecord.Key.Prefix.of(
                        tableCode = prefix.tableCode,
                        directedSource = prefix.source,
                        direction = Direction.of(directionCode),
                        indexCode = indexCode,
                        indexValues = remaining.dropLast(1),
                    ),
                suffix =
                    EdgeIndexRecord.Key.Suffix(
                        restIndexValues = emptyList(),
                        directedTarget = directedTarget,
                    ),
            )
        }

        fun decodeValue(value: ByteArray): EdgeIndexRecord.Value {
            val buffer = value.buffer()
            val version: Long = buffer.getValue()

            val properties = mutableMapOf<Int, Any>()

            while (buffer.hasRemaining()) {
                val propertyHashKey: Int = buffer.getValue()
                val propertyValue: Any = buffer.getValue()

                properties[propertyHashKey] = propertyValue
            }

            return EdgeIndexRecord.Value(version, properties.toMap())
        }
    }
}
