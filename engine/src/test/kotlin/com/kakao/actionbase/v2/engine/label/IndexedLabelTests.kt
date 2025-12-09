package com.kakao.actionbase.v2.engine.label

import com.kakao.actionbase.v2.core.code.Index
import com.kakao.actionbase.v2.core.code.hbase.Order
import com.kakao.actionbase.v2.core.edge.Edge
import com.kakao.actionbase.v2.core.metadata.DirectionType
import com.kakao.actionbase.v2.core.metadata.EdgeOperation
import com.kakao.actionbase.v2.core.metadata.LabelType
import com.kakao.actionbase.v2.core.types.DataType
import com.kakao.actionbase.v2.core.types.EdgeSchema
import com.kakao.actionbase.v2.core.types.Field
import com.kakao.actionbase.v2.core.types.VertexField
import com.kakao.actionbase.v2.core.types.VertexType
import com.kakao.actionbase.v2.engine.Graph
import com.kakao.actionbase.v2.engine.entity.EntityName
import com.kakao.actionbase.v2.engine.entity.LabelEntity
import com.kakao.actionbase.v2.engine.service.ddl.DdlStatus
import com.kakao.actionbase.v2.engine.sql.ScanFilter
import com.kakao.actionbase.v2.engine.sql.select
import com.kakao.actionbase.v2.engine.sql.show
import com.kakao.actionbase.v2.engine.sql.toRowFlux
import com.kakao.actionbase.v2.engine.test.GraphFixtures
import com.kakao.actionbase.v2.engine.test.toRequest

import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import reactor.kotlin.test.test

class IndexedLabelTests :
    StringSpec({

        lateinit var graph: Graph

        lateinit var labels: List<Label>

        val sampleEdges =
            listOf(
                Edge(3000L, "src1", "a", mapOf("s" to "string1", "l" to 100L, "i" to 100)),
                Edge(1000L, "src1", "a", mapOf("s" to "string1", "l" to 100L, "i" to 100)), // dup
                Edge(2000L, "src1", "b", mapOf("s" to "string2", "l" to 200L, "i" to 200)),
            )

        val schema =
            EdgeSchema(
                VertexField(VertexType.STRING),
                VertexField(VertexType.STRING),
                listOf(
                    Field("s", DataType.STRING, false),
                    Field("l", DataType.LONG, false),
                    Field("i", DataType.INT, false),
                ),
            )

        val indices =
            listOf(
                Index(
                    "ts_desc",
                    listOf(
                        Index.Field("ts", Order.DESC),
                    ),
                ),
                Index(
                    "ts_asc",
                    listOf(
                        Index.Field("ts", Order.ASC),
                    ),
                ),
                Index(
                    "s_asc",
                    listOf(
                        Index.Field("s", Order.ASC),
                    ),
                ),
                Index(
                    "s_desc",
                    listOf(
                        Index.Field("s", Order.DESC),
                    ),
                ),
                Index(
                    "l_asc",
                    listOf(
                        Index.Field("l", Order.ASC),
                    ),
                ),
                Index(
                    "l_desc",
                    listOf(
                        Index.Field("l", Order.DESC),
                    ),
                ),
                Index(
                    "i_asc",
                    listOf(
                        Index.Field("i", Order.ASC),
                    ),
                ),
                Index(
                    "i_desc",
                    listOf(
                        Index.Field("i", Order.DESC),
                    ),
                ),
            )

        beforeTest {
            graph = GraphFixtures.create()
            labels =
                listOf(GraphFixtures.hbaseStorage).map { storageName ->
                    val name = EntityName(GraphFixtures.serviceName, "${storageName}_various_index")
                    val entity =
                        LabelEntity(
                            active = true,
                            name = name,
                            desc = "$name label",
                            type = LabelType.INDEXED,
                            schema = schema,
                            dirType = DirectionType.BOTH,
                            storage = storageName,
                            indices = indices,
                        )

                    graph.labelDdl
                        .create(name, entity.toRequest())
                        .test()
                        .assertNext {
                            it.status shouldBe DdlStatus.Status.CREATED
                        }.verifyComplete()

                    val label = graph.getLabel(name)

                    graph
                        .mutate(label.name, label, sampleEdges.map { it.toTraceEdge() }, EdgeOperation.INSERT)
                        .test()
                        .expectNextCount(1)
                        .verifyComplete()

                    label
                }
        }

        afterTest {
            graph.close()
        }

        "check the order of the various indices" {
            labels.forEach { label ->
                val name = label.name
                val valid =
                    listOf(
                        ScanFilter(name, setOf("src1"), indexName = "ts_desc") to listOf("a", "b"),
                        ScanFilter(name, setOf("src1"), indexName = "ts_asc") to listOf("b", "a"),
                        ScanFilter(name, setOf("src1"), indexName = "s_asc") to listOf("a", "b"),
                        ScanFilter(name, setOf("src1"), indexName = "s_desc") to listOf("b", "a"),
                        ScanFilter(name, setOf("src1"), indexName = "l_asc") to listOf("a", "b"),
                        ScanFilter(name, setOf("src1"), indexName = "l_desc") to listOf("b", "a"),
                        ScanFilter(name, setOf("src1"), indexName = "i_asc") to listOf("a", "b"),
                        ScanFilter(name, setOf("src1"), indexName = "i_desc") to listOf("b", "a"),
                    )
                valid.forEach { (query, expected) ->
                    graph
                        .singleStepQuery(query)
                        .select("tgt")
                        .toRowFlux()
                        .map { it.get(0) }
                        .filter { it in setOf("a", "b") } // filter for data corruption
                        .collectList()
                        .test()
                        .assertNext {
                            it.size shouldBe expected.size
                            it shouldBe expected
                        }.verifyComplete()
                }

                val noSuchIndex =
                    listOf(
                        ScanFilter(name, setOf("src1")),
                        ScanFilter(name, setOf("src1"), indexName = "no_such_index_1"),
                        ScanFilter(name, setOf("src1"), indexName = "no_such_index_2"),
                    )

                noSuchIndex.forEach { query ->
                    shouldThrowAny {
                        graph.singleStepQuery(query).show()
                    }
                }
            }
        }
    })
