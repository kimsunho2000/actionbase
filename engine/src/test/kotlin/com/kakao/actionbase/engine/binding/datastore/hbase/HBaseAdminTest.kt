package com.kakao.actionbase.engine.binding.datastore.hbase

import com.kakao.actionbase.engine.datastore.hbase.admin.HBaseAdmin
import com.kakao.actionbase.engine.datastore.hbase.admin.HBaseTableSchema
import com.kakao.actionbase.test.hbase.HBaseTestingClusterExtension

import java.util.UUID

import org.apache.hadoop.hbase.client.AsyncConnection
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

import reactor.core.publisher.Mono
import reactor.kotlin.test.test
import reactor.test.StepVerifier

@ExtendWith(HBaseTestingClusterExtension::class)
class HBaseAdminTest(
    connection: AsyncConnection,
) {
    private val admin = HBaseAdmin(Mono.just(connection.admin))
    private var testNamespace: String = makeTestAlphanumericName()
    private val defaultSchema =
        HBaseTableSchema(
            columnFamilyName = "f".toByteArray(),
            numRegions = 1,
        )

    @BeforeEach
    fun setUp() {
        admin.createNamespace(testNamespace).block()
    }

    @Test
    fun shouldCreateNamespace() {
        val namespace = "test_namespace_${System.currentTimeMillis()}"

        admin
            .createNamespace(namespace)
            .test()
            .verifyComplete()
    }

    @Test
    fun shouldCreateAndGetTables() {
        val tableName = makeTestAlphanumericName()

        StepVerifier
            .create(admin.createTable(testNamespace, tableName, defaultSchema))
            .verifyComplete()

        StepVerifier
            .create(admin.getTables(testNamespace))
            .assertNext { tables ->
                assertNotNull(tables.firstOrNull { it.tableName == tableName })
                // Table list may be empty
            }.verifyComplete()
    }

    @Test
    fun shouldGetTable() {
        val tableName = makeTestAlphanumericName()

        createTestTable(tableName)
            .then(admin.getTable(testNamespace, tableName))
            .test()
            .assertNext { hbaseTable ->
                assertNotNull(hbaseTable)
                assertEquals(testNamespace, hbaseTable.namespace)
                assertEquals(tableName, hbaseTable.tableName)
            }.verifyComplete()
    }

    @Test
    fun shouldEnableTable() {
        createTestTable()
            .flatMap { admin.enableTable(testNamespace, it) }
            .test()
            .verifyComplete()
    }

    @Test
    fun shouldDisableTable() {
        createTestTable()
            .flatMap { admin.disableTable(testNamespace, it) }
            .test()
            .verifyComplete()
    }

    @Test
    fun shouldDeleteTable() {
        createTestTable()
            .flatMap { admin.deleteTable(testNamespace, it) }
            .test()
            .verifyComplete()
    }

    @Test
    fun shouldGetTableMetricSummary() {
        createTestTable()
            .flatMap { admin.getTableMetricSummary(testNamespace, it) }
            .test()
            .assertNext { metrics -> assertNotNull(metrics) }
            .verifyComplete()
    }

    private fun createTestTable(tableName: String = makeTestAlphanumericName()): Mono<String> = admin.createTable(testNamespace, tableName, defaultSchema).thenReturn(tableName)

    companion object {
        private fun makeTestAlphanumericName(): String = UUID.randomUUID().toString().replace("-", "")
    }
}
