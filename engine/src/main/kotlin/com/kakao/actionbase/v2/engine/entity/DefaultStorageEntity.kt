package com.kakao.actionbase.v2.engine.entity

import com.kakao.actionbase.v2.engine.metadata.StorageType

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

data class DefaultStorageEntity(
    val type: StorageType,
    val conf: Map<String, Any>,
) {
    fun toStorageEntity(entityName: EntityName): StorageEntity =
        StorageEntity(
            active = true,
            name = entityName,
            desc = "system default storage",
            type = type,
            conf = objectMapper.valueToTree(conf),
        )

    companion object {
        private val objectMapper = jacksonObjectMapper()
    }
}
