package com.kakao.actionbase.engine.util

import java.time.Duration

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

/**
 * Ensures execution to completion once subscribed.
 * Even if the subscription is cancelled due to client disconnection, timeout, etc., already started work continues.
 */
fun <T> Mono<T>.runEvenIfCancelled(): Mono<T> = this.cache(Duration.ZERO)

fun <T> Flux<T>.runEvenIfCancelled(): Flux<T> = this.cache(Duration.ZERO)
