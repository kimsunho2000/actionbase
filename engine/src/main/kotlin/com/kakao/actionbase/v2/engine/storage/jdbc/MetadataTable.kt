package com.kakao.actionbase.v2.engine.storage.jdbc

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime

class MetadataTable private constructor(
    name: String,
) : BaseTable(name) {
    val k = varchar("k", 512).uniqueIndex("udx_${name}_k")
    val v = text("v")

    companion object {
        val legacy = get("kc_graph_metadata")

        fun get(name: String): MetadataTable = MetadataTable(name)
    }
}

abstract class BaseTable(
    name: String,
) : LongIdTable(name) {
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
    val createdBy = varchar("created_by", 256)
    val modifiedAt = datetime("modified_at").defaultExpression(CurrentDateTime)
    val modifiedBy = varchar("modified_by", 256)
    val updateTs = datetime("update_ts").defaultExpression(CurrentDateTime)

    init {
        index("idx_${name}_createdat", false, createdAt)
        index("idx_${name}_updatets", false, updateTs)
    }
}

object BaseTableConstants {
    const val MAX_LENGTH = 40
}
