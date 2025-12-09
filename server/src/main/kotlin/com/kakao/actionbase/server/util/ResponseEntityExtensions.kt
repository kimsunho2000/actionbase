package com.kakao.actionbase.server.util

import org.springframework.http.ResponseEntity

import reactor.core.publisher.Mono

fun <T> Mono<T>.mapToResponseEntity(): Mono<ResponseEntity<T>> = this.map { ResponseEntity.ok(it) }.defaultIfEmpty(ResponseEntity.notFound().build())
