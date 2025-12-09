package com.kakao.actionbase.v2.engine.entity

import com.kakao.actionbase.v2.core.edge.TraceEdge
import com.kakao.actionbase.v2.engine.Graph
import com.kakao.actionbase.v2.engine.edge.HashEdge
import com.kakao.actionbase.v2.engine.service.ddl.AliasCreateRequest
import com.kakao.actionbase.v2.engine.sql.RowWithSchema

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty

data class AliasEntity(
    override val active: Boolean,
    override val name: EntityName,
    val desc: String,
    @JsonIgnore
    val target: EntityName,
    val label: LabelEntity? = null,
) : EdgeEntity {
    @get:JsonProperty("target")
    val targetName: String
        get() = target.fullQualifiedName

    override fun toEdge(): TraceEdge =
        name.toTraceEdge(
            props =
                mapOf(
                    "props_active" to active,
                    "desc" to desc,
                    "target" to target.fullQualifiedName,
                ),
        )

    fun toCreateRequest(): AliasCreateRequest =
        AliasCreateRequest(
            desc = desc,
            target = target.fullQualifiedName,
        )

    fun withLabel(graph: Graph): AliasEntity = copy(label = graph.getLabel(target).entity)

    companion object : EntityFactory<AliasEntity> {
        override fun toEntity(edge: HashEdge): AliasEntity =
            AliasEntity(
                active = (edge.props.getOrDefault("props_active", null) ?: true).toString().toBoolean(),
                name = EntityName.withPhase(edge.src.toString(), edge.tgt.toString()),
                desc = edge.props["desc"].toString(),
                target = EntityName.of(edge.props["target"].toString()),
            )

        override fun toEntity(row: RowWithSchema): AliasEntity =
            AliasEntity(
                active = (row.getOrNull("props_active") ?: true).toString().toBoolean(),
                name = EntityName.withPhase(row.getString("src"), row.getString("tgt")),
                desc = row.getString("desc"),
                target = EntityName.of(row.getString("target")),
            )
    }
}
