package com.kakao.actionbase.engine.datastore.hbase.admin

import org.apache.hadoop.hbase.HConstants

/**
 * HBase Table Schema definition
 */
class HBaseTableSchema(
    val columnFamilyName: ByteArray,
    val bloomFilter: String = "ROW",
    val inMemory: Boolean = false,
    val keepDeletedCells: String = "FALSE",
    val dataBlockEncoding: String = "FAST_DIFF",
    val compression: String = "LZ4",
    val ttl: String = "forever",
    val maxVersions: Int = 1,
    val minVersions: Int = 0,
    val blockCache: Boolean = true,
    val blockSize: Int = 65536,
    val numRegions: Int = 32,
    val replicationScope: Int = HConstants.REPLICATION_SCOPE_LOCAL,
) {
    companion object {
        val DEFAULT = HBaseTableSchema("f".toByteArray())
    }
}
