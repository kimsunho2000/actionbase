package com.kakao.actionbase.server.api.graph.v2.service

import com.kakao.actionbase.server.util.mapToResponseEntity
import com.kakao.actionbase.v2.engine.Graph
import com.kakao.actionbase.v2.engine.entity.EntityName

import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

import reactor.core.publisher.Mono

@RestController
@Validated
class ServiceLabelStatusController(
    val graph: Graph,
) {
    @GetMapping("/graph/v2/service/{service}/label/{label}/status")
    fun status(
        @PathVariable service: String,
        @PathVariable label: String,
    ): Mono<ResponseEntity<String>> {
        val name = EntityName(service, label)
        return graph.status(name).mapToResponseEntity()
    }
}
