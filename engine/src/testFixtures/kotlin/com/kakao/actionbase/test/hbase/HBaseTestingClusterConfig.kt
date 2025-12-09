package com.kakao.actionbase.test.hbase

import org.apache.hadoop.hbase.TableName

class HBaseTestingClusterConfig(
    val tableName: TableName,
    val columnFamily: ByteArray,
)
