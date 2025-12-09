package com.kakao.actionbase.core.v2.metadata

import com.fasterxml.jackson.annotation.JsonIgnore

data class V2StorageDescriptor(
    val type: String,
    val name: String,
    override val desc: String,
    override val active: Boolean,
    val conf: Map<String, Any>,
) : V2Descriptor<V2StorageId> {
    @JsonIgnore
    override val id: V2StorageId = V2StorageId(name)
}
