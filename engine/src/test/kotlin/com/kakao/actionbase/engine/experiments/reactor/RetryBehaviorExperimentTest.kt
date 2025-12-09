package com.kakao.actionbase.engine.experiments.reactor

import java.time.Duration
import java.util.concurrent.atomic.AtomicInteger

import org.junit.jupiter.api.Test

import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import reactor.util.retry.Retry

class RetryBehaviorExperimentTest {
    @Test
    fun `retry executes function once but Mono chain multiple times`() {
        // given
        val functionCallCount = AtomicInteger(0)
        val monoExecutionCount = AtomicInteger(0)
        val successAfterAttempts = 3

        // when & then
        StepVerifier
            .create(testRetryBehavior(functionCallCount, monoExecutionCount, successAfterAttempts))
            .expectNext(1 to successAfterAttempts)
            .verifyComplete()
    }

    private fun testRetryBehavior(
        functionCallCount: AtomicInteger,
        monoExecutionCount: AtomicInteger,
        successAfterAttempts: Int,
    ): Mono<Pair<Int, Int>> {
        val functionCalls = functionCallCount.incrementAndGet()

        return Mono
            .fromSupplier {
                val monoCalls = monoExecutionCount.incrementAndGet()
                if (monoCalls < successAfterAttempts) {
                    throw RuntimeException("Simulated failure")
                }
                functionCalls to monoCalls
            }.retryWhen(Retry.fixedDelay(successAfterAttempts - 1L, Duration.ofMillis(1)))
    }
}
