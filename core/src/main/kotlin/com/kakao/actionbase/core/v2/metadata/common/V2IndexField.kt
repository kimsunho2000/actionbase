package com.kakao.actionbase.core.v2.metadata.common

import com.kakao.actionbase.core.java.codec.common.hbase.Order

data class V2IndexField(
    val name: String,
    val order: Order,
)
