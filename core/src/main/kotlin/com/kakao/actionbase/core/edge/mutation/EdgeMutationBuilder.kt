package com.kakao.actionbase.core.edge.mutation

import com.kakao.actionbase.core.codec.XXHash32Wrapper
import com.kakao.actionbase.core.edge.record.EdgeCountRecord
import com.kakao.actionbase.core.edge.record.EdgeGroupRecord
import com.kakao.actionbase.core.edge.record.EdgeIndexRecord
import com.kakao.actionbase.core.edge.record.EdgeStateRecord
import com.kakao.actionbase.core.metadata.common.Direction
import com.kakao.actionbase.core.metadata.common.DirectionType
import com.kakao.actionbase.core.metadata.common.Group
import com.kakao.actionbase.core.metadata.common.GroupType
import com.kakao.actionbase.core.metadata.common.Index
import com.kakao.actionbase.core.metadata.common.SystemProperties
import com.kakao.actionbase.core.state.AbstractSchema
import com.kakao.actionbase.core.types.PrimitiveType

object EdgeMutationBuilder {
    const val MULTI_EDGE_ID_FIELD_NAME = "id"
    const val MULTI_EDGE_SOURCE_FIELD_NAME = "_source"
    const val MULTI_EDGE_TARGET_FIELD_NAME = "_target"

    val MULTI_EDGE_ID_CODE = XXHash32Wrapper.default.stringHash(MULTI_EDGE_ID_FIELD_NAME)
    val MULTI_EDGE_SOURCE_CODE = XXHash32Wrapper.default.stringHash(MULTI_EDGE_SOURCE_FIELD_NAME)
    val MULTI_EDGE_TARGET_CODE = XXHash32Wrapper.default.stringHash(MULTI_EDGE_TARGET_FIELD_NAME)

    fun build(
        before: EdgeStateRecord,
        after: EdgeStateRecord,
        directionType: DirectionType,
        indexes: List<Index>,
        groups: List<Group>,
    ): EdgeMutationRecords {
        val beforeActive = before.value.active
        val afterActive = after.value.active

        var createIndexRecords: List<EdgeIndexRecord> = emptyList()
        var deleteIndexRecordKeys: List<EdgeIndexRecord.Key> = emptyList()
        var countRecords: List<EdgeCountRecord> = emptyList()
        var groupRecords: List<EdgeGroupRecord> = emptyList()

        val (status, acc) =
            when {
                before == after -> {
                    "IDLE" to 0L
                }

                !beforeActive && afterActive -> {
                    createIndexRecords = buildIndexRecords(after, directionType, indexes)
                    countRecords = buildCountRecords(after, directionType, 1L)
                    groupRecords = buildGroupRecords(after, groups, 1L)
                    "CREATED" to 1L
                }

                beforeActive && !afterActive -> {
                    deleteIndexRecordKeys = buildIndexRecords(before, directionType, indexes).map { it.key }
                    countRecords = buildCountRecords(after, directionType, -1L)
                    groupRecords = buildGroupRecords(before, groups, -1L)
                    "DELETED" to -1L
                }

                beforeActive && afterActive -> {
                    createIndexRecords = buildIndexRecords(after, directionType, indexes)

                    val willBeUpdated = createIndexRecords.map { it.key }.toSet()

                    deleteIndexRecordKeys =
                        buildIndexRecords(before, directionType, indexes)
                            .map { it.key }
                            .filter { it !in willBeUpdated }

                    val decrementGroupRecords = buildGroupRecords(before, groups, -1L)
                    val incrementGroupRecords = buildGroupRecords(after, groups, 1L)
                    groupRecords = decrementGroupRecords + incrementGroupRecords

                    "UPDATED" to 0L
                }

                else -> {
                    "IDLE" to 0L
                }
            }

        return EdgeMutationRecords(
            status = status,
            acc = acc,
            stateRecord = after,
            createIndexRecords = createIndexRecords,
            deleteIndexRecordKeys = deleteIndexRecordKeys,
            countRecords = countRecords,
            groupRecords = groupRecords,
        )
    }

