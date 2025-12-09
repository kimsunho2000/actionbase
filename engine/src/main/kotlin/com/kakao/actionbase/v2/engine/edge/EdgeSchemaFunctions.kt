package com.kakao.actionbase.v2.engine.edge

import com.kakao.actionbase.v2.core.code.DecodedEdge
import com.kakao.actionbase.v2.core.code.KeyFieldValue
import com.kakao.actionbase.v2.core.code.KeyValue
import com.kakao.actionbase.v2.core.code.VersionValue
import com.kakao.actionbase.v2.core.edge.SchemaEdge
import com.kakao.actionbase.v2.core.metadata.Direction
import com.kakao.actionbase.v2.core.types.EdgeSchema
import com.kakao.actionbase.v2.core.types.Field

fun List<Field>.ensureType(values: List<Any?>): List<Any?> =
    values.zip(this).map { (value, property) ->
        property.type.cast(value)
    }

fun EdgeSchema.decodeMetastore(encodedValue: KeyValue<String>): SchemaEdge {
    val decodedValue = DecodedEdge.fromMetastore(encodedValue, hashToFieldNameMap)
    return createSchemaEdge(decodedValue)
}

fun EdgeSchema.decodeString(encodedValue: KeyFieldValue<String>): SchemaEdge {
    val decodedValue = DecodedEdge.fromString(encodedValue, hashToFieldNameMap)
    return createSchemaEdge(decodedValue)
}

fun EdgeSchema.decodeByteArray(encodedValue: KeyFieldValue<ByteArray>): SchemaEdge {
    val decodedValue = DecodedEdge.from(encodedValue, hashToFieldNameMap)
    return createSchemaEdge(decodedValue)
}

private fun EdgeSchema.createSchemaEdge(value: DecodedEdge): SchemaEdge {
    val properties = selectProperties(value.propertyAsMap)
    return SchemaEdge(
        value.isActive,
        value.ts,
        value.src,
        value.tgt,
        value.direction ?: Direction.OUT,
        properties,
        this,
        value.propertyAsMap,
    )
}

fun EdgeSchema.selectProperties(values: Map<String, VersionValue>): List<Any?> {
    val typeUnsafeProperties = fields.map { values[it.name]?.value }
    return fields.ensureType(typeUnsafeProperties)
}
