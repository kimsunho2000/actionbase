package com.kakao.actionbase.core.v2.metadata

import com.kakao.actionbase.core.metadata.AliasDescriptor as V3AliasDescriptor

import com.kakao.actionbase.test.json.PrettyObjectWriter

import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals

import com.fasterxml.jackson.module.kotlin.readValue

class V2AliasSerializationTest {
    val prettyWriter = PrettyObjectWriter(indentSize = 2, lineLengthLimit = 80)

    val objectMapper = prettyWriter.objectMapper

    @Test
    fun `test service serialization`() {
        // given
        val v2AliasDescriptor =
            V2AliasDescriptor(
                name = "gift.gift_like_product_v1",
                desc = "Gift Wish",
                active = true,
                target = "gift.gift_like_product_v1_20240605_102816",
            )
        // when
        val actual = prettyWriter.writeValueAsString(v2AliasDescriptor)

        // then
        val expected =
            """
            {
              "name": "gift.gift_like_product_v1",
              "target": "gift.gift_like_product_v1_20240605_102816",
              "desc": "Gift Wish",
              "active": true
            }
            """.trimIndent()

        assertEquals(expected, actual)
    }

    @Test
    fun `test alias deserialization`() {
        // given
        val json =
            """
            {
              "active": true,
              "name": "gift.gift_like_product_v1",
              "desc": "Gift Wish",
              "target": "gift.gift_like_product_v1_20240605_102816",
              "label": {
                "active": true,
                "name": "gift.gift_like_product_v1_20240605_102816",
                "desc": "some redundant label information"
              }
            }
            """.trimIndent()

        // when
        val actual = objectMapper.readValue<V2AliasDescriptor>(json)

        // then
        val expected =
            V2AliasDescriptor(
                name = "gift.gift_like_product_v1",
                desc = "Gift Wish",
                active = true,
                target = "gift.gift_like_product_v1_20240605_102816",
            )
        assertEquals(expected, actual)
    }

    @Test
    fun `test service to v3 object`() {
        // given
        val v2AliasDescriptor =
            V2AliasDescriptor(
                name = "gift.gift_like_product_v1",
                desc = "Gift Wish",
                active = true,
                target = "gift.gift_like_product_v1_20240605_102816",
            )

        // when
        val actual = v2AliasDescriptor.toV3("test_tenant")

        // then
        val expected =
            V3AliasDescriptor(
                tenant = "test_tenant",
                database = "gift",
                alias = "gift_like_product_v1",
                table = "gift_like_product_v1_20240605_102816",
                comment = "Gift Wish",
                active = true,
                revision = -1,
                createdAt = -1,
                createdBy = "",
                updatedAt = -1,
                updatedBy = "",
            )
        assertEquals(expected, actual)
    }

    @Ignore
    @Test
    fun `test service to database json string`() {
        // given
        val v2AliasDescriptor =
            V2AliasDescriptor(
                name = "gift.gift_like_product_v1",
                desc = "Gift Wish",
                active = true,
                target = "gift.gift_like_product_v1_20240605_102816",
            )

        // when
        val v3 = v2AliasDescriptor.toV3("test_tenant")
        val actual = prettyWriter.writeValueAsString(v3)

        // then
        val expected =
            """
            {
              "tenant": "test_tenant",
              "database": "gift",
              "alias": "gift_like_product_v1",
              "table": "gift_like_product_v1_20240605_102816",
              "active": true,
              "comment": "Gift Wish",
              "revision": -1,
              "createdAt": -1,
              "createdBy": "",
              "updatedAt": -1,
              "updatedBy": ""
            }
            """.trimIndent()
        assertEquals(expected, actual)
    }
}
