package com.kakao.actionbase.v2.engine.service.ddl

import com.kakao.actionbase.v2.core.edge.TraceEdge
import com.kakao.actionbase.v2.engine.Graph
import com.kakao.actionbase.v2.engine.audit.Audit
import com.kakao.actionbase.v2.engine.edge.HashEdge
import com.kakao.actionbase.v2.engine.entity.EntityFactory
import com.kakao.actionbase.v2.engine.entity.EntityName
import com.kakao.actionbase.v2.engine.entity.QueryEntity
import com.kakao.actionbase.v2.engine.label.Label

import reactor.core.publisher.Mono

class QueryDdlService(
    graph: Graph,
    label: Label,
    factory: EntityFactory<QueryEntity>,
) : DdlService<QueryEntity, QueryCreateRequest, QueryUpdateRequest, QueryDeleteRequest>(graph, label, factory) {
    override fun canDeactivate(name: EntityName): Mono<Boolean> = Mono.just(true)

    override fun toEntity(edge: HashEdge): QueryEntity = QueryEntity.toEntity(edge)

    override fun sync(): Mono<Void> = graph.updateQueries()
}

data class QueryCreateRequest(
    val desc: String,
    val query: String,
    val stats: List<String>,
    override val audit: Audit = Audit.default,
) : DdlRequest {
    private fun toEntity(name: EntityName): QueryEntity =
        QueryEntity(
            active = true,
            name = name,
            desc = desc,
            query = query,
            stats = stats,
        )

    override fun toEdge(name: EntityName): TraceEdge = toEntity(name).toEdge()
}

data class QueryUpdateRequest(
    val active: Boolean?,
    val desc: String?,
    val query: String?,
    val stats: List<String>?,
    override val audit: Audit = Audit.default,
) : DdlRequest {
    private fun toNotNullMap(): Map<String, Any> =
        buildMap {
            active?.let { put("props_active", it) }
            desc?.let { put("desc", it) }
            query?.let { put("query", it) }
            stats?.let { put("stats", it.joinToString(",")) }
        }

    override fun toEdge(name: EntityName): TraceEdge = name.toTraceEdge(props = toNotNullMap())
}

data class QueryDeleteRequest(
    override val audit: Audit = Audit.default,
) : DdlRequest {
    override fun toEdge(name: EntityName): TraceEdge = name.toTraceEdge()
}
