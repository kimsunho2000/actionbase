package com.kakao.actionbase.core.metadata

import com.kakao.actionbase.test.json.PrettyObjectWriter

import kotlin.test.Test
import kotlin.test.assertEquals

import com.fasterxml.jackson.module.kotlin.readValue

class DatabaseSerializationTest {
    val prettyWriter = PrettyObjectWriter(indentSize = 2, lineLengthLimit = 80)

    val objectMapper = prettyWriter.objectMapper

    @Test
    fun `test database serialization`() {
        // given
        val databaseDescriptor =
            DatabaseDescriptor(
                tenant = "test_tenant",
                database = "test_database",
            )

        // when
        val actual = prettyWriter.writeValueAsString(databaseDescriptor)

        // then
        val expected =
            """
            {
              "tenant": "test_tenant",
              "database": "test_database",
              "active": true,
              "comment": "",
              "revision": -1,
              "createdAt": -1,
              "createdBy": "",
              "updatedAt": -1,
              "updatedBy": ""
            }
            """.trimIndent()
        assertEquals(expected, actual)
    }

    @Test
    fun `test database deserialization`() {
        // given
        val json = """{"tenant":"test_tenant","database":"test_database"}"""

        // when
        val actual = objectMapper.readValue<DatabaseDescriptor>(json)

        // then
        val expected = DatabaseDescriptor(tenant = "test_tenant", database = "test_database")
        assertEquals(expected, actual)
    }
}
