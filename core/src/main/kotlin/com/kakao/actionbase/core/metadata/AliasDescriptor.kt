package com.kakao.actionbase.core.metadata

import com.kakao.actionbase.core.Constants

import com.fasterxml.jackson.annotation.JsonIgnore

data class AliasDescriptor(
    override val tenant: String,
    val database: String,
    val alias: String,
    val table: String,
    override val active: Boolean = true,
    override val comment: String = Constants.DEFAULT_COMMENT,
    override val revision: Long = Constants.DEFAULT_REVISION,
    override val createdAt: Long = Constants.DEFAULT_CREATED_AT,
    override val createdBy: String = Constants.DEFAULT_CREATED_BY,
    override val updatedAt: Long = Constants.DEFAULT_UPDATED_AT,
    override val updatedBy: String = Constants.DEFAULT_UPDATED_BY,
) : V3Descriptor<AliasId> {
    @JsonIgnore
    override val id: AliasId = AliasId(tenant, database, alias)
}
