package com.kakao.actionbase.v2.engine.storage.hbase

import org.apache.hadoop.hbase.TableName
import org.apache.hadoop.hbase.io.compress.Compression
import org.apache.hadoop.hbase.io.encoding.DataBlockEncoding
import org.apache.hadoop.hbase.regionserver.BloomType

data class HBaseTableCreateRequest(
    val tableName: TableName,
    val compressionAlgorithm: Compression.Algorithm?,
    val dataBlockEncoding: DataBlockEncoding?,
    val bloomType: BloomType?,
)
