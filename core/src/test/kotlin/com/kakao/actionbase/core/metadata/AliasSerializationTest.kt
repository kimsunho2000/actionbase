package com.kakao.actionbase.core.metadata

import com.kakao.actionbase.test.json.PrettyObjectWriter

import kotlin.test.Test
import kotlin.test.assertEquals

import com.fasterxml.jackson.module.kotlin.readValue

class AliasSerializationTest {
    val prettyWriter = PrettyObjectWriter(indentSize = 2, lineLengthLimit = 80)

    val objectMapper = prettyWriter.objectMapper

    @Test
    fun `test database serialization`() {
        // given
        val aliasDescriptor =
            AliasDescriptor(
                tenant = "test_tenant",
                database = "test_database",
                alias = "test_alias",
                table = "test_table",
            )

        // when
        val actual = prettyWriter.writeValueAsString(aliasDescriptor)

        // then
        val expected =
            """
            {
              "tenant": "test_tenant",
              "database": "test_database",
              "alias": "test_alias",
              "table": "test_table",
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
        val json = """{"tenant":"test_tenant","database":"test_database","alias":"test_alias","table":"test_table"}"""

        // when
        val actual = objectMapper.readValue<AliasDescriptor>(json)

        // then
        val expected =
            AliasDescriptor(
                tenant = "test_tenant",
                database = "test_database",
                alias = "test_alias",
                table = "test_table",
            )
        assertEquals(expected, actual)
    }
}
