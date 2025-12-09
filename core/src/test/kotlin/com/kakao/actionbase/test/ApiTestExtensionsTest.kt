package com.kakao.actionbase.test

import kotlin.test.assertEquals

import org.junit.jupiter.api.Test

class ApiTestExtensionsTest {
    @Test
    fun testToQueries() {
        val expected: Map<String, Any> =
            mapOf(
                "requestParam2" to "value1",
                "requestParam4" to "1,2",
                "requestParam5" to "false",
            )

        val actual: Map<String, String> =
            mapOf(
                "requestParam1" to null,
                "requestParam2" to "value1",
                "requestParam3" to emptyList<Any>(),
                "requestParam4" to listOf(1, 2),
                "requestParam5" to false,
            ).toQueries()

        assertEquals(expected, actual)
    }
}
