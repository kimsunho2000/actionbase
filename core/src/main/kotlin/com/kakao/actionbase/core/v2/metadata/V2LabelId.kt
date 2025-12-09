package com.kakao.actionbase.core.v2.metadata

import com.kakao.actionbase.core.metadata.Id

data class V2LabelId(
    val service: String,
    val label: String,
) : Id
