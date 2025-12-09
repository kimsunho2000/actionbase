package com.kakao.actionbase.v2.engine.storage

import com.kakao.actionbase.v2.engine.entity.EntityName
import com.kakao.actionbase.v2.engine.entity.StorageEntity

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

interface Storage<T> {
    val entity: StorageEntity

    val name: EntityName
        get() = entity.name

    val options: T

    companion object {
        val jackson = jacksonObjectMapper()

        inline fun <reified T> parseOptions(conf: JsonNode): T = jackson.readValue(jackson.treeAsTokens(conf), T::class.java)
    }
}
