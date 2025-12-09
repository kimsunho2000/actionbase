package com.kakao.actionbase.core.codec

import com.kakao.actionbase.core.java.codec.common.hbase.Order
import com.kakao.actionbase.core.java.codec.common.hbase.OrderedBytes
import com.kakao.actionbase.core.java.codec.common.hbase.PositionedByteRange
import com.kakao.actionbase.core.java.codec.common.hbase.ValueType

import java.io.Serializable
import java.math.BigDecimal
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.UUID

import com.fasterxml.jackson.databind.JsonNode

object ValueUtils {
    fun serialize(
        buffer: PositionedByteRange,
        obj: Any?,
    ) {
        serialize(buffer, obj, order = Order.ASC)
    }

    fun serialize(
        buffer: PositionedByteRange,
        obj: Any?,
        order: Order,
    ) {
        val type = getValueType(obj)
        when (type) {
            ValueType.NULL -> OrderedBytes.encodeNull(buffer, order)
            ValueType.STRING -> OrderedBytes.encodeString(buffer, obj as String, order)
            ValueType.BOOLEAN -> OrderedBytes.encodeBoolean(buffer, obj as Boolean, order)
            ValueType.BYTE -> OrderedBytes.encodeInt8(buffer, obj as Byte, order)
            ValueType.SHORT -> OrderedBytes.encodeInt16(buffer, obj as Short, order)
            ValueType.INT -> OrderedBytes.encodeInt32(buffer, obj as Int, order)
            ValueType.LONG -> OrderedBytes.encodeInt64(buffer, obj as Long, order)
            ValueType.FLOAT -> OrderedBytes.encodeFloat32(buffer, obj as Float, order)
            ValueType.DOUBLE -> OrderedBytes.encodeFloat64(buffer, obj as Double, order)
            ValueType.JSON -> OrderedBytes.encodeJsonNode(buffer, obj as JsonNode, order)
            else -> throw IllegalArgumentException("Unexpected data of type : ${obj?.javaClass?.name}")
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> deserialize(buffer: PositionedByteRange): T? {
        val header: Byte = buffer.peek()

        return when (OrderedHeader.Companion.of(header)) {
            OrderedHeader.NULL_HEADER, OrderedHeader.NULL_HEADER_DESC -> {
                buffer.get()
                null
            }

            OrderedHeader.STRING_HEADER, OrderedHeader.STRING_HEADER_DESC -> OrderedBytes.decodeString(buffer) as T
            OrderedHeader.TRUE_HEADER, OrderedHeader.TRUE_HEADER_DESC -> {
                buffer.get()
                true as T
            }

            OrderedHeader.FALSE_HEADER, OrderedHeader.FALSE_HEADER_DESC -> {
                buffer.get()
                false as T
            }

            OrderedHeader.INT8_HEADER, OrderedHeader.INT8_HEADER_DESC -> OrderedBytes.decodeInt8(buffer) as T
            OrderedHeader.INT16_HEADER, OrderedHeader.INT16_HEADER_DESC -> OrderedBytes.decodeInt16(buffer) as T
            OrderedHeader.INT32_HEADER, OrderedHeader.INT32_HEADER_DESC -> OrderedBytes.decodeInt32(buffer) as T
            OrderedHeader.INT64_HEADER, OrderedHeader.INT64_HEADER_DESC -> OrderedBytes.decodeInt64(buffer) as T
            OrderedHeader.FLOAT32_HEADER, OrderedHeader.FLOAT32_HEADER_DESC -> OrderedBytes.decodeFloat32(buffer) as T
            OrderedHeader.FLOAT64_HEADER, OrderedHeader.FLOAT64_HEADER_DESC -> OrderedBytes.decodeFloat64(buffer) as T
            OrderedHeader.JSON_HEADER, OrderedHeader.JSON_HEADER_DESC -> OrderedBytes.decodeJsonNode(buffer) as T
        }
    }

    private fun getValueType(obj: Any?): ValueType {
        if (obj == null) {
            return ValueType.NULL
        }

        return when (obj) {
            is Boolean -> ValueType.BOOLEAN
            is String -> ValueType.STRING
            is Byte -> ValueType.BYTE
            is Short -> ValueType.SHORT
            is Int -> ValueType.INT
            is Long -> ValueType.LONG
            is Float -> ValueType.FLOAT
            is Double -> ValueType.DOUBLE
            is BigDecimal -> ValueType.DECIMAL
            is JsonNode -> ValueType.JSON
            is LocalDate -> ValueType.DATE
            is LocalTime -> ValueType.TIME
            is LocalDateTime -> ValueType.TIMESTAMP
            is Duration -> ValueType.INTERVAL
            is ByteArray -> ValueType.BINARY
            is Enum<*> -> ValueType.ENUM
            is UUID -> ValueType.UUID
            is Serializable -> ValueType.SERIALIZABLE
            else -> throw IllegalArgumentException("Unsupported type: ${obj.javaClass}")
        }
    }
}
