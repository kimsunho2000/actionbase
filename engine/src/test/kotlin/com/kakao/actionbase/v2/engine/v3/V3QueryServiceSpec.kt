package com.kakao.actionbase.v2.engine.v3

import com.kakao.actionbase.core.edge.payload.EdgePayload
import com.kakao.actionbase.v2.core.metadata.Direction
import com.kakao.actionbase.v2.engine.Graph
import com.kakao.actionbase.v2.engine.test.GraphFixtures

import org.junit.jupiter.api.assertThrows

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import reactor.kotlin.test.test

class V3QueryServiceSpec :
    StringSpec({

        lateinit var graph: Graph
        lateinit var v3QueryService: V3QueryService

        beforeTest {
            graph = GraphFixtures.create()
            v3QueryService = V3QueryService(graph)
        }

        afterTest {
            graph.close()
        }

        "count" {
            val database = GraphFixtures.serviceName
            val table = GraphFixtures.hbaseIndexed
            val sampleEdge = GraphFixtures.sampleEdges.first()
            val expectedCount = GraphFixtures.sampleEdges.count { it.src == sampleEdge.src }.toLong()
            v3QueryService
                .count(database, table, sampleEdge.src, Direction.OUT)
                .test()
                .assertNext {
                    it.count shouldBe expectedCount
                }.verifyComplete()
        }

        "get" {
            val database = GraphFixtures.serviceName
            val table = GraphFixtures.hbaseIndexed
            val sampleEdge = GraphFixtures.sampleEdges.first()
            val expectedEdgePayload =
                EdgePayload(
                    version = sampleEdge.ts,
                    source = sampleEdge.src,
                    target = sampleEdge.tgt,
                    properties = (mapOf("receivedFrom" to null) + sampleEdge.props),
                    context = emptyMap(),
                )
            v3QueryService
                .gets(database, table, listOf(sampleEdge.src), listOf(sampleEdge.tgt))
                .test()
                .assertNext {
                    it.edges.map { edge -> edge.toStringValues() } shouldBe listOf(expectedEdgePayload.toStringValues())
                    it.count shouldBe 1
                    it.total shouldBe 1L
                }.verifyComplete()
        }

        "scan" {
            val database = GraphFixtures.serviceName
            val table = GraphFixtures.hbaseIndexed
            val index = GraphFixtures.index2 // created_at_desc
            val sampleEdge = GraphFixtures.sampleEdges.first()
            val expectedCount = GraphFixtures.sampleEdges.count { it.src == sampleEdge.src }
            val expectedEdges =
                GraphFixtures.sampleEdges
                    .filter { it.src == sampleEdge.src }
                    .map { EdgePayload(it.ts, it.src, it.tgt, mapOf("receivedFrom" to null) + it.props, emptyMap()) }
                    .sortedByDescending { it.properties["createdAt"].toString().toLong() }

            v3QueryService
                .scan(database, table, index, sampleEdge.src, Direction.OUT, limit = 10)
                .test()
                .assertNext {
                    it.edges.size shouldBe expectedCount
                    it.count shouldBe expectedCount
                    it.edges.map { edge -> edge.toStringValues() } shouldBe expectedEdges.map { edge -> edge.toStringValues() }
                    it.total shouldBe -1L // total is not provided in this case
                }.verifyComplete()
        }

        "scan with offset" {
            val firstStepLimit = 2
            val database = GraphFixtures.serviceName
            val table = GraphFixtures.hbaseIndexed
            val index = GraphFixtures.index2 // created_at_desc
            val sampleEdge = GraphFixtures.sampleEdges.first()

            val expectedCount = GraphFixtures.sampleEdges.count { it.src == sampleEdge.src } - firstStepLimit
            val expectedEdges =
                GraphFixtures.sampleEdges
                    .filter { it.src == sampleEdge.src }
                    .map { EdgePayload(it.ts, it.src, it.tgt, mapOf("receivedFrom" to null) + it.props, emptyMap()) }
                    .sortedByDescending { it.properties["createdAt"].toString().toLong() }
                    .drop(firstStepLimit)

            v3QueryService
                .scan(database, table, index, sampleEdge.src, Direction.OUT, limit = firstStepLimit)
                .flatMap {
                    val offset = it.offset
                    v3QueryService.scan(database, table, index, sampleEdge.src, Direction.OUT, offset = offset, limit = 10)
                }.test()
                .assertNext {
                    it.edges.size shouldBe expectedCount
                    it.count shouldBe expectedCount
                    it.edges.map { edge -> edge.toStringValues() } shouldBe expectedEdges.map { edge -> edge.toStringValues() }
                    it.total shouldBe -1L // total is not provided in this case
                }.verifyComplete()
        }

        "scan with features=total" {
            val database = GraphFixtures.serviceName
            val table = GraphFixtures.hbaseIndexed
            val index = GraphFixtures.index2 // created_at_desc
            val sampleEdge = GraphFixtures.sampleEdges.first()
            val expectedCount = GraphFixtures.sampleEdges.count { it.src == sampleEdge.src }
            val expectedEdges =
                GraphFixtures.sampleEdges
                    .filter { it.src == sampleEdge.src }
                    .map { EdgePayload(it.ts, it.src, it.tgt, mapOf("receivedFrom" to null) + it.props, emptyMap()) }
                    .sortedByDescending { it.properties["createdAt"].toString().toLong() }

            v3QueryService
                .scan(database, table, index, sampleEdge.src, Direction.OUT, limit = 10, features = listOf("total"))
                .test()
                .assertNext {
                    it.edges.size shouldBe expectedCount
                    it.count shouldBe expectedCount
                    it.edges.map { edge -> edge.toStringValues() } shouldBe expectedEdges.map { edge -> edge.toStringValues() }
                    it.total shouldBe expectedCount
                }.verifyComplete()
        }

        "scan with valid ranges" {
            val database = GraphFixtures.serviceName
            val table = GraphFixtures.hbaseIndexed
            val index = GraphFixtures.index1 // permission_created_at_desc
            val sampleEdge = GraphFixtures.sampleEdges.first()
            val permission = sampleEdge.props["permission"].toString()
            val createdAt = (sampleEdge.props["createdAt"] as Number).toLong()
            val expectedEdges =
                GraphFixtures.sampleEdges
                    .filter { it.src == sampleEdge.src && it.props["permission"] == permission && (it.props["createdAt"] as Number).toLong() > createdAt }
                    .map { EdgePayload(it.ts, it.src, it.tgt, mapOf("receivedFrom" to null) + it.props, emptyMap()) }
                    .sortedByDescending { it.properties["createdAt"].toString().toLong() }
            val expectedCount = expectedEdges.size

            // valid ranges: none, (permission), (permission, createdAt)
            v3QueryService
                .scan(database, table, index, sampleEdge.src, Direction.OUT, limit = 10, ranges = "permission:eq:na;createdAt:gt:10")
                .test()
                .assertNext {
                    it.edges.size shouldBe expectedCount
                    it.count shouldBe expectedCount
                    it.edges.map { edge -> edge.toStringValues() } shouldBe expectedEdges.map { edge -> edge.toStringValues() }
                    it.total shouldBe -1L // total is not provided in this case
                }.verifyComplete()
        }

        "scan with invalid ranges" {
            val database = GraphFixtures.serviceName
            val table = GraphFixtures.hbaseIndexed
            val index = GraphFixtures.index1 // permission_created_at_desc
            val sampleEdge = GraphFixtures.sampleEdges.first()
            val permission = sampleEdge.props["permission"].toString()
            val createdAt = (sampleEdge.props["createdAt"] as Number).toLong()
            val expectedEdges =
                GraphFixtures.sampleEdges
                    .filter { it.src == sampleEdge.src && it.props["permission"] == permission && (it.props["createdAt"] as Number).toLong() > createdAt }
                    .map { EdgePayload(it.ts, it.src, it.tgt, mapOf("receivedFrom" to null) + it.props, emptyMap()) }
                    .sortedByDescending { it.properties["createdAt"].toString().toLong() }
            expectedEdges.size

            // valid ranges: none, (permission), (permission, createdAt)
            assertThrows<IllegalArgumentException> {
                v3QueryService
                    .scan(database, table, index, sampleEdge.src, Direction.OUT, limit = 10, ranges = "createdAt:gt:10")
                    .subscribe()
            }
        }
    }) {
    companion object {
        fun EdgePayload.toStringValues(): EdgePayload =
            this.copy(
                version = this.version,
                source = this.source.toString(),
                target = this.target.toString(),
                properties = this.properties.mapValues { it.value?.toString() },
            )
    }
}
