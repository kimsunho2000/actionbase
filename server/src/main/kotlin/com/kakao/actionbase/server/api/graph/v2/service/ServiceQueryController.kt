package com.kakao.actionbase.server.api.graph.v2.service

import com.kakao.actionbase.server.util.mapToResponseEntity
import com.kakao.actionbase.v2.engine.Graph
import com.kakao.actionbase.v2.engine.entity.EntityName
import com.kakao.actionbase.v2.engine.entity.QueryEntity
import com.kakao.actionbase.v2.engine.service.ddl.DdlPage
import com.kakao.actionbase.v2.engine.service.ddl.DdlStatus
import com.kakao.actionbase.v2.engine.service.ddl.QueryCreateRequest
import com.kakao.actionbase.v2.engine.service.ddl.QueryUpdateRequest

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

import reactor.core.publisher.Mono

@RestController
class ServiceQueryController(
    val graph: Graph,
) {
    @GetMapping("/graph/v2/service/{service}/query/{query}")
    fun get(
        @PathVariable service: String,
        @PathVariable query: String,
    ): Mono<ResponseEntity<QueryEntity>> {
        val name = EntityName(service, query)
        return graph.queryDdl.getSingle(name).mapToResponseEntity()
    }

    @GetMapping("/graph/v2/service/{service}/query")
    fun getAll(
        @PathVariable service: String,
    ): Mono<ResponseEntity<DdlPage<QueryEntity>>> = graph.queryDdl.getAll(EntityName(service)).mapToResponseEntity()

    @PostMapping("/graph/v2/service/{service}/query/{query}")
    fun create(
        @PathVariable service: String,
        @PathVariable query: String,
        @RequestBody request: QueryCreateRequest,
    ): Mono<ResponseEntity<DdlStatus<QueryEntity>>> {
        val name = EntityName(service, query)
        return graph.queryDdl
            .create(name, request)
            .mapToResponseEntity()
    }

    @PutMapping("/graph/v2/service/{service}/query/{query}")
    fun update(
        @PathVariable service: String,
        @PathVariable query: String,
        @RequestBody request: QueryUpdateRequest,
    ): Mono<ResponseEntity<DdlStatus<QueryEntity>>> {
        val name = EntityName(service, query)
        return graph.queryDdl.update(name, request).mapToResponseEntity()
    }
}
