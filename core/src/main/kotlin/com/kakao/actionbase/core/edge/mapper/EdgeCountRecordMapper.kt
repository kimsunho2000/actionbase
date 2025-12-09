package com.kakao.actionbase.core.edge.mapper

import com.kakao.actionbase.core.codec.ByteArrayBuffer
import com.kakao.actionbase.core.codec.ByteArrayBufferPool
import com.kakao.actionbase.core.codec.MapperExtensions.decodeEdgeKeyPrefix
import com.kakao.actionbase.core.codec.MapperExtensions.encodeKeyPrefix
import com.kakao.actionbase.core.codec.XXHash32Wrapper
import com.kakao.actionbase.core.codec.buffer
import com.kakao.actionbase.core.codec.getValue
import com.kakao.actionbase.core.codec.putValue
import com.kakao.actionbase.core.edge.record.EdgeCountRecord
import com.kakao.actionbase.core.metadata.common.Direction

class EdgeCountRecordMapper private constructor(
    val encoder: Encoder,
    val decoder: Decoder,
) {
    companion object {
        @JvmStatic
        fun create(
            bufferPool: ByteArrayBufferPool = ByteArrayBufferPool.Companion.default,
            xxHash32Wrapper: XXHash32Wrapper = XXHash32Wrapper.default,
        ): EdgeCountRecordMapper =
            EdgeCountRecordMapper(
                encoder = Encoder(bufferPool, xxHash32Wrapper),
                decoder = Decoder(),
            )
    }

    class Encoder(
        private val bufferPool: ByteArrayBufferPool,
        private val xxHash32Wrapper: XXHash32Wrapper,
    ) {
        fun encodeKey(key: EdgeCountRecord.Key): ByteArray =
            bufferPool.use { buffer: ByteArrayBuffer ->
                buffer
                    .encodeKeyPrefix(xxHash32Wrapper, key.directedSource, key.tableCode, key.recordTypeCode)
                    .putValue(key.direction.code)
                    .toByteArray()
            }
    }

    class Decoder {
        fun decode(
            key: ByteArray,
            value: ByteArray,
        ): EdgeCountRecord =
            EdgeCountRecord(
                key = decodeKey(key),
                value = decodeValue(value),
            )

        fun decode(
            key: ByteArray,
            value: Long,
        ): EdgeCountRecord =
            EdgeCountRecord(
                key = decodeKey(key),
                value = value,
            )

        fun decodeKey(key: ByteArray): EdgeCountRecord.Key {
            val buffer = key.buffer()

            val prefix = buffer.decodeEdgeKeyPrefix()
            val direction = Direction.of(code = buffer.getValue())

            return EdgeCountRecord.Key.of(
                directedSource = prefix.source,
                tableCode = prefix.tableCode,
                direction = direction,
            )
        }

        fun decodeValue(value: ByteArray): Long {
            val buffer = value.buffer()
            return buffer.long
        }
    }
}
