package com.kakao.actionbase.server.test

import com.kakao.actionbase.server.ServerApplication

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient

@ActiveProfiles("test")
@AutoConfigureWebTestClient
@ExtendWith(E2ETestExtension::class)
@SpringBootTest(classes = [ServerApplication::class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class E2ETestBase {
    @Autowired
    protected lateinit var client: WebTestClient

    @BeforeEach
    fun initialize() {}
}
