package com.kakao.actionbase.v2.engine.service.ddl

import com.kakao.actionbase.v2.core.edge.TraceEdge
import com.kakao.actionbase.v2.engine.Graph
import com.kakao.actionbase.v2.engine.audit.Audit
import com.kakao.actionbase.v2.engine.edge.HashEdge
import com.kakao.actionbase.v2.engine.entity.EntityFactory
import com.kakao.actionbase.v2.engine.entity.EntityName
import com.kakao.actionbase.v2.engine.entity.ServiceEntity
import com.kakao.actionbase.v2.engine.label.Label

import reactor.core.publisher.Mono

class ServiceDdlService(
    graph: Graph,
    label: Label,
    factory: EntityFactory<ServiceEntity>,
) : DdlService<ServiceEntity, ServiceCreateRequest, ServiceUpdateRequest, ServiceDeleteRequest>(graph, label, factory) {
    override fun canDeactivate(name: EntityName): Mono<Boolean> = graph.labelDdl.getAll(name.shiftNameToService()).map { it.content.none { label -> label.active } }

    override fun toEntity(edge: HashEdge): ServiceEntity = ServiceEntity.toEntity(edge)

    override fun sync(): Mono<Void> = graph.updateServices()
}

data class ServiceCreateRequest(
    val desc: String,
    override val audit: Audit = Audit.default,
) : DdlRequest {
    private fun toEntity(name: EntityName): ServiceEntity =
        ServiceEntity(
            active = true,
            name = name,
            desc = desc,
        )

    override fun toEdge(name: EntityName): TraceEdge = toEntity(name).toEdge()
}

data class ServiceUpdateRequest(
    val active: Boolean?,
    val desc: String?,
    override val audit: Audit = Audit.default,
) : DdlRequest {
    private fun toNotNullMap(): Map<String, Any> =
        buildMap {
            active?.let { put("props_active", active) }
            desc?.let { put("desc", desc) }
        }

    override fun toEdge(name: EntityName): TraceEdge = name.toTraceEdge(props = toNotNullMap())
}

data class ServiceDeleteRequest(
    override val audit: Audit = Audit.default,
) : DdlRequest {
    override fun toEdge(name: EntityName): TraceEdge = name.toTraceEdge()
}
