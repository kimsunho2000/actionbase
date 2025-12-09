package com.kakao.actionbase.core.edge.record

import com.kakao.actionbase.core.edge.Edge
import com.kakao.actionbase.core.edge.EdgeState
import com.kakao.actionbase.core.edge.mutation.EdgeMutationBuilder
import com.kakao.actionbase.core.state.AbstractSchema
import com.kakao.actionbase.core.state.State
import com.kakao.actionbase.core.state.StateValue

data class EdgeStateRecord(
    override val key: Key,
    val value: Value,
) : EdgeRecord() {
    data class Key(
        val source: Any,
        val tableCode: Int,
        override val recordTypeCode: Byte,
        val target: Any,
    ) : EdgeRecord.Key() {
        init {
            require(recordTypeCode == EdgeRecordType.EDGE_STATE.code) {
                "Invalid record type code: $recordTypeCode, expected: ${EdgeRecordType.EDGE_STATE.code}"
            }
        }

        companion object {
            fun of(
                source: Any,
                tableCode: Int,
                target: Any,
            ): Key =
                Key(
                    source = source,
                    tableCode = tableCode,
                    recordTypeCode = EdgeRecordType.EDGE_STATE.code,
                    target = target,
                )
        }
    }

    data class Value(
        val active: Boolean,
        val version: Long,
        val createdAt: Long?,
        val deletedAt: Long?,
        val properties: Map<Int, StateValue>,
    )

    // for Read on Read-Modify-Write
    fun toState(info: AbstractSchema): EdgeState =
        EdgeState(
            source = key.source,
            target = key.target,
            state =
                State.create(
                    active = value.active,
                    version = value.version,
                    createdAt = value.createdAt,
                    deletedAt = value.deletedAt,
                    properties =
                        value.properties.mapNotNull { (code, value) ->
                            info.nameOfOrNull(code)?.let { it to value }
                        },
                ),
        )

    // for 'get' query
    fun toEdge(info: AbstractSchema): Edge =
        Edge(
            version = value.version,
            source = key.source,
            target = key.target,
            properties =
                value.properties
                    .mapNotNull { (code, value) ->
                        info.nameOfOrNull(code)?.let { it to value.value }
                    }.toMap(),
        )

    fun toMultiEdgeStateRecord(): EdgeStateRecord {
        val id = value.properties[EdgeMutationBuilder.MULTI_EDGE_ID_CODE]?.value ?: throw IllegalStateException("Multi-edge ID not found in properties")
        val version = value.version
        val sourceTargetStateValues = listOf(EdgeMutationBuilder.MULTI_EDGE_SOURCE_CODE to StateValue(version, key.source), EdgeMutationBuilder.MULTI_EDGE_TARGET_CODE to StateValue(version, key.target))
        return EdgeStateRecord(
            key =
                Key.of(
                    source = id,
                    tableCode = key.tableCode,
                    target = id,
                ),
            value =
                Value(
                    active = value.active,
                    version = value.version,
                    createdAt = value.createdAt,
                    deletedAt = value.deletedAt,
                    properties = value.properties - EdgeMutationBuilder.MULTI_EDGE_ID_CODE + sourceTargetStateValues,
                ),
        )
    }

    companion object {
        fun of(
            source: Any,
            target: Any,
            state: State,
            tableCode: Int,
        ) = EdgeStateRecord(
            key =
                Key.of(
                    source = source,
                    tableCode = tableCode,
                    target = target,
                ),
            value =
                Value(
                    active = state.active,
                    version = state.version,
                    createdAt = state.createdAt,
                    deletedAt = state.deletedAt,
                    properties =
                        state.properties
                            .map { (name, value) ->
                                AbstractSchema.codeOf(name) to value
                            }.toMap(),
                ),
        )
    }
}
