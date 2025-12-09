package com.kakao.actionbase.core.v2.metadata

import com.kakao.actionbase.core.metadata.Id

interface V2Descriptor<ID : Id> {
    val id: ID

    val active: Boolean

    val desc: String
}
