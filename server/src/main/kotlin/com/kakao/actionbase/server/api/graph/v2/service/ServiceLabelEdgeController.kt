package com.kakao.actionbase.server.api.graph.v2.service

import com.kakao.actionbase.server.configuration.Defaults
import com.kakao.actionbase.server.payload.EdgesRequest
import com.kakao.actionbase.server.util.mapToResponseEntity
import com.kakao.actionbase.v2.core.metadata.Direction
import com.kakao.actionbase.v2.core.metadata.MutationMode
import com.kakao.actionbase.v2.engine.Graph
import com.kakao.actionbase.v2.engine.edge.MutationResult
import com.kakao.actionbase.v2.engine.entity.EntityName
import com.kakao.actionbase.v2.engine.label.DeleteEdgeRequest
import com.kakao.actionbase.v2.engine.label.InsertEdgeRequest
import com.kakao.actionbase.v2.engine.sql.QueryResult
import com.kakao.actionbase.v2.engine.sql.ScanFilter
import com.kakao.actionbase.v2.engine.sql.StatKey
import com.kakao.actionbase.v2.engine.sql.WherePredicate
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
class ServiceLabelEdgeController(
    val graph: Graph,
) {
    @PostMapping("/graph/v2/service/{service}/label/{label}/edge")
    fun insert(
        @PathVariable service: String,
        @PathVariable label: String,
        @RequestBody edges: EdgesRequest,
        @RequestParam(required = false) bulk: Boolean = false,
        @RequestParam(required = false) mode: MutationMode?,
        @RequestHeader(
            Defaults.REQUEST_ID_HEADER,
            required = false,
            defaultValue = Defaults.DEFAULT_REQUEST_ID,
        ) requestId: String,
    ): Mono<ResponseEntity<MutationResult>> {
        val request = InsertEdgeRequest("$service.$label", edges.edges, edges.audit, requestId)
        return graph.upsert(request, bulk, mode).mapToResponseEntity()
    }

    @PutMapping("/graph/v2/service/{service}/label/{label}/edge")
    fun update(
        @PathVariable service: String,
        @PathVariable label: String,
        @RequestBody edges: EdgesRequest,
        @RequestParam(required = false) bulk: Boolean = false,
        @RequestParam(required = false) mode: MutationMode?,
        @RequestHeader(
            Defaults.REQUEST_ID_HEADER,
            required = false,
            defaultValue = Defaults.DEFAULT_REQUEST_ID,
        ) requestId: String,
    ): Mono<ResponseEntity<MutationResult>> {
        val request = InsertEdgeRequest("$service.$label", edges.edges, edges.audit, requestId)
        return graph.update(request, bulk, mode).mapToResponseEntity()
    }

    @DeleteMapping("/graph/v2/service/{service}/label/{label}/edge")
    fun delete(
        @PathVariable service: String,
        @PathVariable label: String,
        @RequestBody edges: EdgesRequest,
        @RequestParam(required = false) bulk: Boolean = false,
        @RequestParam(required = false) mode: MutationMode?,
        @RequestHeader(
            Defaults.REQUEST_ID_HEADER,
            required = false,
            defaultValue = Defaults.DEFAULT_REQUEST_ID,
        ) requestId: String,
    ): Mono<ResponseEntity<MutationResult>> {
        val request = DeleteEdgeRequest("$service.$label", edges.edges, edges.audit, requestId)
        return graph.delete(request, bulk, mode).mapToResponseEntity()
    }

    @GetMapping("/graph/v2/service/{service}/label/{label}/edge")
    fun getAll(
        @PathVariable service: String,
        @PathVariable label: String,
        @RequestParam(required = false) self: String? = null,
        @RequestParam(required = false) src: String? = null,
        @RequestParam(required = false) tgt: String? = null,
        @RequestParam(required = false) select: List<String>? = null,
        @RequestParam(required = false) index: String? = null,
        @RequestParam(required = false) dir: String? = null,
        @RequestParam(required = false) limit: Int? = null,
        @RequestParam(required = false) offset: String? = null,
        @RequestParam(required = false) filter: String? = null,
        @RequestParam(required = false) stats: Set<StatKey>? = null,
        @RequestParam(required = false) format: String? = null,
    ): Mono<ResponseEntity<QueryResult.OutputFormat>> {
        val name = EntityName(service, label)
        val filters = filter?.let { WherePredicate.parse(it) }?.toSet() ?: emptySet()
        val srcOrSelf: String? = self ?: src

        require(srcOrSelf != null) { "src is required" }

        val scanFilter =
            ScanFilter(
                name = name,
                srcSet = srcOrSelf.split(",").toSet(),
                tgt = tgt?.split(",")?.toSet(),
                selectFields = select ?: ScanFilter.defaultSelectFields,
                dir = dir?.let { Direction.of(dir) } ?: ScanFilter.defaultDir,
                limit = limit ?: ScanFilter.defaultLimit,
                offset = offset,
                indexName = index,
                otherPredicates = filters,
                selfEdge = self != null,
            )
        val df = graph.singleStepQuery(scanFilter, stats ?: emptySet())
        return df.toOutputFormat(format).mapToResponseEntity()
    }
}
