package com.kakao.actionbase.v2.engine.edge

import com.kakao.actionbase.v2.core.metadata.Active

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty

data class HashEdge(
    @JsonIgnore
    val active: Active,
    val ts: Long,
    val src: Any,
    val tgt: Any,
    val props: Map<String, Any>,
) {
    @get:JsonProperty("active")
    val booleanActive: Boolean
        get() = active == Active.ACTIVE
}
