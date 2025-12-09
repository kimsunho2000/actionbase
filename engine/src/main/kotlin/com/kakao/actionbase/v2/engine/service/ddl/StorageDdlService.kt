package com.kakao.actionbase.v2.engine.service.ddl

import com.kakao.actionbase.v2.core.edge.TraceEdge
import com.kakao.actionbase.v2.engine.Graph
import com.kakao.actionbase.v2.engine.audit.Audit
import com.kakao.actionbase.v2.engine.edge.HashEdge
import com.kakao.actionbase.v2.engine.entity.EntityFactory
import com.kakao.actionbase.v2.engine.entity.EntityName
import com.kakao.actionbase.v2.engine.entity.StorageEntity
import com.kakao.actionbase.v2.engine.entity.StorageEntity.Companion.toJsonString
import com.kakao.actionbase.v2.engine.label.Label
import com.kakao.actionbase.v2.engine.metadata.StorageType

import com.fasterxml.jackson.databind.JsonNode

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

class StorageDdlService(
    graph: Graph,
    label: Label,
    factory: EntityFactory<StorageEntity>,
) : DdlService<StorageEntity, StorageCreateRequest, StorageUpdateRequest, StorageDeleteRequest>(graph, label, factory) {
    override fun canDeactivate(name: EntityName): Mono<Boolean> =
        graph.serviceDdl
            .getAll(EntityName.origin)
            .flatMap { service ->
                Flux
                    .fromIterable(service.content)
                    .map { serviceEntity -> serviceEntity.name.shiftNameToService() }
                    .flatMap { serviceName ->
                        graph.labelDdl.getAll(serviceName)
                    }.map { label ->
                        label.content.none { labelEntity ->
                            labelEntity.active && labelEntity.storage == name.name
                        }
                    }.all { it }
            }

    override fun toEntity(edge: HashEdge): StorageEntity = StorageEntity.toEntity(edge)

    override fun sync(): Mono<Void> = graph.updateStorages()
}

data class StorageCreateRequest(
    val desc: String,
    val type: StorageType,
    val conf: JsonNode,
    override val audit: Audit = Audit.default,
) : DdlRequest {
    private fun toEntity(name: EntityName): StorageEntity =
        StorageEntity(
            active = true,
            name = name,
            desc = desc,
            type = type,
            conf = conf,
        )

    override fun toEdge(name: EntityName): TraceEdge = toEntity(name).toEdge()
}

data class StorageUpdateRequest(
    val active: Boolean?,
    val desc: String?,
    val type: StorageType?,
    val conf: JsonNode?,
    override val audit: Audit = Audit.default,
) : DdlRequest {
    private fun toNotNullMap(): Map<String, Any> =
        buildMap {
            active?.let { put("props_active", it) }
            desc?.let { put("desc", it) }
            type?.let { put("type", it.name) }
            conf?.let { put("conf", it.toJsonString()) }
        }

    override fun toEdge(name: EntityName): TraceEdge = name.toTraceEdge(props = toNotNullMap())
}

data class StorageDeleteRequest(
    override val audit: Audit = Audit.default,
) : DdlRequest {
    override fun toEdge(name: EntityName): TraceEdge = name.toTraceEdge()
}
