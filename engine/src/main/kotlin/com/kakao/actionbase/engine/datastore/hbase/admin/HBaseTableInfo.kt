package com.kakao.actionbase.engine.datastore.hbase.admin

/**
 * Data class containing HBase Table information
 */
data class HBaseTableInfo(
    val name: String,
    val isEnabled: Boolean,
    val bloomFilter: String,
    val inMemory: Boolean,
    val versions: Int,
    val keepDeletedCells: String,
    val dataBlockEncoding: String,
    val compression: String,
    val ttl: Int,
    val minVersions: Int,
    val blockCache: Boolean,
    val blockSize: Int,
    val replicationScope: Int,
) {
    val namespace: String
        get() =
            if (name.contains(":")) {
                name.split(":")[0]
            } else {
                "default"
            }

    val tableName: String
        get() =
            if (name.contains(":")) {
                name.substringAfter(":")
            } else {
                name
            }
}
