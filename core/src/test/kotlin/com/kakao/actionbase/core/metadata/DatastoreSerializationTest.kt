package com.kakao.actionbase.core.metadata

import com.kakao.actionbase.core.metadata.common.DatastoreType
import com.kakao.actionbase.test.json.PrettyObjectWriter

import kotlin.test.Test
import kotlin.test.assertEquals

import com.fasterxml.jackson.module.kotlin.readValue

class DatastoreSerializationTest {
    val prettyWriter = PrettyObjectWriter(indentSize = 2, lineLengthLimit = 80)

    val objectMapper = prettyWriter.objectMapper

    @Test
    fun `test database serialization`() {
        // given
        val datastoreDescriptor =
            DatastoreDescriptor(
                type = DatastoreType.HBASE,
                configuration = mapOf("hbase.zookeeper.quorum" to "localhost"),
            )

        // when
        val actual = prettyWriter.writeValueAsString(datastoreDescriptor)

        // then
        val expected =
            """{"type": "HBASE", "configuration": {"hbase.zookeeper.quorum": "localhost"}}""".trimIndent()
        assertEquals(expected, actual)
    }

    @Test
    fun `test database deserialization`() {
        // given
        val json =
            """
            {"type": "HBASE", "configuration": {"hbase.zookeeper.quorum": "localhost"}}
            """.trimIndent()

        // when
        val actual = objectMapper.readValue<DatastoreDescriptor>(json)

        // then
        val expected =
            DatastoreDescriptor(
                type = DatastoreType.HBASE,
                configuration = mapOf("hbase.zookeeper.quorum" to "localhost"),
            )
        assertEquals(expected, actual)
    }
}
