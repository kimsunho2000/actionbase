package com.kakao.actionbase.v2.engine.entity

import com.kakao.actionbase.v2.core.edge.TraceEdge
import com.kakao.actionbase.v2.engine.edge.HashEdge
import com.kakao.actionbase.v2.engine.service.ddl.ServiceCreateRequest
import com.kakao.actionbase.v2.engine.sql.RowWithSchema

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class ServiceEntity(
    override val active: Boolean,
    override val name: EntityName,
    val desc: String,
) : EdgeEntity {
    @get:JsonProperty("name")
    override val fullName: String
        get() = name.nameNotNull

    override fun toEdge(): TraceEdge =
        name.toTraceEdge(
            props =
                mapOf(
                    "desc" to desc,
                ),
        )

    fun toCreateRequest(): ServiceCreateRequest =
        ServiceCreateRequest(
            desc = desc,
        )

    companion object : EntityFactory<ServiceEntity> {
        override fun toEntity(edge: HashEdge): ServiceEntity =
            ServiceEntity(
                active = (edge.props.getOrDefault("props_active", null) ?: true).toString().toBoolean(),
                name = EntityName.withPhase(edge.src.toString(), edge.tgt.toString()),
                desc = edge.props["desc"].toString(),
            )

        override fun toEntity(row: RowWithSchema): ServiceEntity =
            ServiceEntity(
                active = (row.getOrNull("props_active") ?: true).toString().toBoolean(),
                name = EntityName.withPhase(row.getString("src"), row.getString("tgt")),
                desc = row.getString("desc"),
            )

        @JvmStatic
        @JsonCreator
        fun fromJson(
            @JsonProperty("active") active: Boolean,
            @JsonProperty("name") fullName: String,
            @JsonProperty("desc") desc: String,
        ): ServiceEntity = ServiceEntity(active, EntityName.fromOrigin(fullName), desc)
    }
}
