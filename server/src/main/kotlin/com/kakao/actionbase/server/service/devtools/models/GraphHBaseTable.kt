package com.kakao.actionbase.server.service.devtools.models

import com.kakao.actionbase.server.service.devtools.util.NameSortUtil

import org.apache.hadoop.hbase.KeepDeletedCells

data class GraphHBaseTable(
    val name: String,
    val isEnabled: Boolean,
    val bloomFilter: String,
    val inMemory: Boolean,
    val versions: Int,
    val keepDeletedCells: KeepDeletedCells,
    val dataBlockEncoding: String,
    val compression: String,
    val ttl: Int,
    val minVersions: Int,
    val blockCache: Boolean,
    val blockSize: Int,
    val replicationScope: Int,
)

data class GraphHBaseTableView(
    val fullName: String,
    val prefix: String,
    val timestamp: String?,
    val isEnabled: Boolean = true,
    val isLatest: Boolean = false,
    val regionNum: Int = 0,
    val totalStoreSize: String = "",
) {
    companion object {
        fun from(dto: GraphHBaseTable): GraphHBaseTableView {
            val (prefix, timestamp) = NameSortUtil.extractPrefixAndTimestamp(dto.name)
            return GraphHBaseTableView(dto.name, prefix, timestamp, isEnabled = dto.isEnabled)
        }

        fun sort(tables: List<GraphHBaseTableView>): List<GraphHBaseTableView> =
            NameSortUtil.sortList(tables, { it.fullName }) { entry, isLatest ->
                entry.copy(isLatest = isLatest)
            }
    }
}
