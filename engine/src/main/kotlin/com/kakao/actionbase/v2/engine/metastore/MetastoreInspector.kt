package com.kakao.actionbase.v2.engine.metastore

import com.kakao.actionbase.v2.core.code.DecodedEdge
import com.kakao.actionbase.v2.core.code.KeyValue
import com.kakao.actionbase.v2.engine.GraphDefaults
import com.kakao.actionbase.v2.engine.storage.jdbc.MetadataTable

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Query
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

import reactor.core.publisher.Mono

class MetastoreInspector(
    private val metastore: Database,
    private val table: MetadataTable,
) {
    // Mapping of supported sorting options
    private val supportedSort =
        mapOf(
            "id: ASC" to (table.id to SortOrder.ASC),
            "id: DESC" to (table.id to SortOrder.DESC),
            "k: ASC" to (table.k to SortOrder.ASC),
            "k: DESC" to (table.k to SortOrder.DESC),
        )
    private val defaultSort = table.id to SortOrder.ASC

    /**
     * Builds a query based on the presence of a keyPrefix.
     * If keyPrefix is provided, it filters the query; otherwise, it selects all rows.
     */
    private fun buildQuery(keyPrefix: String?): Query =
        if (keyPrefix != null) {
            table.select { table.k like "$keyPrefix%" }
        } else {
            table.selectAll()
        }

    /**
     * Fetches a list of entries from the metastore, applying sorting, pagination, and optional filtering by keyPrefix.
     */
    fun dumpMetastore(
        limit: Int,
        offset: Long,
        sort: String = "id: ASC",
        keyPrefix: String? = null,
    ): Mono<List<Map<String, Any?>>> {
        val orderBy = supportedSort[sort] ?: defaultSort

        return Mono.fromCallable {
            transaction(metastore) {
                buildQuery(keyPrefix)
                    .orderBy(orderBy.first, orderBy.second)
                    .limit(limit, offset)
                    .map { row ->
                        val encodedEdge = KeyValue(row[table.k], row[table.v])
                        val decodedEdge = runCatching { DecodedEdge.fromMetastore(encodedEdge, emptyMap()) }.getOrNull()

                        mapOf(
                            "id" to row[table.id].value,
                            "k" to row[table.k],
                            "v" to row[table.v],
                            "decoded" to decodedEdge,
                        )
                    }
            }
        }
    }

    /**
     * Returns the total count of entries in the metastore, optionally filtered by a keyPrefix.
     */
    fun getTotalCount(keyPrefix: String?): Mono<Long> =
        Mono.fromCallable {
            transaction(metastore) {
                buildQuery(keyPrefix).count()
            }
        }

    companion object {
        fun createGlobal(graph: GraphDefaults): MetastoreInspector = MetastoreInspector(graph.metastore, graph.metadataTable)

        fun createLocal(graph: GraphDefaults): MetastoreInspector = MetastoreInspector(graph.localMetastore, graph.metadataTable)
    }
}
