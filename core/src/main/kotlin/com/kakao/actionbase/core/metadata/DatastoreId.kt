package com.kakao.actionbase.core.metadata

data class DatastoreId(
    val tenant: String,
    val datastore: String,
) : Id
