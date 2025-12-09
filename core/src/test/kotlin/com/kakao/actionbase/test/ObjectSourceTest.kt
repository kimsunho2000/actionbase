package com.kakao.actionbase.test

import com.kakao.actionbase.test.documentations.params.ObjectSource
import com.kakao.actionbase.test.documentations.params.ObjectSourceParameterizedTest

import java.util.concurrent.atomic.AtomicInteger

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Nested

import com.fasterxml.jackson.module.kotlin.readValue

class ObjectSourceTest {
    @ObjectSourceParameterizedTest
    @ObjectSource(
        """
        - number:  1
          string: foo
        """,
    )
    fun test1(
        number: Int,
        string: String,
    ) {
        assertEquals(1, number)
        assertEquals("foo", string)
    }

    @Disabled
    @ObjectSourceParameterizedTest
    @ObjectSource(
        """
        - number: 1
        """,
    )
    fun test2(number: Int) {
    }

    companion object {
        private val testCount = AtomicInteger(0)
    }

    @ObjectSourceParameterizedTest
    @ObjectSource(
        """
        - number1: 1
          number2: 10
          string: foo
        - number2: 20
          number1: 2
          string: bar
        - string: baz
          number1: 3
          number2: 30
        """,
    )
    fun test3(
        number1: Int,
        number2: Int,
        string: String,
    ) {
        val parsedArguments: List<Map<String, Any>> =
            ObjectMappers.YAML.readValue(
                """
                - number1: 1
                  number2: 10
                  string: foo
                - number2: 20
                  number1: 2
                  string: bar
                - string: baz
                  number1: 3
                  number2: 30
                """.trimIndent(),
            )
        val invocationNumber = testCount.getAndIncrement()
        val argument = parsedArguments[invocationNumber]

        assertEquals(argument["number1"], number1)
        assertEquals(argument["number2"], number2)
        assertEquals(argument["string"], string)
    }

    @ObjectSourceParameterizedTest
    @ObjectSource(
        """
        - number1: 1
          number2: 10
        """,
    )
    fun test4(
        number1: Int,
        number2: Int,
        string: String?,
    ) {
        assertEquals(1, number1)
        assertEquals(10, number2)
        assertNull(string)
    }

    @ObjectSourceParameterizedTest
    @ObjectSource(
        """
        - number: 1
          enum : FOO
          list:
             - 1
             - 2
          map:
             foo: foo
             bar: bar
             baz: baz
          testClasses: |
              [
                {
                  "number": 1,
                  "string": "foo"
                }
              ]
          testClass: |
            {
              testClass: {
                "number": 1,
                "string": "foo"
              }
            }
        """,
    )
    fun test5(
        number: Int,
        enum: TestEnum,
        list: List<Int>,
        map: Map<String, String>,
        testClasses: List<TestClass>,
        testClass: Map<String, TestClass>,
    ) {
        assertEquals(1, number)
        assertEquals(TestEnum.FOO, enum)
        assertEquals(2, list.size)
        assertEquals(listOf(1, 2), list)
        assertEquals(3, map.size)
        assertEquals(mapOf("foo" to "foo", "bar" to "bar", "baz" to "baz"), map)
        assertEquals(1, testClasses.size)
        assertEquals(ObjectMappers.JSON.writeValueAsString(listOf(TestClass(1, "foo"))), ObjectMappers.JSON.writeValueAsString(testClasses))
        assertEquals(1, testClass.size)
        ObjectAssertions.assertEquals(testClass, mapOf("testClass" to TestClass(1, "foo")))
    }

    @ObjectSourceParameterizedTest
    @ObjectSource(
        """
        - testClasses:
           - number: 1
             string : foo
        """,
    )
    fun test6(testClasses: List<TestClass>) {
        assertEquals(1, testClasses[0].number)
        assertEquals("foo", testClasses[0].string)
    }

    @ObjectSourceParameterizedTest
    @ObjectSource(
        """
        [
          {
            "number": 1,
            "string": "foo"
          }
        ]
        """,
    )
    fun test7(
        number: Int,
        string: String,
    ) {
        assertEquals(1, number)
        assertEquals("foo", string)
    }

    @ObjectSourceParameterizedTest
    @ObjectSource(
        """
        [
          {
            "number1": 1,
            "number2": 10
          }
        ]
        """,
    )
    fun test8(
        number1: Int,
        number2: Int,
        string: String?,
    ) {
        assertEquals(1, number1)
        assertEquals(10, number2)
        assertNull(string)
    }

    @ObjectSourceParameterizedTest
    @ObjectSource(
        """
        [
          {
            "number": 1,
            "enum": "FOO",
            "list": [
              1,
              2
            ],
            "map": {
              "foo": "foo",
              "bar": "bar",
              "baz": "baz"
            }
          }
        ]
        """,
    )
    fun test9(
        number: Int,
        enum: TestEnum,
        list: List<Int>,
        map: Map<String, String>,
    ) {
        assertEquals(1, number)
        assertEquals(TestEnum.FOO, enum)
        assertEquals(2, list.size)
        assertEquals(listOf(1, 2), list)
        assertEquals(3, map.size)
        assertEquals(mapOf("foo" to "foo", "bar" to "bar", "baz" to "baz"), map)
    }

    @ObjectSourceParameterizedTest
    @ObjectSource(
        """
        [
          {
            "testClasses": [
              {
                "number": 1,
                "string": "foo"
              }
            ]
          }
        ]
        """,
    )
    fun test10(testClasses: List<TestClass>) {
        assertEquals(1, testClasses[0].number)
        assertEquals("foo", testClasses[0].string)
    }

    @Nested
    inner class InnerClassTest {
        @ObjectSourceParameterizedTest
        @ObjectSource(
            """
            - number: 1000
              string: foo
            """,
        )
        fun test1(
            number: Int,
            string: String,
        ) {
            assertEquals(1000, number)
            assertEquals("foo", string)
        }

        @ObjectSourceParameterizedTest
        @ObjectSource(
            """
            [
              {
                "number": 1000,
                "string": "foo"
              }
            ]
            """,
        )
        fun test2(
            number: Int,
            string: String,
        ) {
            assertEquals(1000, number)
            assertEquals("foo", string)
        }
    }

    class TestClass(
        val number: Int,
        val string: String,
    )

    enum class TestEnum {
        FOO,
        BAR,
    }
}
