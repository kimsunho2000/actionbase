package com.kakao.actionbase.engine.util

import java.time.Duration
import java.util.concurrent.atomic.AtomicBoolean

import kotlin.test.assertFalse
import kotlin.test.assertTrue

import org.junit.jupiter.api.Test

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

class ReactorExtensionsTest {
    @Test
    fun `Mono runEvenIfCancelled should continue execution even when cancelled`() {
        val completed = AtomicBoolean(false)
        val mono =
            Mono
                .delay(Duration.ofMillis(100))
                .doOnNext { completed.set(true) }
                .runEvenIfCancelled()

        mono.subscribe().dispose()
        Thread.sleep(200) // Wait for delay completion

        assertTrue(completed.get(), "Should complete execution even after cancellation")
    }

    @Test
    fun `Flux runEvenIfCancelled should continue execution even when cancelled`() {
        val completed = AtomicBoolean(false)
        val flux =
            Flux
                .interval(Duration.ofMillis(50))
                .take(3)
                .doOnComplete { completed.set(true) }
                .runEvenIfCancelled()

        flux.subscribe().dispose()
        Thread.sleep(300) // Wait for interval completion

        assertTrue(completed.get(), "Should complete execution even after cancellation")
    }

    @Test
    fun `Mono without runEvenIfCancelled should be cancelled`() {
        val completed = AtomicBoolean(false)
        val mono =
            Mono
                .delay(Duration.ofMillis(100))
                .doOnNext { completed.set(true) }

        mono.subscribe().dispose()
        Thread.sleep(200)

        assertFalse(completed.get(), "Should be cancelled without runEvenIfCancelled")
    }

    @Test
    fun `Flux without runEvenIfCancelled should be cancelled`() {
        val completed = AtomicBoolean(false)
        val flux =
            Flux
                .interval(Duration.ofMillis(50))
                .take(3)
                .doOnComplete { completed.set(true) }

        flux.subscribe().dispose()
        Thread.sleep(300)

        assertFalse(completed.get(), "Should be cancelled without runEvenIfCancelled")
    }
}
