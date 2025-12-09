package com.kakao.actionbase.server.service.devtools

import com.kakao.actionbase.server.configuration.HBaseProperties
import com.kakao.actionbase.server.service.devtools.models.GraphHBaseTable
import com.kakao.actionbase.v2.engine.storage.hbase.HBaseConnections

import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.TableName
import org.apache.hadoop.hbase.client.AsyncConnection
import org.apache.hadoop.hbase.client.TableDescriptor
import org.springframework.stereotype.Service

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

@Service
class HBaseAdminService(
    hbaseProperties: HBaseProperties,
) {
    data class HBaseClusterInfo(
        val zkHosts: List<String>,
        val zkPort: String,
    ) {
        val zkQuorum: String
            get() = zkHosts.joinToString(",")

        val zkQuorumWithPort: String
            get() = zkHosts.joinToString(",") { "$it:$zkPort" }
    }

    val hbaseClusters = hbaseProperties.cluster

    val hBaseTableCreator = HBaseTableCreator()

    private val hBaseMetric = HBaseMetric()

    fun getOrCreateHBaseConnection(cluster: String): Mono<AsyncConnection> {
        val clusterInfo = hbaseClusters[cluster] ?: throw IllegalArgumentException("Invalid cluster name: $cluster")
        val config =
            HBaseConfiguration.create().apply {
                set("hbase.zookeeper.quorum", clusterInfo.zkQuorum)
                set("hbase.zookeeper.property.clientPort", clusterInfo.zkPort)
            }

        // If already connected, return the existing connection.
        // The connection closes when the Graph instance is closed.
        // Previously, zkHosts (containing the port number) was used in the implementation.
        // The new implementation separates zkQuorum and zkPort.
        // As a result, the cache key is different, preventing the connection from being shared with other requests.
        return HBaseConnections.getConnection(clusterInfo.zkQuorum, config)
    }

    fun listClusters(): Mono<List<Map<String, String>>> = Mono.just(hbaseClusters.map { mapOf("name" to it.key, "zookeeperQuorum" to it.value.zkQuorumWithPort) })

    fun getCluster(cluster: String): Mono<Map<String, String>> {
        val clusterInfo = hbaseClusters[cluster] ?: throw IllegalArgumentException("Invalid cluster name: $cluster")
        return Mono.just(mapOf("name" to cluster, "zookeeperQuorum" to clusterInfo.zkQuorumWithPort))
    }

    fun listTables(
        cluster: String,
        namespacePrefix: String = "kc_graph",
    ): Mono<List<GraphHBaseTable>> =
        getOrCreateHBaseConnection(cluster).flatMap { connection ->
            val admin = connection.admin
            Mono
                .fromFuture(admin.listTableDescriptors())
                .flatMap { tableDescriptors ->
                    val filteredTableNames =
                        tableDescriptors.filter {
                            it.tableName.namespaceAsString.startsWith(
                                namespacePrefix,
                            )
                        }
                    val tableDetailsMonos =
                        filteredTableNames.map { tableDescriptor ->
                            Mono
                                .fromFuture(admin.isTableEnabled(tableDescriptor.tableName))
                                .map { isEnabled -> makeTableInfo(isEnabled, tableDescriptor) }
                                .subscribeOn(Schedulers.boundedElastic())
                        }
                    Flux.mergeSequential(tableDetailsMonos).collectList()
                }
        }

    fun getTable(
        cluster: String,
        tableName: String,
    ): Mono<GraphHBaseTable> =
        getOrCreateHBaseConnection(cluster).flatMap { connection ->
            val admin = connection.admin
            Mono
                .fromFuture(admin.listTableDescriptors(listOf(TableName.valueOf(tableName))))
                .map { it.first() } // not safe - just ported original code x[0]
                .flatMap { tableDescriptor ->
                    Mono
                        .fromFuture(admin.isTableEnabled(tableDescriptor.tableName))
                        .map { isEnabled -> makeTableInfo(isEnabled, tableDescriptor) }
                }
        }

    fun disableTable(
        cluster: String,
        tableName: String,
    ): Mono<Void> =
        getOrCreateHBaseConnection(cluster).flatMap { connection ->
            val admin = connection.admin
            Mono.fromFuture(admin.disableTable(TableName.valueOf(tableName)))
        }

    fun enableTable(
        cluster: String,
        tableName: String,
    ): Mono<Void> =
        getOrCreateHBaseConnection(cluster).flatMap { connection ->
            val admin = connection.admin
            Mono.fromFuture(admin.enableTable(TableName.valueOf(tableName)))
        }

    fun deleteTable(
        cluster: String,
        tableName: String,
    ): Mono<Void> =
        getOrCreateHBaseConnection(cluster).flatMap { connection ->
            val admin = connection.admin
            val table = TableName.valueOf(tableName)
            Mono
                .fromFuture(admin.isTableEnabled(table))
                .flatMap { isEnabled ->
                    if (isEnabled) {
                        Mono.error(IllegalArgumentException("Table $tableName is enabled, delete after disable it."))
                    } else {
                        Mono.fromFuture(admin.deleteTable(table))
                    }
                }
        }

    fun existTable(
        cluster: String,
        tableName: String,
    ): Mono<Boolean> =
        getOrCreateHBaseConnection(cluster)
            .flatMap { connection ->
                val admin = connection.admin
                val table = TableName.valueOf(tableName)
                Mono.fromFuture(admin.tableExists(table))
            }.onErrorResume { Mono.just(false) }

    fun createTable(
        cluster: String,
        namespace: String,
        tableName: String,
        schema: HBaseTableSchema = hBaseTableCreator.getDefaultSchema(),
    ): Mono<Void> =
        getOrCreateHBaseConnection(cluster).flatMap { connection ->
            val admin = connection.admin
            Mono.fromFuture(hBaseTableCreator.createTable(namespace, tableName, schema, admin))
        }

    fun getRegionInfo(
        cluster: String,
        tableName: String,
    ): Mono<List<HBaseMetric.RegionMetric>> =
        getOrCreateHBaseConnection(cluster).flatMap { connection ->
            hBaseMetric.getRegionInfo(connection, tableName)
        }

    fun getReplicationPeer(cluster: String): Mono<List<Map<String, Any>>> =
        getOrCreateHBaseConnection(cluster).flatMap { connection ->
            Mono
                .fromFuture(connection.admin.listReplicationPeers())
                .map { peers ->
                    peers.map { peer ->
                        mapOf(
                            "peerId" to peer.peerId,
                            "peerConfig" to peer.peerConfig,
                        )
                    }
                }
        }

    fun makeTableInfo(
        isEnabled: Boolean,
        descriptor: TableDescriptor,
    ): GraphHBaseTable {
        val cf = descriptor.columnFamilies[0]
        return GraphHBaseTable(
            name = descriptor.tableName.nameAsString,
            isEnabled = isEnabled,
            bloomFilter = cf.bloomFilterType.name,
            inMemory = cf.isInMemory,
            versions = cf.maxVersions,
            keepDeletedCells = cf.keepDeletedCells,
            dataBlockEncoding = cf.dataBlockEncoding.name,
            compression = cf.compressionType.name,
            ttl = cf.timeToLive,
            minVersions = cf.minVersions,
            blockCache = cf.isBlockCacheEnabled,
            blockSize = cf.blocksize,
            replicationScope = cf.scope,
        )
    }
}
