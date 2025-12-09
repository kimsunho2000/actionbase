package com.kakao.actionbase.core.metadata.common

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeName

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type",
)
@JsonSubTypes(
    JsonSubTypes.Type(value = Storage.HBase::class, name = "hbase"),
)
sealed class Storage {
    @JsonTypeName("hbase")
    data class HBase(
        val tableName: String,
    ) : Storage()
}
