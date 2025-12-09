package com.kakao.actionbase.v2.engine.entity

import com.kakao.actionbase.v2.core.edge.TraceEdge

import com.fasterxml.jackson.annotation.JsonProperty

interface EdgeEntity {
    fun toEdge(): TraceEdge

    val name: EntityName

    @get:JsonProperty("name")
    val fullName: String
        get() = name.fullQualifiedName

    val active: Boolean
}
