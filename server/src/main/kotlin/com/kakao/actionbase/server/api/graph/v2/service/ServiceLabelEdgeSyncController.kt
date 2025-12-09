package com.kakao.actionbase.server.api.graph.v2.service

import com.kakao.actionbase.server.configuration.Defaults
import com.kakao.actionbase.server.payload.EdgesRequest
import com.kakao.actionbase.server.util.mapToResponseEntity
import com.kakao.actionbase.v2.core.metadata.MutationMode
import com.kakao.actionbase.v2.engine.Graph
import com.kakao.actionbase.v2.engine.edge.MutationResult
import com.kakao.actionbase.v2.engine.label.DeleteEdgeRequest
import com.kakao.actionbase.v2.engine.label.InsertEdgeRequest

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

import reactor.core.publisher.Mono

@RestController
class ServiceLabelEdgeSyncController(
    val graph: Graph,
) {
    @PostMapping("/graph/v2/service/{service}/label/{label}/edge/sync")
    fun insert(
        @PathVariable service: String,
        @PathVariable label: String,
        @RequestBody edges: EdgesRequest,
        @RequestParam(required = false) bulk: Boolean = false,
        @RequestHeader(
            Defaults.REQUEST_ID_HEADER,
            required = false,
            defaultValue = Defaults.DEFAULT_REQUEST_ID,
        ) requestId: String,
    ): Mono<ResponseEntity<MutationResult>> {
        val request = InsertEdgeRequest("$service.$label", edges.edges, edges.audit, requestId)
        return graph.upsert(request, bulk, MutationMode.SYNC).mapToResponseEntity()
    }

    @PutMapping("/graph/v2/service/{service}/label/{label}/edge/sync")
    fun update(
        @PathVariable service: String,
        @PathVariable label: String,
        @RequestBody edges: EdgesRequest,
        @RequestParam(required = false) bulk: Boolean = false,
        @RequestHeader(
            Defaults.REQUEST_ID_HEADER,
            required = false,
            defaultValue = Defaults.DEFAULT_REQUEST_ID,
        ) requestId: String,
    ): Mono<ResponseEntity<MutationResult>> {
        val request = InsertEdgeRequest("$service.$label", edges.edges, edges.audit, requestId)
        return graph.update(request, bulk, MutationMode.SYNC).mapToResponseEntity()
    }

    @DeleteMapping("/graph/v2/service/{service}/label/{label}/edge/sync")
    fun delete(
        @PathVariable service: String,
        @PathVariable label: String,
        @RequestBody edges: EdgesRequest,
        @RequestParam(required = false) bulk: Boolean = false,
        @RequestHeader(
            Defaults.REQUEST_ID_HEADER,
            required = false,
            defaultValue = Defaults.DEFAULT_REQUEST_ID,
        ) requestId: String,
    ): Mono<ResponseEntity<MutationResult>> {
        val request = DeleteEdgeRequest("$service.$label", edges.edges, edges.audit, requestId)
        return graph.delete(request, bulk, MutationMode.SYNC).mapToResponseEntity()
    }
}
