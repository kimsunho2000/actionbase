package com.kakao.actionbase.server

import com.kakao.actionbase.server.test.E2ETestBase

import kotlin.test.Test

class StartUpTest : E2ETestBase() {
    @Test
    fun checkV2() {
        client
            .get()
            .uri("/graph/v2")
            .exchange()
            .expectStatus()
            .isOk
    }

    @Test
    fun checkV3() {
        client
            .get()
            .uri("/graph/v3")
            .exchange()
            .expectStatus()
            .isOk
    }
}
