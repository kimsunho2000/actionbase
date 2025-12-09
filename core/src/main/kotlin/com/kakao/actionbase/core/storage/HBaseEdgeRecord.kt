package com.kakao.actionbase.core.storage

import com.kakao.actionbase.core.edge.record.EdgeRecord

data class HBaseEdgeRecord(
    val status: String,
    val timestamp: Long,
    val content: EdgeRecord,
) {
    data class Key(
        val status: String,
        val timestamp: Long,
        val content: EdgeRecord.Key,
    )
}

fun EdgeRecord.toHBaseEdgeRecord(
    status: String,
    timestamp: Long,
): HBaseEdgeRecord = HBaseEdgeRecord(status, timestamp, this)

fun EdgeRecord.Key.toHBaseEdgeRecordKey(
    status: String,
    timestamp: Long,
): HBaseEdgeRecord.Key = HBaseEdgeRecord.Key(status, timestamp, this)
