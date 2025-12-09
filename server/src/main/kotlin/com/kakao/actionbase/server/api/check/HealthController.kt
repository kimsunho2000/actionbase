package com.kakao.actionbase.server.api.check

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RestController

import reactor.core.publisher.Mono

@RestController
class HealthController {
    private val readinessNotReady = Mono.just(ResponseEntity.status(503).body("DOWN"))

    private val readinessReady = Mono.just(ResponseEntity.ok("UP"))

    private var readinessStatus = readinessNotReady

    private val livenessStatus = Mono.just(ResponseEntity.ok("UP"))

    @GetMapping("/graph/health")
    fun health(): Mono<ResponseEntity<String>> = readinessStatus

    @GetMapping("/graph/health/readiness")
    fun readiness(): Mono<ResponseEntity<String>> = readinessStatus

    @PutMapping("/graph/health/readiness")
    fun readinessUp(): Mono<ResponseEntity<String>> {
        readinessStatus = readinessReady
        return readinessStatus
    }

    @GetMapping("/graph/health/liveness")
    fun liveness(): Mono<ResponseEntity<String>> = livenessStatus
}
