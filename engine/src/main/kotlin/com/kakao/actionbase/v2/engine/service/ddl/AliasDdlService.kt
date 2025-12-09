package com.kakao.actionbase.v2.engine.service.ddl

import com.kakao.actionbase.v2.core.edge.TraceEdge
import com.kakao.actionbase.v2.engine.Graph
import com.kakao.actionbase.v2.engine.audit.Audit
import com.kakao.actionbase.v2.engine.edge.HashEdge
import com.kakao.actionbase.v2.engine.entity.AliasEntity
import com.kakao.actionbase.v2.engine.entity.EntityFactory
import com.kakao.actionbase.v2.engine.entity.EntityName
import com.kakao.actionbase.v2.engine.label.Label
import com.kakao.actionbase.v2.engine.service.ddl.DdlExceptionMessage.labelNameAlreadyExists
import com.kakao.actionbase.v2.engine.service.ddl.DdlExceptionMessage.targetLabelNotExists

import reactor.core.publisher.Mono

class AliasDdlService(
    graph: Graph,
    label: Label,
    factory: EntityFactory<AliasEntity>,
) : DdlService<AliasEntity, AliasCreateRequest, AliasUpdateRequest, AliasDeleteRequest>(graph, label, factory) {
    override fun createPreconditions(
        name: EntityName,
        request: AliasCreateRequest,
    ): Array<Mono<List<String>>> =
        arrayOf(
            graph.checkLabelExists(name).map { if (!it) listOf() else listOf(labelNameAlreadyExists(name)) },
            checkTargetExists(request),
        )

    private fun checkTargetExists(request: AliasCreateRequest): Mono<List<String>> =
        graph
            .checkLabelExists(EntityName.of(request.target))
            .map { if (it) listOf() else listOf(targetLabelNotExists(EntityName.of(request.target))) }

    override fun canDeactivate(name: EntityName): Mono<Boolean> = Mono.just(true)

    override fun toEntity(edge: HashEdge): AliasEntity = AliasEntity.toEntity(edge)

    override fun sync(): Mono<Void> = graph.updateAliases()
}

data class AliasCreateRequest(
    val desc: String,
    val target: String,
    override val audit: Audit = Audit.default,
) : DdlRequest {
    private fun toEntity(name: EntityName): AliasEntity =
        AliasEntity(
            active = true,
            name = name,
            desc = desc,
            target = EntityName.of(target),
        )

    override fun toEdge(name: EntityName): TraceEdge = toEntity(name).toEdge()
}

data class AliasUpdateRequest(
    val active: Boolean?,
    val desc: String?,
    val target: String?,
    override val audit: Audit = Audit.default,
) : DdlRequest {
    private fun toNotNullMap(): Map<String, Any> =
        buildMap {
            active?.let { put("props_active", it) }
            desc?.let { put("desc", it) }
            target?.let { put("target", it) }
        }

    override fun toEdge(name: EntityName): TraceEdge = name.toTraceEdge(props = toNotNullMap())
}

data class AliasDeleteRequest(
    override val audit: Audit = Audit.default,
) : DdlRequest {
    override fun toEdge(name: EntityName): TraceEdge = name.toTraceEdge()
}
