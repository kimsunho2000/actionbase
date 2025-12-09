package com.kakao.actionbase.v2.engine

object GraphGlobalValues {
    val phase: String =
        System.getProperty("graph.phase")
            ?: System.getenv("GRAPH_PHASE")
            ?: System.getProperty("spring.profiles.active")
            ?: System.getenv("SPRING_PROFILES_ACTIVE")
            ?: "local"

    val tenant: String = phase
}
