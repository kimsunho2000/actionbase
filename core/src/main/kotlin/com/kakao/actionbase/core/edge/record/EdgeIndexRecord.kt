package com.kakao.actionbase.core.edge.record

import com.kakao.actionbase.core.edge.Edge
import com.kakao.actionbase.core.java.codec.common.hbase.Order
import com.kakao.actionbase.core.metadata.common.Direction
import com.kakao.actionbase.core.state.AbstractSchema

data class EdgeIndexRecord(
    override val key: Key,
    val value: Value,
) : EdgeRecord() {
    data class Key(
        val prefix: Prefix,
        val suffix: Suffix,
    ) : EdgeRecord.Key() {
        override val recordTypeCode: Byte = prefix.recordTypeCode

        data class Prefix(
            val directedSource: Any,
            val tableCode: Int,
            val recordTypeCode: Byte,
            val direction: Direction,
            val indexCode: Int,
            val indexValues: List<IndexValue>,
        ) {
            init {
                require(recordTypeCode == EdgeRecordType.EDGE_INDEX.code) {
                    "Invalid record type code: $recordTypeCode, expected: ${EdgeRecordType.EDGE_INDEX.code}"
                }
            }

            companion object {
                fun of(
                    tableCode: Int,
                    directedSource: Any,
                    direction: Direction,
                    indexCode: Int,
                    indexValues: List<IndexValue>,
                ): Prefix =
                    Prefix(
                        tableCode = tableCode,
                        directedSource = directedSource,
                        recordTypeCode = EdgeRecordType.EDGE_INDEX.code,
                        direction = direction,
                        indexCode = indexCode,
                        indexValues = indexValues,
                    )
            }
        }

        data class Suffix(
            val restIndexValues: List<IndexValue>,
            val directedTarget: Any,
        )

        data class IndexValue(
            val value: Any?,
            val order: Order,
        )
    }

    data class Value(
        val version: Long,
        val properties: Map<Int, Any?>,
    )

    // for 'scan' query
    fun toEdge(info: AbstractSchema): Edge {
        val (source, target) =
            when (key.prefix.direction) {
                Direction.OUT -> key.prefix.directedSource to key.suffix.directedTarget
                Direction.IN -> key.suffix.directedTarget to key.prefix.directedSource
            }

        return Edge(
            version = value.version,
            source = source,
            target = target,
            properties =
                value.properties
                    .mapNotNull { (code, value) ->
                        info.nameOfOrNull(code)?.let { it to value }
                    }.toMap(),
        )
    }
}
