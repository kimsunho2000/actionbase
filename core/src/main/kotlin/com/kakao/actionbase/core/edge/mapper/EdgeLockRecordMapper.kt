package com.kakao.actionbase.core.edge.mapper

import com.kakao.actionbase.core.Constants
import com.kakao.actionbase.core.codec.ByteArrayBufferPool
import com.kakao.actionbase.core.codec.MapperExtensions.decodeEdgeKeyPrefix
import com.kakao.actionbase.core.codec.MapperExtensions.encodeKeyPrefix
import com.kakao.actionbase.core.codec.XXHash32Wrapper
import com.kakao.actionbase.core.codec.buffer
import com.kakao.actionbase.core.codec.getValue
import com.kakao.actionbase.core.codec.getValueOrNull
import com.kakao.actionbase.core.codec.hasRemaining
import com.kakao.actionbase.core.codec.putValue
import com.kakao.actionbase.core.edge.record.EdgeLockRecord
import com.kakao.actionbase.core.edge.record.EdgeStateRecord
import com.kakao.actionbase.core.state.StateValue
import com.kakao.actionbase.core.storage.HBaseRecord

class EdgeLockRecordMapper private constructor(
    val encoder: Encoder,
    val decoder: Decoder,
) {
    companion object {
        @JvmStatic
        fun create(
            bufferPool: ByteArrayBufferPool = ByteArrayBufferPool.Companion.default,
            xxHash32Wrapper: XXHash32Wrapper = XXHash32Wrapper.default,
        ): EdgeLockRecordMapper =
            EdgeLockRecordMapper(
                encoder = Encoder(bufferPool, xxHash32Wrapper),
                decoder = Decoder(xxHash32Wrapper),
            )
    }

    class Encoder(
        private val bufferPool: ByteArrayBufferPool,
        private val xxHash32Wrapper: XXHash32Wrapper,
    ) {
        fun encode(record: EdgeLockRecord): HBaseRecord {
            val key = encodeKey(record.key)
            val value = encodeValue(record.value)
            return HBaseRecord(key = key, value = value)
        }

        fun encodeKey(key: EdgeLockRecord.Key): ByteArray =
            bufferPool.use { buffer ->
                buffer
                    .encodeKeyPrefix(xxHash32Wrapper, key.source, key.tableCode, key.recordTypeCode)
                    .putValue(key.target)
                    .toByteArray()
            }

        fun encodeValue(value: Long): ByteArray =
            bufferPool.use { buffer ->
                buffer
                    .putValue(value)
                    .toByteArray()
            }
    }

    class Decoder(
        private val xxHash32Wrapper: XXHash32Wrapper,
    ) {
        fun decode(
            key: ByteArray,
            value: ByteArray,
        ): EdgeStateRecord {
            val edgeKey = decodeKey(key)
            val edgeValue = decodeValue(value)

            return EdgeStateRecord(
                key = edgeKey,
                value = edgeValue,
            )
        }

        fun decodeKey(key: ByteArray): EdgeStateRecord.Key {
            val buffer = key.buffer()

            val prefix = buffer.decodeEdgeKeyPrefix()
            val target: Any = buffer.getValue()

            return EdgeStateRecord.Key.of(source = prefix.source, tableCode = prefix.tableCode, target = target)
        }

        fun decodeValue(value: ByteArray): EdgeStateRecord.Value {
            val buffer = value.buffer()

            val active: Boolean = buffer.getValue<Byte>() == Constants.Codec.BYTE_TRUE
            val version: Long = buffer.getValue()

            val properties = mutableMapOf<Int, StateValue>()

            var insertTs: Long? = null
            var deleteTs: Long? = null

            while (buffer.hasRemaining()) {
                val propertyHashKey: Int = buffer.getValue()
                val propertyValue: Any? = buffer.getValueOrNull()
                val propertyVersion: Long = buffer.getValue()

                when (propertyHashKey) {
                    xxHash32Wrapper.insertTsHash -> insertTs = propertyValue as? Long
                    xxHash32Wrapper.deleteTsHash -> deleteTs = propertyValue as? Long
                    else -> {
                        properties[propertyHashKey] = StateValue(propertyVersion, propertyValue)
                    }
                }
            }

            return EdgeStateRecord.Value(
                active = active,
                version = version,
                createdAt = insertTs,
                deletedAt = deleteTs,
                properties = properties,
            )
        }
    }
}
