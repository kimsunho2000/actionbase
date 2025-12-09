package com.kakao.actionbase.server.service.devtools

import java.util.concurrent.CompletableFuture

import org.apache.hadoop.hbase.KeepDeletedCells
import org.apache.hadoop.hbase.TableName
import org.apache.hadoop.hbase.client.AsyncAdmin
import org.apache.hadoop.hbase.client.ColumnFamilyDescriptorBuilder
import org.apache.hadoop.hbase.client.TableDescriptorBuilder
import org.apache.hadoop.hbase.io.compress.Compression
import org.apache.hadoop.hbase.io.encoding.DataBlockEncoding
import org.apache.hadoop.hbase.regionserver.BloomType
import org.apache.hadoop.hbase.util.RegionSplitter

data class HBaseTableSchema(
    val columnFamilyName: ByteArray,
    val bloomFilter: String,
    val inMemory: Boolean,
    val keepDeletedCells: String,
    val dataBlockEncoding: String,
    val compression: String,
    val ttl: String,
    val maxVersions: Int,
    val minVersions: Int,
    val blockCache: Boolean,
    val blockSize: Int,
    val numRegions: Int,
)

class HBaseTableCreator {
    fun createTable(
        namespace: String,
        tableName: String,
        hBaseTableSchema: HBaseTableSchema,
        admin: AsyncAdmin,
    ): CompletableFuture<Void> {
        val tableDescriptor =
            TableDescriptorBuilder
                .newBuilder(TableName.valueOf(namespace, tableName))
                .setColumnFamily(
                    ColumnFamilyDescriptorBuilder
                        .newBuilder(hBaseTableSchema.columnFamilyName)
                        .setBloomFilterType(BloomType.valueOf(hBaseTableSchema.bloomFilter))
                        .setInMemory(hBaseTableSchema.inMemory)
                        .setMinVersions(hBaseTableSchema.minVersions)
                        .setMaxVersions(hBaseTableSchema.maxVersions)
                        .setKeepDeletedCells(KeepDeletedCells.valueOf(hBaseTableSchema.keepDeletedCells))
                        .setDataBlockEncoding(DataBlockEncoding.valueOf(hBaseTableSchema.dataBlockEncoding))
                        .setCompressionType(Compression.Algorithm.valueOf(hBaseTableSchema.compression))
                        .setTimeToLive(hBaseTableSchema.ttl)
                        .setBlockCacheEnabled(hBaseTableSchema.blockCache)
                        .setBlocksize(hBaseTableSchema.blockSize)
                        .build(),
                ).setSplitEnabled(true)
                .setReplicationScope(0)
                .build()

        return admin.createTable(tableDescriptor, RegionSplitter.UniformSplit().split(hBaseTableSchema.numRegions))
    }

    fun getDefaultSchema(): HBaseTableSchema =
        HBaseTableSchema(
            columnFamilyName = "f".toByteArray(), // assuming column family name as "f"
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
            numRegions = 32,
        )
}
