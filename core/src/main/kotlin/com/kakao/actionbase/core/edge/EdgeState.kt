package com.kakao.actionbase.core.edge

import com.kakao.actionbase.core.edge.record.EdgeStateRecord
import com.kakao.actionbase.core.state.AbstractSchema
import com.kakao.actionbase.core.state.State

data class EdgeState(
    val source: Any,
    val target: Any,
    val state: State,
) {
    // for storage
    fun toRecord(tableCode: Int): EdgeStateRecord =
        EdgeStateRecord(
            key =
                EdgeStateRecord.Key.of(
                    tableCode = tableCode,
                    source = source,
                    target = target,
                ),
            value =
                EdgeStateRecord.Value(
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
