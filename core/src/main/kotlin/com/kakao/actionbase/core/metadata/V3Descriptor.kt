package com.kakao.actionbase.core.metadata

interface V3Descriptor<ID : Id> {
    val id: ID

    val active: Boolean

    val tenant: String

    val comment: String

    val revision: Long

    val createdAt: Long

    val createdBy: String

    val updatedAt: Long

    val updatedBy: String
}
