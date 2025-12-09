package com.kakao.actionbase.engine.experiments.hbase

import com.kakao.actionbase.core.Constants
import com.kakao.actionbase.test.hbase.HBaseTestingClusterConfig
import com.kakao.actionbase.test.hbase.HBaseTestingClusterExtension
import com.kakao.actionbase.test.hbase.OperationHelper.perform

import kotlin.test.assertEquals
import kotlin.test.assertNull

import org.apache.hadoop.hbase.client.Get
import org.apache.hadoop.hbase.client.Table
import org.apache.hadoop.hbase.util.Bytes
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

@ExtendWith(HBaseTestingClusterExtension::class)
class PerCellTTLTest(
    private val table: Table,
    config: HBaseTestingClusterConfig,
) {
    private val family = config.columnFamily
    private val qualifier = Constants.DEFAULT_QUALIFIER

    @ParameterizedTest(name = "{0}")
    @CsvSource(
        delimiter = '|',
        nullValues = ["null"],
        value = [
            "Basic TTL expired      | Put(20), Delay(50)              | null",
            "Basic TTL active       | Put(50), Delay(20)              | value_0",
            "Put then TTL expired   | Put, Put(20), Delay(50)         | value_0",
            "Put then TTL active    | Put, Put(50), Delay(20)         | value_1",
            "TTL then Put           | Put(20), Put, Delay(50)         | value_1",
            "Multiple Puts TTL      | Put, Put, Put(20), Delay(50)    | value_1",
            "Put TTL Delete         | Put, Put(20), Delete, Delay(50) | null",
            "Put Delete no delay    | Put, Delete                     | null",
            "TTL Delete immediate   | Put(20), Delete                 | null",
            "Delete then Put        | Delete, Put                     | value_1",
            "Delete then TTL        | Delete, Put(20), Delay(50)      | null",
            "Short delay TTL active | Put(20), Delay(5)               | value_0",
            "Increment then Delete  | Increment, Delete               | null",
            "Multiple Increments    | Increment, Increment, Increment | 3",
        ],
    )
    fun testHBaseTTLOperations(
        name: String,
        operations: String,
        expected: String?,
    ) {
        // when
        val rowKey = Bytes.toBytes("row_${name.hashCode()}")
        table.perform(rowKey, family, qualifier, "value_", operations)

        // then
        val get = Get(rowKey)
        val result = table.get(get)
        val actualValue = result.getValue(family, qualifier)

        // Verify result
        when (expected) {
            null -> {
                assertNull(actualValue, "Expected null but got: ${actualValue?.let { parseValue(it) }}")
            }
            else -> {
                val actualString = actualValue?.let { parseValue(it) }
                assertEquals(expected, actualString, "Expected $expected but got: $actualString")
            }
        }
        println()
    }

    private fun parseValue(bytes: ByteArray): String =
        try {
            // Try to parse as long first (for increment results)
            Bytes.toLong(bytes).toString()
        } catch (e: Exception) {
            // Fall back to string
            Bytes.toString(bytes)
        }
}
