package com.kakao.actionbase.server.api.graph.v2.edge

import com.kakao.actionbase.server.util.mapToResponseEntity
import com.kakao.actionbase.v2.core.metadata.MutationMode
import com.kakao.actionbase.v2.engine.Graph
import com.kakao.actionbase.v2.engine.edge.MutationResult
import com.kakao.actionbase.v2.engine.label.DeleteEdgeRequest
import com.kakao.actionbase.v2.engine.label.DeleteIdEdgeRequest
import com.kakao.actionbase.v2.engine.label.InsertEdgeRequest
import com.kakao.actionbase.v2.engine.label.InsertIdEdgeRequest

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

import reactor.core.publisher.Mono

@RestController
class EdgeController(
    val graph: Graph,
) {
    @PostMapping("/graph/v2/edge")
    fun insert(
        @RequestParam(required = false) bulk: Boolean = false,
        @RequestParam(required = false) mode: MutationMode?,
        @RequestBody request: InsertEdgeRequest,
    ): Mono<ResponseEntity<MutationResult>> = graph.upsert(request, bulk, mode).mapToResponseEntity()

    @PutMapping("/graph/v2/edge")
    fun update(
        @RequestParam(required = false) bulk: Boolean = false,
        @RequestParam(required = false) mode: MutationMode?,
        @RequestBody request: InsertEdgeRequest,
    ): Mono<ResponseEntity<MutationResult>> = graph.update(request, bulk, mode).mapToResponseEntity()

    @DeleteMapping("/graph/v2/edge")
    fun delete(
        @RequestParam(required = false) bulk: Boolean = false,
        @RequestParam(required = false) mode: MutationMode?,
        @RequestBody request: DeleteEdgeRequest,
    ): Mono<ResponseEntity<MutationResult>> = graph.delete(request, bulk, mode).mapToResponseEntity()

    @PostMapping("/graph/v2/edge/id")
    fun insertId(
        @RequestBody request: InsertIdEdgeRequest,
    ): Mono<ResponseEntity<MutationResult>> = graph.upsert(request).mapToResponseEntity()

    @PutMapping("/graph/v2/edge/id")
    fun updateId(
        @RequestBody request: InsertIdEdgeRequest,
    ): Mono<ResponseEntity<MutationResult>> = graph.update(request).mapToResponseEntity()

    @DeleteMapping("/graph/v2/edge/id")
    fun deleteId(
        @RequestBody request: DeleteIdEdgeRequest,
    ): Mono<ResponseEntity<MutationResult>> = graph.delete(request).mapToResponseEntity()
}
