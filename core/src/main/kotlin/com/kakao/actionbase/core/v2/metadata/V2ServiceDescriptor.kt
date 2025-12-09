package com.kakao.actionbase.core.v2.metadata

import com.kakao.actionbase.core.metadata.DatabaseDescriptor

import com.fasterxml.jackson.annotation.JsonIgnore

data class V2ServiceDescriptor(
    val name: String,
    override val desc: String,
    override val active: Boolean,
) : V2Descriptor<V2ServiceId> {
    @JsonIgnore
    override val id: V2ServiceId = V2ServiceId(name)

    fun toV3(tenant: String): DatabaseDescriptor =
        DatabaseDescriptor(
            tenant = tenant,
            database = name,
            active = active,
            comment = desc,
        )
}
