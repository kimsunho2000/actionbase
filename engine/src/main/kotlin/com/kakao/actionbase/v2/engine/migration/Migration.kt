package com.kakao.actionbase.v2.engine.migration

import com.kakao.actionbase.v2.engine.Graph
import com.kakao.actionbase.v2.engine.migration.tasks.MigrationTask
import com.kakao.actionbase.v2.engine.migration.tasks.MigrationTask20240416A

import reactor.core.publisher.Mono

object Migration {
    private val tasks: Map<String, MigrationTask> =
        mapOf(
            "20240416A" to MigrationTask20240416A,
        )

    fun migrate(
        graph: Graph,
        name: String,
    ): Mono<List<String>> = tasks[name]?.migrate(graph) ?: Mono.error(IllegalArgumentException("Migration task not found: $name"))
}
