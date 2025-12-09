package com.kakao.actionbase.v2.engine.migration.tasks

import com.kakao.actionbase.v2.engine.Graph

import reactor.core.publisher.Mono

interface MigrationTask {
    fun migrate(graph: Graph): Mono<List<String>>
}
