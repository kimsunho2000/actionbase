package com.kakao.actionbase.server.api.graph.v3.datastore.hbase

import com.kakao.actionbase.engine.datastore.hbase.admin.HBaseTableSchema

data class HBaseTableCreateRequest(
    val columnFamilyName: String?,
    val bloomFilter: String?,
    val inMemory: Boolean?,
    val keepDeletedCells: String?,
    val dataBlockEncoding: String?,
    val compression: String?,
    val ttl: String?,
    val maxVersions: Int?,
    val minVersions: Int?,
    val blockCache: Boolean?,
    val blockSize: Int?,
    val numRegions: Int?,
    val replicationScope: Int?,
) {
    fun toHBaseTableSchema(): HBaseTableSchema =
        HBaseTableSchema(
            columnFamilyName =
                columnFamilyName?.toByteArray()
                    ?: HBaseTableSchema.DEFAULT.columnFamilyName,
            bloomFilter = bloomFilter ?: HBaseTableSchema.DEFAULT.bloomFilter,
            inMemory = inMemory ?: HBaseTableSchema.DEFAULT.inMemory,
            keepDeletedCells = keepDeletedCells ?: HBaseTableSchema.DEFAULT.keepDeletedCells,
            dataBlockEncoding = dataBlockEncoding ?: HBaseTableSchema.DEFAULT.dataBlockEncoding,
            compression = compression ?: HBaseTableSchema.DEFAULT.compression,
            ttl = ttl ?: HBaseTableSchema.DEFAULT.ttl,
            maxVersions = maxVersions ?: HBaseTableSchema.DEFAULT.maxVersions,
            minVersions = minVersions ?: HBaseTableSchema.DEFAULT.minVersions,
            blockCache = blockCache ?: HBaseTableSchema.DEFAULT.blockCache,
            blockSize = blockSize ?: HBaseTableSchema.DEFAULT.blockSize,
            numRegions = numRegions ?: HBaseTableSchema.DEFAULT.numRegions,
            replicationScope = replicationScope ?: HBaseTableSchema.DEFAULT.replicationScope,
        )
}
