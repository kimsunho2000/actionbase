package com.kakao.actionbase.v2.engine.storage.hbase

import com.kakao.actionbase.v2.engine.util.getLogger

import java.util.concurrent.ConcurrentHashMap

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.client.AsyncConnection
import org.apache.hadoop.hbase.client.Connection
import org.apache.hadoop.hbase.client.ConnectionFactory
import org.apache.hadoop.hbase.client.mock.MockConnection

import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

object HBaseConnections {
    private val logger = getLogger()

    private val connections: ConcurrentHashMap<String, Mono<AsyncConnection>> = ConcurrentHashMap()

    private var mockConnections: ConcurrentHashMap<String, Connection> = ConcurrentHashMap()

    fun getMockConnection(namespace: String): Connection =
        mockConnections.computeIfAbsent(namespace) {
            MockConnection(HBaseOptions.newConfiguration())
        }

    fun getConnection(
        zkHosts: String,
        hbaseConfig: Configuration,
    ): Mono<AsyncConnection> {
        val cacheKey = getCacheKey(zkHosts, hbaseConfig)

        return connections.computeIfAbsent(cacheKey) { key ->
            Mono
                .defer {
                    Mono
                        .fromFuture(ConnectionFactory.createAsyncConnection(hbaseConfig))
                        .doOnSuccess {
                            logger.info("Successfully established a new HBase async connection for cacheKey: {}", key)
                        }.doOnError { error ->
                            logger.error(
                                "Failed to establish a new HBase async connection for cacheKey: {}",
                                key,
                                error,
                            )
                            connections.remove(key)
                        }
                }.cache()
        }
    }

    fun getCacheKey(
        zkHosts: String,
        hbaseConfig: Configuration,
    ): String {
        var hash = 7
        for (entry in hbaseConfig.sortedBy { it.key }) {
            hash = 31 * hash + (entry.key?.hashCode() ?: 0)
            hash = 31 * hash + (entry.value?.hashCode() ?: 0)
        }
        return "$zkHosts:$hash"
    }

    fun closeConnections(): Mono<Void> {
        val closeMonos =
            connections.entries.map { (key, connectionMono) ->
                connectionMono
                    .flatMap { connection ->
                        Mono
                            .fromRunnable<Void> {
                                try {
                                    connection.close()
                                    logger.info("Closed connection for cacheKey: {}", key)
                                } catch (e: Exception) {
                                    logger.error("Error closing connection for cacheKey: {}", key, e)
                                }
                            }.subscribeOn(Schedulers.boundedElastic())
                    }
            }
        return Mono.`when`(closeMonos).doFinally { connections.clear() }
    }
}
