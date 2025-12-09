package com.kakao.actionbase.core.edge.record

import com.kakao.actionbase.core.metadata.common.Direction

data class EdgeCountRecord(
    override val key: Key,
    val value: Long,
) : EdgeRecord() {
    data class Key(
        val directedSource: Any,
        val tableCode: Int,
        override val recordTypeCode: Byte,
        val direction: Direction,
    ) : EdgeRecord.Key() {
        init {
            require(recordTypeCode == EdgeRecordType.EDGE_COUNT.code) {
                "Invalid record type code: $recordTypeCode, expected: ${EdgeRecordType.EDGE_COUNT.code}"
            }
        }

        companion object {
            fun of(
                directedSource: Any,
                tableCode: Int,
                direction: Direction,
            ): Key =
                Key(
                    directedSource = directedSource,
                    tableCode = tableCode,
                    recordTypeCode = EdgeRecordType.EDGE_COUNT.code,
                    direction = direction,
                )
        }
    }
}
