package com.kakao.actionbase.engine.binding.datastore.hbase

import com.kakao.actionbase.engine.datastore.hbase.admin.HBaseAdmin
import com.kakao.actionbase.engine.datastore.hbase.admin.HBaseTableSchema
import com.kakao.actionbase.test.hbase.HBaseTestingClusterExtension

import org.apache.hadoop.hbase.client.AsyncConnection
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.test.test

@ExtendWith(HBaseTestingClusterExtension::class)
class HBaseAdminScenarioTest(
    connection: AsyncConnection,
) {
    val admin = HBaseAdmin(Mono.just(connection.admin))

    @Test
    fun scenarioCompleteHBaseManagementWorkflow() {
        // 1. Create and initialize DatastoreBinding
        assertNotNull(admin)

        val namespace = "scenario_test_${System.currentTimeMillis()}"
        val tableName = "test_table"

        // 2. Create Namespace
        admin
            .createNamespace(namespace)
            .test()
            .verifyComplete()

        // 3. Create Table
        val schema =
            HBaseTableSchema(
                columnFamilyName = "f".toByteArray(),
                bloomFilter = "ROW",
                inMemory = false,
                keepDeletedCells = "FALSE",
                dataBlockEncoding = "FAST_DIFF",
                compression = "LZ4",
                ttl = "forever",
                maxVersions = 1,
                minVersions = 0,
                blockCache = true,
                blockSize = 65536,
                numRegions = 1,
            )

        admin
            .createTable(namespace, tableName, schema)
            .test()
            .verifyComplete()

        // 4. Retrieve and verify Table (no preparation time needed due to retry logic)
        admin
            .getTable(namespace, tableName)
            .test()
            .assertNext { table ->
                assertNotNull(table)
                assertEquals("$namespace:$tableName", table.name)
                assertEquals(namespace, table.namespace)
                assertEquals(tableName, table.tableName)
                assertEquals("ROW", table.bloomFilter)
                assertEquals("LZ4", table.compression)
            }.verifyComplete()

        // 5. Retrieve Table list
        admin
            .getTables(namespace)
            .test()
            .assertNext { tables ->
                assertNotNull(tables)
                assertTrue(tables.any { it.name == "$namespace:$tableName" })
            }.verifyComplete()

        // 6. Disable Table
        admin
            .disableTable(namespace, tableName)
            .test()
            .verifyComplete()

        // 7. Enable Table
        admin
            .enableTable(namespace, tableName)
            .test()
            .verifyComplete()

        // 8. Retrieve Table metrics
        admin
            .getTableMetricSummary(namespace, tableName)
            .test()
            .assertNext { metrics ->
                assertNotNull(metrics)
            }.verifyComplete()

        // 9. Delete Table (after disabling)
        admin
            .disableTable(namespace, tableName)
            .then(admin.deleteTable(namespace, tableName))
            .test()
            .verifyComplete()
    }

    @Test
    fun scenarioMultipleTablesCreationAndManagement() {
        val namespace = "scenario_multi_${System.currentTimeMillis()}"

        admin
            .createNamespace(namespace)
            .test()
            .verifyComplete()

        val schema =
            HBaseTableSchema(
                columnFamilyName = "f".toByteArray(),
                numRegions = 1,
            )

        // Create multiple tables
        val tableNames = listOf("table1", "table2", "table3")
        Flux
            .fromIterable(tableNames)
            .flatMap { tableName -> admin.createTable(namespace, tableName, schema) }
            .then()
            .test()
            .verifyComplete()

        // Verify all tables are retrieved
        admin
            .getTables(namespace)
            .test()
            .assertNext { tables ->
                assertEquals(tableNames.size, tables.size)
                tableNames.forEach { tableName ->
                    assertTrue(tables.any { it.tableName == tableName })
                }
            }.verifyComplete()

        // Retrieve each table individually
        tableNames.forEach { tableName ->
            admin
                .getTable(namespace, tableName)
                .test()
                .assertNext { table -> assertEquals(tableName, table.tableName) }
                .verifyComplete()
        }
    }
}
