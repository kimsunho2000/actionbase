package com.kakao.actionbase.v2.engine.entity

import com.kakao.actionbase.v2.engine.edge.HashEdge
import com.kakao.actionbase.v2.engine.sql.DataFrame
import com.kakao.actionbase.v2.engine.sql.RowWithSchema

interface EntityFactory<Entity : EdgeEntity> {
    fun fromDataFrame(df: DataFrame): List<Entity> = df.toRowWithSchema().map { toEntity(it) }

    fun toEntity(edge: HashEdge): Entity

    fun toEntity(row: RowWithSchema): Entity
}
