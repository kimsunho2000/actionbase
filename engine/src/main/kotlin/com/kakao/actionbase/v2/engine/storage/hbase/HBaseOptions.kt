package com.kakao.actionbase.v2.engine.storage.hbase

import com.kakao.actionbase.v2.core.code.hbase.Constants
import com.kakao.actionbase.v2.engine.compat.DefaultHBaseCluster
import com.kakao.actionbase.v2.engine.storage.hbase.impl.NewMockTable
import com.kakao.actionbase.v2.engine.util.getLogger

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.TableName
import org.apache.hadoop.hbase.client.AsyncConnection
import org.apache.hadoop.hbase.client.ConnectionFactory
import org.apache.hadoop.hbase.client.mock.MockHTable
import org.slf4j.LoggerFactory

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

/**
 * This supports only HBase 2.4 or below.
 * To support HBase 2.5, use [com.kakao.actionbase.v2.engine.compat.DefaultHBaseCluster] with zkHosts=default.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class HBaseOptions(
    val mock: Boolean = false,
    val namespace: String = "",
    val tableName: String = "",
    val tablePrefix: String = "",
    val tableSuffix: String = "",
    val zkHosts: String = "",
    val rpcTimeout: Int?,
    val clientOperationTimeout: Int?,
    val clientPause: Int?,
    val clientRetries: Int?,
    val clientScannerTimeoutPeriod: Int?,
    val useLockTable: Boolean = false,
) {
    private val logger = LoggerFactory.getLogger(HBaseOptions::class.java)

    private val useDefaultHBaseCluster = DefaultHBaseCluster.isForDefaultHBaseCluster(zkHosts)
    private val useMockConnection = mock || (useDefaultHBaseCluster && DefaultHBaseCluster.INSTANCE.mock)

    fun checkConnection(): Mono<Boolean> =
        // Mock or DefaultHBaseCluster connections is always available.
        if (useMockConnection || useDefaultHBaseCluster) {
            Mono.just(true)
        } else {
            getWarmUpConnection()
                .map {
                    it.close()
                    true
                }.subscribeOn(Schedulers.boundedElastic())
                .onErrorResume {
                    log.error("Failed to connect to HBase", it)
                    Mono.just(false)
                }
        }

    private fun toHBaseConfiguration(): Configuration {
        val c = newConfiguration()
        c.set("hbase.zookeeper.quorum", zkHosts)
        rpcTimeout?.let { c.set("hbase.rpc.timeout", it.toString()) }
        clientOperationTimeout?.let { c.set("hbase.client.operation.timeout", it.toString()) }
        clientPause?.let { c.set("hbase.client.pause", it.toString()) }
        clientRetries?.let { c.set("hbase.client.retries.number", it.toString()) }
        clientScannerTimeoutPeriod?.let { c.set("hbase.client.scanner.timeout.period", it.toString()) }
        return c
    }

    private fun toWarmUpHBaseConfiguration(): Configuration {
        val c = newConfiguration()
        c.set("hbase.zookeeper.quorum", zkHosts)
        rpcTimeout?.let { c.set("hbase.rpc.timeout", it.toString()) }
        clientOperationTimeout?.let { c.set("hbase.client.operation.timeout", it.toString()) }
        clientPause?.let { c.set("hbase.client.pause", it.toString()) }
        clientScannerTimeoutPeriod?.let { c.set("hbase.client.scanner.timeout.period", it.toString()) }
        c.setInt("zookeeper.recovery.retry", 1)
        c.setInt("hbase.client.retries.number", 1)
        return c
    }

    private fun getWarmUpConnection(): Mono<AsyncConnection> = Mono.fromFuture(ConnectionFactory.createAsyncConnection(toWarmUpHBaseConfiguration()))

    /**
     * // DefaultHBaseCluster = DHC
     * | mock  | useDHC | DHC.mock ||| connection    | namespace       | note                               |
     * |-------|--------|----------|||---------------|-----------------|------------------------------------|
     * | true  | -      | -        ||| mock          | given namespace | original logic                     |
     * | false | false  | -        ||| get or create | given namespace | original logic                     |
     * |-------|--------|----------|||---------------|-----------------|------------------------------------|
     * | false | true   | true     ||| mock          | given namespace | same as original logic             |
     * | false | true   | false    ||| DHC with      | DHC.namespace   | use already created DHC connection |
     */
    fun getTables(): Mono<HBaseTables> =
        if (useMockConnection) {
            logger.info("Using MockHBase for tableName: {}", tableName)
            val conn = HBaseConnections.getMockConnection(namespace)
            val table = NewMockTable(conn.getTable(TableName.valueOf("edges")) as MockHTable)
            val hbaseTable = HBaseTable.create(table)
            Mono.just(HBaseTables(hbaseTable, hbaseTable))
        } else if (useDefaultHBaseCluster) {
            require(!DefaultHBaseCluster.INSTANCE.mock)
            logger.info("🚀 Using DefaultHBaseCluster for tableName: {} (using namespace: {})", tableName, DefaultHBaseCluster.INSTANCE.namespace)
            DefaultHBaseCluster.INSTANCE.connectionMono
                .map { connection ->
                    val edgeTable = connection.getTable(TableName.valueOf(DefaultHBaseCluster.INSTANCE.namespace, tableName))
                    val hbaseTable = HBaseTable.create(edgeTable)
                    HBaseTables(hbaseTable, hbaseTable)
                }.cache()
        } else {
            HBaseConnections
                .getConnection(zkHosts, toHBaseConfiguration())
                .map {
                    if (tableName.isNotEmpty()) {
                        val edgeTable = it.getTable(TableName.valueOf(namespace, tableName))
                        val hbaseTable = HBaseTable.create(edgeTable)
                        HBaseTables(hbaseTable, hbaseTable)
                    } else {
                        val edgeTable = it.getTable(getEdgeTableName(namespace, tablePrefix, tableSuffix))
                        val lockTable =
                            if (useLockTable) {
                                it.getTable(getLockTableName(namespace, tablePrefix, tableSuffix))
                            } else {
                                // use edgeTable as lockTable
                                edgeTable
                            }
                        HBaseTables(
                            HBaseTable.create(edgeTable),
                            HBaseTable.create(lockTable),
                        )
                    }
                }.cache()
        }

    companion object {
        val log = getLogger()

        fun newConfiguration(): Configuration = Configuration()

        private fun getTableName(
            namespace: String,
            tablePrefix: String,
            name: String,
            tableSuffix: String,
        ): TableName {
            val fullName =
                listOfNotNull(
                    tablePrefix.takeIf { it.isNotEmpty() },
                    name,
                    tableSuffix.takeIf { it.isNotEmpty() },
                ).joinToString(separator = "_")
            return TableName.valueOf(namespace, fullName)
        }

        fun getEdgeTableName(
            namespace: String,
            tablePrefix: String = "",
            tableSuffix: String = "",
        ): TableName = getTableName(namespace, tablePrefix, Constants.EDGE_TABLE_NAME, tableSuffix)

        fun getLockTableName(
            namespace: String,
            tablePrefix: String = "",
            tableSuffix: String = "",
        ): TableName = getTableName(namespace, tablePrefix, Constants.LOCK_TABLE_NAME, tableSuffix)
    }
}