    fun buildForMultiEdge(
        before: EdgeStateRecord,
        after: EdgeStateRecord,
        directionType: DirectionType,
        indexes: List<Index>,
        groups: List<Group>,
    ): EdgeMutationRecords {
        val beforeActive = before.value.active
        val afterActive = after.value.active

        var createIndexRecords: List<EdgeIndexRecord> = emptyList()
        var deleteIndexRecordKeys: List<EdgeIndexRecord.Key> = emptyList()
        var countRecords: List<EdgeCountRecord> = emptyList()
        var groupRecords: List<EdgeGroupRecord> = emptyList()

        val (status, acc) =
            when {
                before == after -> {
                    "IDLE" to 0L
                }

                !beforeActive && afterActive -> {
                    createIndexRecords = buildIndexRecordsForMultiEdge(after, directionType, indexes)
                    countRecords = buildCountRecordsForMultiEdge(after, directionType, 1L)
                    groupRecords = buildGroupRecordsForMultiEdge(after, groups, 1L)
                    "CREATED" to 1L
                }

                beforeActive && !afterActive -> {
                    deleteIndexRecordKeys = buildIndexRecordsForMultiEdge(before, directionType, indexes).map { it.key }
                    countRecords = buildCountRecordsForMultiEdge(before, directionType, -1L)
                    groupRecords = buildGroupRecordsForMultiEdge(before, groups, -1L)
                    "DELETED" to -1L
                }

                beforeActive && afterActive -> {
                    createIndexRecords = buildIndexRecordsForMultiEdge(after, directionType, indexes)

                    val willBeUpdated = createIndexRecords.map { it.key }.toSet()

                    deleteIndexRecordKeys =
                        buildIndexRecordsForMultiEdge(before, directionType, indexes)
                            .map { it.key }
                            .filter { it !in willBeUpdated }

                    // If the source or target is changed, update the count records.
                    val sourceOrTargetChanged =
                        before.sourceForKeyEdgeTable() != after.sourceForKeyEdgeTable() ||
                            before.targetForKeyEdgeTable() != after.targetForKeyEdgeTable()
                    if (sourceOrTargetChanged) {
                        countRecords =
                            buildCountRecordsForMultiEdge(before, directionType, -1L) +
                            buildCountRecordsForMultiEdge(after, directionType, 1L)
                    }

                    val decrementGroupRecords = buildGroupRecordsForMultiEdge(before, groups, -1L)
                    val incrementGroupRecords = buildGroupRecordsForMultiEdge(after, groups, 1L)
                    groupRecords = decrementGroupRecords + incrementGroupRecords

                    "UPDATED" to 0L
                }

                else -> {
                    "IDLE" to 0L
                }
            }

        return EdgeMutationRecords(
            status = status,
            acc = acc,
            stateRecord = after,
            createIndexRecords = createIndexRecords,
            deleteIndexRecordKeys = deleteIndexRecordKeys,
            countRecords = countRecords,
            groupRecords = groupRecords,
        )
    }

    fun buildIndexRecords(
        record: EdgeStateRecord,
        directionType: DirectionType,
        indexes: List<Index>,
    ): List<EdgeIndexRecord> {
        val properties: Map<Int, Any?> = record.value.properties.mapValues { (_, stateValue) -> stateValue.value }

        val directions = directionType.directions()
        return indexes.flatMap { index ->
            directions.map { direction ->
                val indexValues =
                    index.fields.map { field ->
                        val value = record.indexValueOf(properties, field.field)
                        EdgeIndexRecord.Key.IndexValue(
                            value = value,
                            order = field.order,
                        )
                    }
                EdgeIndexRecord(
                    key =
                        EdgeIndexRecord.Key(
                            prefix =
                                EdgeIndexRecord.Key.Prefix.of(
                                    tableCode = record.key.tableCode,
                                    directedSource = record.directedSource(direction),
                                    direction = direction,
                                    indexCode = index.code,
                                    indexValues = indexValues,
                                ),
                            suffix =
                                EdgeIndexRecord.Key.Suffix(
                                    restIndexValues = emptyList(),
                                    directedTarget = record.directedTarget(direction),
                                ),
                        ),
                    value =
                        EdgeIndexRecord.Value(
                            version = record.value.version,
                            properties = properties,
                        ),
                )
            }
        }
    }

    fun buildIndexRecordsForMultiEdge(
        record: EdgeStateRecord,
        directionType: DirectionType,
        indexes: List<Index>,
    ): List<EdgeIndexRecord> {
        val id = record.key.source
        val properties: Map<Int, Any?> = record.value.properties.mapValues { (_, stateValue) -> stateValue.value }

        val directions = directionType.directions()
        return indexes.flatMap { index ->
            directions.map { direction ->
                val directedSource = record.directedSourceForMultiEdge(direction)

                val indexValues =
                    index.fields.map { field ->
                        val value = record.indexValueOf(properties, field.field)
                        EdgeIndexRecord.Key.IndexValue(
                            value = value,
                            order = field.order,
                        )
                    }
                EdgeIndexRecord(
                    key =
                        EdgeIndexRecord.Key(
                            prefix =
                                EdgeIndexRecord.Key.Prefix.of(
                                    tableCode = record.key.tableCode,
                                    directedSource = directedSource,
                                    direction = direction,
                                    indexCode = index.code,
                                    indexValues = indexValues,
                                ),
                            suffix =
                                EdgeIndexRecord.Key.Suffix(
                                    restIndexValues = emptyList(),
                                    directedTarget = id,
                                ),
                        ),
                    value =
                        EdgeIndexRecord.Value(
                            version = record.value.version,
                            properties = properties,
                        ),
                )
            }
        }
    }

