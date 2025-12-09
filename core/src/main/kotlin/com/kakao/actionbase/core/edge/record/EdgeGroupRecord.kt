package com.kakao.actionbase.core.edge.record

import com.kakao.actionbase.core.metadata.common.Direction

data class EdgeGroupRecord(
    override val key: Key,
    val qualifier: Qualifier,
    val value: Long, // update by Increment atomically
    val ttl: Long? = null,
) : EdgeRecord() {
    data class Key(
        val directedSource: Any,
        val tableCode: Int,
        override val recordTypeCode: Byte,
        val direction: Direction,
        val groupCode: Int,
    ) : EdgeRecord.Key() {
        init {
            require(recordTypeCode == EdgeRecordType.EDGE_GROUP.code) {
                "Invalid record type code: $recordTypeCode, expected: ${EdgeRecordType.EDGE_GROUP.code}"
            }
        }

        companion object {
            fun of(
                directedSource: Any,
                tableCode: Int,
                direction: Direction,
                groupCode: Int,
            ) = Key(
                directedSource = directedSource,
                tableCode = tableCode,
                recordTypeCode = EdgeRecordType.EDGE_GROUP.code,
                direction = direction,
                groupCode = groupCode,
            )
        }
    }

    data class Qualifier(
        val groupValues: List<Any?>,
    )
}
