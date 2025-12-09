package com.kakao.actionbase.v2.engine.consistency

import com.kakao.actionbase.v2.core.edge.TraceEdge
import com.kakao.actionbase.v2.core.metadata.EdgeOperation
import com.kakao.actionbase.v2.engine.consistency.EventualConsistencySpec.IdGenerator

data class EventSequence(
    val items: List<OpAndEventTime>,
) {
    fun name(): String = items.map { it.op.name.first() }.joinToString("")
}

data class OpAndEventTime(
    val op: EdgeOperation,
    val ts: Long,
)

data class TraceEdgeMutation(
    val op: EdgeOperation,
    val edge: TraceEdge,
)

data class MutationRequest(
    val items: List<TraceEdgeMutation>,
) {
    override fun toString(): String =
        items.joinToString(
            ", ",
            "[[",
            "]]",
        ) { "${it.op.name.first()} ${it.edge.ts} ${it.edge.props.toMap()}" }
}

data class TestSetup(
    val src: IdGenerator,
    val tgt: IdGenerator,
    val useLegacy: Boolean,
    val base: BaseTestSetup,
)

data class BaseTestSetup(
    val insertReceivedFrom: String?,
    val updateReceivedFrom: String?,
    val insertPermission: String?,
    val updatePermission: String?,
) {
    fun create(
        src: IdGenerator,
        tgt: IdGenerator,
        useLegacy: Boolean,
    ): TestSetup =
        TestSetup(
            src,
            tgt,
            useLegacy,
            this,
        )
}
