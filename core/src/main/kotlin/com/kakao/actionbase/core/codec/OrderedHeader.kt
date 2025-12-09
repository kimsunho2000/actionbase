package com.kakao.actionbase.core.codec

import com.kakao.actionbase.core.java.codec.common.hbase.Order

enum class OrderedHeader(
    val code: Byte,
    val order: Order = Order.ASC,
) {
    NULL_HEADER(5),
    NULL_HEADER_DESC(-6, Order.DESC),
    STRING_HEADER(52),
    STRING_HEADER_DESC(-53, Order.DESC),
    FALSE_HEADER(53),
    FALSE_HEADER_DESC(-54, Order.DESC),
    TRUE_HEADER(54),
    TRUE_HEADER_DESC(-55, Order.DESC),
    INT8_HEADER(41),
    INT8_HEADER_DESC(-42, Order.DESC),
    INT16_HEADER(42),
    INT16_HEADER_DESC(-43, Order.DESC),
    INT32_HEADER(43),
    INT32_HEADER_DESC(-44, Order.DESC),
    INT64_HEADER(44),
    INT64_HEADER_DESC(-45, Order.DESC),
    FLOAT32_HEADER(48),
    FLOAT32_HEADER_DESC(-49, Order.DESC),
    FLOAT64_HEADER(49),
    FLOAT64_HEADER_DESC(-50, Order.DESC),
    JSON_HEADER(50),
    JSON_HEADER_DESC(-51, Order.DESC),
    ;

    companion object {
        private val ORDERED_BYTES_HEADER_BY_CODE = entries.associateBy(OrderedHeader::code)

        fun of(code: Byte): OrderedHeader = ORDERED_BYTES_HEADER_BY_CODE[code] ?: throw IllegalArgumentException("Invalid ordered bytes header code: $code")
    }
}
