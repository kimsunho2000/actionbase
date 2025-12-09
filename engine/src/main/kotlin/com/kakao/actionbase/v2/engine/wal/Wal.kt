package com.kakao.actionbase.v2.engine.wal

import com.kakao.actionbase.v2.core.edge.TraceEdge
import com.kakao.actionbase.v2.core.metadata.EdgeOperation
import com.kakao.actionbase.v2.engine.audit.Audit
import com.kakao.actionbase.v2.engine.entity.EntityName
import com.kakao.actionbase.v2.engine.metadata.MutationModeContext
import com.kakao.actionbase.v2.engine.producer.Producer

import reactor.core.publisher.Mono

class Wal(
    val producer: Producer,
) {
    fun write(walLog: WalLog): Mono<Void> = producer.produce(walLog)

    fun write(
        alias: EntityName,
        label: EntityName,
        edge: TraceEdge,
        op: EdgeOperation,
        audit: Audit,
        requestId: String,
        mode: MutationModeContext,
    ): Mono<Void> {
        val aliasOrNull = if (alias == label) null else alias
        return write(WalLog(aliasOrNull, label, edge, op, mode, audit, requestId))
    }

    fun writeHeartBeat(
        labelName: EntityName,
        hostName: String,
    ): Mono<Void> = producer.produceHeartBeat(labelName, hostName)
}
