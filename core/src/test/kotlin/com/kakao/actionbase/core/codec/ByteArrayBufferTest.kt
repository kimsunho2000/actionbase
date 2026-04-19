package com.kakao.actionbase.core.codec

import com.kakao.actionbase.core.java.codec.common.hbase.Order
import com.kakao.actionbase.test.documentations.params.ObjectSource
import com.kakao.actionbase.test.documentations.params.ObjectSourceParameterizedTest

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ByteArrayBufferTest {
    companion object {
        private const val BUFFER_SIZE = 64
        private const val FIELD_SOURCES = """
- field:
    type: int
    value: "27"
- field:
    type: long
    value: "123456789"
- field:
    type: string
    value: "hello"
- field:
    type: boolean
    value: "true"
- field:
    type: float
    value: "3.14"
- field:
    type: double
    value: "3.14159"
"""
    }

    @ObjectSourceParameterizedTest
    @ObjectSource(FIELD_SOURCES)
    fun `putValue and getValue should round trip in ascending order`(field: Field) {
        val buffer = ByteArray(BUFFER_SIZE).buffer()
        buffer.putValue(field.cast(), Order.ASC)
        buffer.setPosition(0)

        assertEquals(field.cast(), buffer.getValue())
    }

    @ObjectSourceParameterizedTest
    @ObjectSource(FIELD_SOURCES)
    fun `putValue and getValue should round trip in descending order`(field: Field) {
        val buffer = ByteArray(BUFFER_SIZE).buffer()
        buffer.putValue(field.cast(), Order.DESC)
        buffer.setPosition(0)

        assertEquals(field.cast(), buffer.getValue())
    }

    @Test
    fun `getValue should throw IllegalStateException when value is null`() {
        val buffer = ByteArray(BUFFER_SIZE).buffer()
        buffer.putValue(null, Order.ASC)
        buffer.setPosition(0)

        assertThrows<IllegalStateException> {
            buffer.getValue<Any>()
        }
    }

    @Test
    fun `getValueOrNull should return null when value is null`() {
        val buffer = ByteArray(BUFFER_SIZE).buffer()
        buffer.putValue(null, Order.ASC)
        buffer.setPosition(0)

        assertNull(buffer.getValueOrNull<Any>())
    }

    @ObjectSourceParameterizedTest
    @ObjectSource(FIELD_SOURCES)
    fun `hasRemaining should return true when data exists`(field: Field) {
        val buffer = ByteArray(BUFFER_SIZE).buffer()
        buffer.putValue(field.cast(), Order.ASC)
        buffer.setPosition(0)

        assertTrue(buffer.hasRemaining())
    }

    @ObjectSourceParameterizedTest
    @ObjectSource(FIELD_SOURCES)
    fun `hasRemaining should return false after data is consumed`(field: Field) {
        val buffer = ByteArray(BUFFER_SIZE).buffer()
        buffer.putValue(field.cast(), Order.ASC)

        val writtenBytes = buffer.toByteArray()
        val readBuffer = writtenBytes.buffer()

        // consume buffer
        readBuffer.getValue<Any>()

        assertFalse(readBuffer.hasRemaining())
    }

    @ObjectSourceParameterizedTest
    @ObjectSource(FIELD_SOURCES)
    fun `buffer should create ByteArrayBuffer from ByteArray`(field: Field) {
        val tempBuffer = ByteArray(BUFFER_SIZE)

        val buffer = tempBuffer.buffer()
        buffer.putValue(field.cast(), Order.ASC)
        buffer.setPosition(0)

        assertEquals(field.cast(), buffer.getValue())
        assertSame(buffer.bytes, tempBuffer)
    }

    @ObjectSourceParameterizedTest
    @ObjectSource(FIELD_SOURCES)
    fun `putValue should produce different bytes for asc and desc order`(field: Field) {
        val ascBuffer = ByteArray(BUFFER_SIZE).buffer()
        ascBuffer.putValue(field.cast(), Order.ASC)
        val ascBytes = ascBuffer.toByteArray()

        val descBuffer = ByteArray(BUFFER_SIZE).buffer()
        descBuffer.putValue(field.cast(), Order.DESC)
        val descBytes = descBuffer.toByteArray()

        assertFalse(ascBytes.contentEquals(descBytes))
    }
}
