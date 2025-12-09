package com.kakao.actionbase.core.metadata

import com.kakao.actionbase.core.metadata.common.DatastoreType

data class DatastoreDescriptor(
    val type: DatastoreType,
    val configuration: Map<String, String>,
)
