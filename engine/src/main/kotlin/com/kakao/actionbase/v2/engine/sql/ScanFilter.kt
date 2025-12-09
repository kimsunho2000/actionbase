package com.kakao.actionbase.v2.engine.sql

import com.kakao.actionbase.v2.core.metadata.Direction
import com.kakao.actionbase.v2.engine.entity.EntityName
import com.kakao.actionbase.v2.engine.util.getLogger

data class ScanFilter(
    val name: EntityName,
    val srcSet: Set<Any>,
    val tgt: Set<Any>? = null,
    val selectFields: List<String> = defaultSelectFields,
    val dir: Direction = defaultDir,
    val limit: Int = defaultLimit,
    val offset: String? = null,
    val indexName: String? = null,
    val otherPredicates: Set<WherePredicate> = emptySet(),
    val selfEdge: Boolean = false,
) {
    @Suppress("PropertyName")
    companion object {
        private val log = getLogger()

        const val defaultLimit: Int = 10
        val defaultDir: Direction = Direction.OUT
        val defaultSelectFields: List<String> = listOf("*")
    }
}
