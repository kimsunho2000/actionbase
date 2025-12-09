package com.kakao.actionbase.v2.engine.entity

import com.kakao.actionbase.v2.core.edge.Edge
import com.kakao.actionbase.v2.core.edge.TraceEdge
import com.kakao.actionbase.v2.engine.GraphGlobalValues
import com.kakao.actionbase.v2.engine.metadata.Metadata

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty

data class EntityName(
    @JsonIgnore
    val service: String,
    @JsonIgnore
    val name: String? = null,
) {
    @get:JsonProperty("name")
    val fullQualifiedName = if (name == null) service else "$service.$name"

    @get:JsonIgnore
    val phaseServiceName: String = "$phase:$service"

    @get:JsonIgnore
    val nameNotNull: String
        get() = name ?: error("name is null")

    fun shiftNameToService(): EntityName = EntityName(nameNotNull)

    fun toTraceEdge(
        ts: Long = System.currentTimeMillis(),
        props: Map<String, Any?> = emptyMap(),
    ): TraceEdge =
        Edge(
            ts,
            phaseServiceName,
            nameNotNull,
            props,
        ).toTraceEdge()

    override fun toString(): String = fullQualifiedName

    override fun hashCode(): Int = fullQualifiedName.hashCode()

    override fun equals(other: Any?): Boolean = other is EntityName && fullQualifiedName == other.fullQualifiedName

    companion object {
        val phase: String = GraphGlobalValues.phase

        val tenant: String = GraphGlobalValues.tenant

        fun initialize(
            phase: String,
            tenant: String,
        ) {
            require(phase == this.phase) { "phase is mismatched: $phase != ${this.phase}" }
            require(tenant == this.tenant) { "tenant is mismatched: $tenant != ${this.tenant}" }
        }

        fun of(fullQualifiedName: String): EntityName {
            val parts = fullQualifiedName.split(".")
            return when (parts.size) {
                1 -> EntityName(stripPhase(parts[0]))
                2 -> EntityName(stripPhase(parts[0]), parts[1])
                else -> throw IllegalArgumentException("Invalid full name: $fullQualifiedName")
            }
        }

        fun withPhase(
            service: String,
            name: String,
        ): EntityName = EntityName(stripPhase(service), name)

        private fun stripPhase(fullServiceName: String): String = fullServiceName.substringAfter(':')

        fun fromOrigin(name: String): EntityName = EntityName(Metadata.origin, name)

        val origin = EntityName(Metadata.origin)

        @JvmStatic
        @JsonCreator
        fun create(
            @JsonProperty("name") name: String,
        ): EntityName = of(name)
    }
}
