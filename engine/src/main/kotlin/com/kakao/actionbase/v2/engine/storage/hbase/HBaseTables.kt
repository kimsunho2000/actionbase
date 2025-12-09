package com.kakao.actionbase.v2.engine.storage.hbase

data class HBaseTables(
    val edge: HBaseTable,
    val lock: HBaseTable,
)
