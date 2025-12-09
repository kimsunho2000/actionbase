package com.kakao.actionbase.core.v2.metadata

import com.kakao.actionbase.core.metadata.Id

data class V2AliasId(
    val service: String,
    val alias: String,
) : Id
