package com.kakao.actionbase.v2.engine.indexed

import com.kakao.actionbase.v2.core.code.Index
import com.kakao.actionbase.v2.core.code.hbase.Order
import com.kakao.actionbase.v2.core.metadata.Direction
import com.kakao.actionbase.v2.core.metadata.DirectionType
import com.kakao.actionbase.v2.core.metadata.LabelType
import com.kakao.actionbase.v2.core.types.DataType
import com.kakao.actionbase.v2.core.types.EdgeSchema
import com.kakao.actionbase.v2.core.types.Field
import com.kakao.actionbase.v2.core.types.VertexField
import com.kakao.actionbase.v2.core.types.VertexType
import com.kakao.actionbase.v2.engine.Graph
import com.kakao.actionbase.v2.engine.entity.EntityName
import com.kakao.actionbase.v2.engine.service.ddl.LabelCreateRequest
import com.kakao.actionbase.v2.engine.sql.WherePredicate
import com.kakao.actionbase.v2.engine.sql.show
import com.kakao.actionbase.v2.engine.test.GraphFixtures
import com.kakao.actionbase.v2.engine.test.dsl.dml

import io.kotest.core.spec.style.StringSpec

class IndexedSpec :
    StringSpec({

        lateinit var graph: Graph

        beforeTest {
            graph = GraphFixtures.create()

            graph.labelDdl
                .create(
                    EntityName("test", "various_index"),
                    LabelCreateRequest(
                        desc = "test various index",
                        type = LabelType.INDEXED,
                        schema =
                            EdgeSchema(
                                VertexField(VertexType.STRING),
                                VertexField(VertexType.STRING),
                                listOf(
                                    Field("permission", DataType.STRING, false),
                                ),
                            ),
                        dirType = DirectionType.BOTH,
                        storage = GraphFixtures.hbaseStorage,
                        indices =
                            listOf(
                                Index(
                                    "ts_desc",
                                    listOf(
                                        Index.Field("ts", Order.DESC),
                                    ),
                                ),
                                Index(
                                    "permission_ts_desc",
                                    listOf(
                                        Index.Field("permission", Order.ASC),
                                        Index.Field("ts", Order.DESC),
                                    ),
                                ),
                            ),
                    ),
                ).block()
        }

        afterTest {
            graph.close()
        }

        "scan indexed label with specific field value" {
            graph.dml(EntityName("test", "various_index")) {
                insert("user1", "item1") {
                    ts = 1L
                    property(listOf("permission"), "me")
                }.block()
                insert("user1", "item2") {
                    ts = 2L
                    property(listOf("permission"), "me")
                }.block()
                insert("user1", "item3") {
                    ts = 3L
                    property(listOf("permission"), "others")
                }.block()
                insert("user1", "item4") {
                    ts = 4L
                    property(listOf("permission"), "others")
                }.block()
                insert("user1", "item5") {
                    ts = 5L
                    property(listOf("permission"), "others")
                }.block()
            }
            graph.queryScan(EntityName("test", "various_index"), "user1", Direction.OUT, "ts_desc").show()
            graph.queryScan(EntityName("test", "various_index"), "user1", Direction.OUT, "permission_ts_desc").show()
            graph
                .queryScan(
                    EntityName("test", "various_index"),
                    "user1",
                    Direction.OUT,
                    "permission_ts_desc",
                    otherPredicates =
                        setOf(
                            WherePredicate.Eq("permission", "others"),
                        ),
                ).show()
            graph
                .queryScan(
                    EntityName("test", "various_index"),
                    "user1",
                    Direction.OUT,
                    "permission_ts_desc",
                    otherPredicates =
                        setOf(
                            WherePredicate.Eq("permission", "others"),
                            WherePredicate.Lt("ts", 5L),
                        ),
                    limit = 1,
                ).show()
            graph
                .queryScan(
                    EntityName("test", "various_index"),
                    "user1",
                    Direction.OUT,
                    "permission_ts_desc",
                    otherPredicates =
                        setOf(
                            WherePredicate.Eq("permission", "others"),
                            WherePredicate.Between("ts", 2L, 3L),
                        ),
                    limit = 1,
                ).show()
        }
    })