    fun buildCountRecords(
        record: EdgeStateRecord,
        directionType: DirectionType,
        accumulator: Long,
    ): List<EdgeCountRecord> {
        val directions = directionType.directions()
        return directions.map { direction ->

            EdgeCountRecord(
                key =
                    EdgeCountRecord.Key.of(
                        directedSource = record.directedSource(direction),
                        tableCode = record.key.tableCode,
                        direction = direction,
                    ),
                value = accumulator,
            )
        }
    }

    fun buildGroupRecords(
        record: EdgeStateRecord,
        groups: List<Group>,
        weight: Long,
    ): List<EdgeGroupRecord> {
        val properties: Map<Int, Any?> = record.value.properties.mapValues { (_, stateValue) -> stateValue.value }

        return groups.flatMap { group ->
            group.directionType.directions().map { direction ->
                val value =
                    when (group.type) {
                        GroupType.COUNT -> 1L
                        GroupType.SUM -> {
                            val anyValue = record.indexValueOf(properties, group.valueField)
                            requireNotNull(anyValue) { "The value field ${group.valueField} is not found." }
                            PrimitiveType.LONG.cast(anyValue) as Long
                        }
                    }
                val groupValues =
                    group.fields.map { field ->
                        val value = record.indexValueOf(properties, field.name)
                        if (field.bucket != null) {
                            field.bucket.apply(value)
                        } else {
                            value
                        }
                    }
                EdgeGroupRecord(
                    key =
                        EdgeGroupRecord.Key.of(
                            directedSource = record.directedSource(direction),
                            tableCode = record.key.tableCode,
                            direction = direction,
                            groupCode = group.code,
                        ),
                    qualifier = EdgeGroupRecord.Qualifier(groupValues),
                    value = weight * value,
                )
            }
        }
    }

    fun buildGroupRecordsForMultiEdge(
        record: EdgeStateRecord,
        groups: List<Group>,
        weight: Long,
    ): List<EdgeGroupRecord> {
        val properties: Map<Int, Any?> = record.value.properties.mapValues { (_, stateValue) -> stateValue.value }

        return groups.flatMap { group ->
            group.directionType.directions().map { direction ->
                val value =
                    when (group.type) {
                        GroupType.COUNT -> 1L
                        GroupType.SUM -> {
                            val anyValue = record.indexValueOf(properties, group.valueField)
                            requireNotNull(anyValue) { "The value field ${group.valueField} is not found." }
                            PrimitiveType.LONG.cast(anyValue) as Long
                        }
                    }
                val groupValues =
                    group.fields.map { field ->
                        val value = record.indexValueOf(properties, field.name)
                        if (field.bucket != null) {
                            field.bucket.apply(value)
                        } else {
                            value
                        }
                    }
                EdgeGroupRecord(
                    key =
                        EdgeGroupRecord.Key.of(
                            directedSource = record.directedSourceForMultiEdge(direction),
                            tableCode = record.key.tableCode,
                            direction = direction,
                            groupCode = group.code,
                        ),
                    qualifier = EdgeGroupRecord.Qualifier(groupValues),
                    value = weight * value,
                )
            }
        }
    }

    fun buildCountRecordsForMultiEdge(
        record: EdgeStateRecord,
        directionType: DirectionType,
        accumulator: Long,
    ): List<EdgeCountRecord> {
        val directions = directionType.directions()
        return directions.map { direction ->
            val directedSource = record.directedSourceForMultiEdge(direction)

            EdgeCountRecord(
                key =
                    EdgeCountRecord.Key.of(
                        directedSource = directedSource,
                        tableCode = record.key.tableCode,
                        direction = direction,
                    ),
                value = accumulator,
            )
        }
    }

    fun EdgeStateRecord.indexValueOf(
        properties: Map<Int, Any?>,
        name: String,
    ): Any? {
        val systemProperty = SystemProperties.getOrNull(name)
        return when (systemProperty) {
            SystemProperties.VERSION -> value.version
            SystemProperties.SOURCE -> key.source
            SystemProperties.TARGET -> key.target
            else -> {
                properties[AbstractSchema.codeOf(name)]
            }
        }
    }

    fun EdgeStateRecord.directedSource(direction: Direction): Any =
        when (direction) {
            Direction.OUT -> key.source
            Direction.IN -> key.target
        }

    fun EdgeStateRecord.directedTarget(direction: Direction): Any =
        when (direction) {
            Direction.OUT -> key.target
            Direction.IN -> key.source
        }

    fun EdgeStateRecord.sourceForKeyEdgeTable(): Any = directedSourceForMultiEdge(Direction.OUT)

    fun EdgeStateRecord.targetForKeyEdgeTable(): Any = directedSourceForMultiEdge(Direction.IN)

    /**
     * The source and target are non-nullable.
     */
    fun EdgeStateRecord.directedSourceForMultiEdge(direction: Direction): Any =
        when (direction) {
            Direction.OUT -> value.properties[MULTI_EDGE_SOURCE_CODE]?.value!!
            Direction.IN -> value.properties[MULTI_EDGE_TARGET_CODE]?.value!!
        }
}
