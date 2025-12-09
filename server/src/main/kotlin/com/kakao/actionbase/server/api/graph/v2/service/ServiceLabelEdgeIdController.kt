package com.kakao.actionbase.server.api.graph.v2.service

import com.kakao.actionbase.server.configuration.Defaults
import com.kakao.actionbase.server.payload.EdgeValueRequest
import com.kakao.actionbase.server.util.mapToResponseEntity
import com.kakao.actionbase.v2.engine.Graph
import com.kakao.actionbase.v2.engine.edge.MutationResult
import com.kakao.actionbase.v2.engine.entity.EntityName
import com.kakao.actionbase.v2.engine.label.DeleteIdEdgeRequest
import com.kakao.actionbase.v2.engine.label.InsertIdEdgeRequest
import com.kakao.actionbase.v2.engine.sql.QueryResult
import com.kakao.actionbase.v2.engine.sql.ScanFilter
import com.kakao.actionbase.v2.engine.sql.StatKey
import com.kakao.actionbase.v2.engine.sql.toOutputFormat

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

import reactor.core.publisher.Mono

@RestController
class ServiceLabelEdgeIdController(
    val graph: Graph,
) {
    @PostMapping("/graph/v2/service/{service}/label/{label}/edge/id/{edgeId}")
    fun insert(
        @PathVariable service: String,
        @PathVariable label: String,
        @PathVariable edgeId: String,
        @RequestBody edgeValue: EdgeValueRequest,
        @RequestHeader(
            Defaults.REQUEST_ID_HEADER,
            required = false,
            defaultValue = Defaults.DEFAULT_REQUEST_ID,
        ) requestId: String,
    ): Mono<ResponseEntity<MutationResult>> {
        val request =
            InsertIdEdgeRequest(
                "$service.$label",
                edgeId,
                edgeValue.toEdgeValue(),
                edgeValue.audit,
                requestId,
            )
        return graph.upsert(request).mapToResponseEntity()
    }

    @PutMapping("/graph/v2/service/{service}/label/{label}/edge/id/{edgeId}")
    fun update(
        @PathVariable service: String,
        @PathVariable label: String,
        @PathVariable edgeId: String,
        @RequestBody edgeValue: EdgeValueRequest,
        @RequestHeader(
            Defaults.REQUEST_ID_HEADER,
            required = false,
            defaultValue = Defaults.DEFAULT_REQUEST_ID,
        ) requestId: String,
    ): Mono<ResponseEntity<MutationResult>> {
        val request =
            InsertIdEdgeRequest(
                "$service.$label",
                edgeId,
                edgeValue.toEdgeValue(),
                edgeValue.audit,
                requestId,
            )
        return graph.update(request).mapToResponseEntity()
    }

    @DeleteMapping("/graph/v2/service/{service}/label/{label}/edge/id/{edgeId}")
    fun delete(
        @PathVariable service: String,
        @PathVariable label: String,
        @PathVariable edgeId: String,
        @RequestBody edgeValue: EdgeValueRequest,
        @RequestHeader(
            Defaults.REQUEST_ID_HEADER,
            required = false,
            defaultValue = Defaults.DEFAULT_REQUEST_ID,
        ) requestId: String,
    ): Mono<ResponseEntity<MutationResult>> {
        val request =
            DeleteIdEdgeRequest(
                "$service.$label",
                edgeId,
                edgeValue.toEdgeValue(),
                edgeValue.audit,
                requestId,
            )
        return graph.delete(request).mapToResponseEntity()
    }

    @GetMapping("/graph/v2/service/{service}/label/{label}/edge/id/{edgeId}")
    fun get(
        @PathVariable service: String,
        @PathVariable label: String,
        @PathVariable edgeId: String,
        @RequestParam(required = false) stats: Set<StatKey>? = null,
        @RequestParam(required = false) format: String? = null,
    ): Mono<ResponseEntity<QueryResult.OutputFormat>> {
        val (src, tgt) = graph.getSrcAndTgt(edgeId)
        val scanFilter = ScanFilter(name = EntityName(service, label), srcSet = setOf(src), tgt = setOf(tgt))
        return graph
            .singleStepQuery(
                scanFilter,
                stats ?: setOf(StatKey.WITH_ALL),
            ).toOutputFormat(format)
            .mapToResponseEntity()
    }
}
