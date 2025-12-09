package com.kakao.actionbase.v2.engine.entity.deprecated

import com.kakao.actionbase.v2.core.types.EdgeSchema
import com.kakao.actionbase.v2.core.types.Field
import com.kakao.actionbase.v2.core.types.VertexField
import com.kakao.actionbase.v2.core.types.VertexType

data class DeprecatedEdgeSchema(
    val src: VertexType,
    val tgt: VertexType,
    val fields: List<Field>,
) {
    fun toEdgeSchema(): EdgeSchema =
        EdgeSchema(
            VertexField(src, "this format is deprecated."),
            VertexField(tgt, "this format is deprecated."),
            fields,
        )
}
