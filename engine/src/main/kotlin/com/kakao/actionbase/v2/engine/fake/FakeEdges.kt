package com.kakao.actionbase.v2.engine.fake

import com.kakao.actionbase.v2.core.edge.Edge
import com.kakao.actionbase.v2.core.types.DataType
import com.kakao.actionbase.v2.core.types.VertexType
import com.kakao.actionbase.v2.engine.entity.LabelEntity

const val STRING_TEST_VALUE = "__test__value__" // this should be URL-safe.

fun LabelEntity.fakeEdges(): List<Edge> {
    val srcs =
        when (schema.src.type) {
            VertexType.LONG -> listOf(Long.MIN_VALUE)
            else -> listOf(STRING_TEST_VALUE)
        }

    val tgts =
        when (schema.tgt.type) {
            VertexType.LONG -> listOf(Long.MIN_VALUE)
            else -> listOf(STRING_TEST_VALUE)
        }

    val props =
        schema.fields.associate {
            val fakeValue =
                when (it.type) {
                    DataType.BYTE -> Byte.MIN_VALUE
                    DataType.SHORT -> Short.MIN_VALUE
                    DataType.INT -> Int.MIN_VALUE
                    DataType.LONG -> Long.MIN_VALUE
                    DataType.BOOLEAN -> false
                    DataType.FLOAT -> Float.MIN_VALUE
                    DataType.DOUBLE -> Double.MIN_VALUE
                    DataType.DECIMAL -> Long.MIN_VALUE.toBigDecimal()
                    else -> STRING_TEST_VALUE
                }
            it.name to fakeValue
        }

    return srcs.flatMap { src ->
        tgts.map { tgt ->
            Edge(System.currentTimeMillis(), src, tgt, props)
        }
    }
}
