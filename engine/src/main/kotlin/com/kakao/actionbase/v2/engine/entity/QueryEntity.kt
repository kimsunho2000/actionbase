package com.kakao.actionbase.v2.engine.entity

import com.kakao.actionbase.v2.core.edge.TraceEdge
import com.kakao.actionbase.v2.engine.edge.HashEdge
import com.kakao.actionbase.v2.engine.service.ddl.QueryCreateRequest
import com.kakao.actionbase.v2.engine.sql.RowWithSchema

data class QueryEntity(
    override val active: Boolean,
    override val name: EntityName,
    val desc: String,
    val query: String,
    val stats: List<String>,
) : EdgeEntity {
    override fun toEdge(): TraceEdge =
        name.toTraceEdge(
            props =
                mapOf(
                    "props_active" to active,
                    "desc" to desc,
                    "query" to query,
                    "stats" to stats.joinToString(","),
                ),
        )

    fun toCreateRequest(): QueryCreateRequest =
        QueryCreateRequest(
            desc = desc,
            query = query,
            stats = stats,
        )

    companion object : EntityFactory<QueryEntity> {
        override fun toEntity(edge: HashEdge): QueryEntity =
            QueryEntity(
                active = (edge.props.getOrDefault("props_active", null) ?: true).toString().toBoolean(),
                name = EntityName.withPhase(edge.src.toString(), edge.tgt.toString()),
                desc = edge.props["desc"].toString(),
                query = edge.props["query"].toString(),
                stats = edge.props["stats"].toString().split(","),
            )

        override fun toEntity(row: RowWithSchema): QueryEntity =
            QueryEntity(
                active = (row.getOrNull("props_active") ?: true).toString().toBoolean(),
                name = EntityName.withPhase(row.getString("src"), row.getString("tgt")),
                desc = row.getString("desc"),
                query = row.getString("query"),
                stats = row.getString("stats").split(","),
            )
    }
}
