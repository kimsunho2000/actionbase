package com.kakao.actionbase.core.edge.record

import com.kakao.actionbase.core.metadata.common.Direction

data class EdgeLockRecord(
    override val key: Key,
    val value: Long,
) : EdgeRecord() {
    data class Key(
        val source: Any,
        val tableCode: Int,
        override val recordTypeCode: Byte,
        val target: Any,
        val direction: Direction,
    ) : EdgeRecord.Key() {
        init {
            require(recordTypeCode == EdgeRecordType.EDGE_LOCK.code) {
                "Invalid record type code: $recordTypeCode, expected: ${EdgeRecordType.EDGE_LOCK.code}"
            }
        }

        companion object {
            fun of(
                source: Any,
                tableCode: Int,
                target: Any,
                direction: Direction,
            ): Key =
                Key(
                    source = source,
                    tableCode = tableCode,
                    recordTypeCode = EdgeRecordType.EDGE_LOCK.code,
                    target = target,
                    direction = direction,
                )
        }
    }
}
