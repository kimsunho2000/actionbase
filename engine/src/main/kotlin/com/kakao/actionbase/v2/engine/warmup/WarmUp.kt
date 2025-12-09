package com.kakao.actionbase.v2.engine.warmup

import com.kakao.actionbase.v2.engine.Graph
import com.kakao.actionbase.v2.engine.label.Label

import reactor.core.publisher.Mono

interface WarmUp {
    fun bootstrap(graph: Graph): Mono<Void>

    fun label(
        graph: Graph,
        newLabel: Label,
    ): Mono<Void>

    companion object {
        val empty: WarmUp = EmptyWarmUp
    }
}

private object EmptyWarmUp : WarmUp {
    override fun bootstrap(graph: Graph): Mono<Void> = Mono.empty()

    override fun label(
        graph: Graph,
        newLabel: Label,
    ): Mono<Void> = Mono.empty()
}
