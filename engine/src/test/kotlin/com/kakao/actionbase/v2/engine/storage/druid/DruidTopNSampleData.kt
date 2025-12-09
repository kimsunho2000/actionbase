package com.kakao.actionbase.v2.engine.storage.druid

import kotlin.random.Random

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

fun generateSampleData(
    from: Long,
    until: Long,
    size: Int,
): List<SampleData> {
    val phases = listOf("sandbox")
    val services = listOf("gift")
    val labels = listOf("like_product_v1_test")

    return List(size) {
        SampleData(
            ts = System.currentTimeMillis() + it,
            phase = phases.random(),
            service = services.random(),
            label = labels.random(),
            dimension = Random.nextLong(from, until).toString(),
            long = Random.nextLong(1, 10),
            double = Random.nextDouble(),
        )
    }
}

fun main() {
    val objectMapper = jacksonObjectMapper()
    generateSampleData(100, 110, 100).map(objectMapper::writeValueAsString).forEach(::println)
    generateSampleData(110, 200, 100).map(objectMapper::writeValueAsString).forEach(::println)
}

data class SampleData(
    val ts: Long,
    val phase: String,
    val service: String,
    val label: String,
    val dimension: String,
    val long: Long,
    val double: Double,
)
