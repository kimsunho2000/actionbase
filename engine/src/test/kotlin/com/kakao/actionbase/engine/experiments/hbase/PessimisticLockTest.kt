package com.kakao.actionbase.engine.experiments.hbase

import com.kakao.actionbase.core.Constants
import com.kakao.actionbase.test.hbase.HBaseTestingClusterConfig
import com.kakao.actionbase.test.hbase.HBaseTestingClusterExtension

import java.time.Duration

import kotlin.test.Test

import org.apache.hadoop.hbase.client.AsyncTable
import org.apache.hadoop.hbase.client.CheckAndMutate
import org.apache.hadoop.hbase.client.Delete
import org.apache.hadoop.hbase.client.Put
import org.junit.jupiter.api.extension.ExtendWith

import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import reactor.util.retry.Retry

@ExtendWith(HBaseTestingClusterExtension::class)
class PessimisticLockTest(
    private val table: AsyncTable<*>,
    config: HBaseTestingClusterConfig,
) {
    val f = config.columnFamily
    val q = Constants.DEFAULT_QUALIFIER

    @Test
    fun distributedLockTest() {
        val lockKey = "test-lock-key".toByteArray()
        val lockValue = "lock-holder-1".toByteArray()

        // Test distributed lock acquisition and release
        val lockFlow =
            acquireLock(lockKey, lockValue)
                .flatMap { acquired ->
                    if (acquired) {
                        println("Lock acquired successfully")
                        // Simulate actual work
                        performCriticalWork(lockKey)
                            .then(releaseLock(lockKey, lockValue))
                    } else {
                        Mono.error(RuntimeException("Failed to acquire lock"))
                    }
                }

        StepVerifier
            .create(lockFlow)
            .expectNext(true)
            .verifyComplete()
    }

    @Test
    fun concurrentLockTest() {
        val lockKey = "concurrent-lock-key".toByteArray()

        // Attempt to acquire two concurrent locks
        val lock1 = acquireLockWithRetry(lockKey, "holder-1".toByteArray())
        val lock2 = acquireLockWithRetry(lockKey, "holder-2".toByteArray())

        // Only one should succeed
        val combinedTest =
            Mono
                .zip(
                    lock1.onErrorReturn(false),
                    lock2.onErrorReturn(false),
                ).map { tuple ->
                    val success1 = tuple.t1
                    val success2 = tuple.t2
                    // Only one of the two should succeed
                    (success1 && !success2) || (!success1 && success2)
                }

        StepVerifier
            .create(combinedTest)
            .expectNext(true)
            .verifyComplete()
    }

    private fun acquireLock(
        lockKey: ByteArray,
        lockValue: ByteArray,
    ): Mono<Boolean> {
        val checkAndPut =
            CheckAndMutate
                .newBuilder(lockKey)
                .ifNotExists(f, q)
                .build(Put(lockKey).addColumn(f, q, lockValue))

        return Mono
            .fromFuture(table.checkAndMutate(checkAndPut))
            .map { it.isSuccess }
    }

    private fun acquireLockWithRetry(
        lockKey: ByteArray,
        lockValue: ByteArray,
    ): Mono<Boolean> =
        acquireLock(lockKey, lockValue)
            .flatMap { success ->
                if (success) {
                    println("Lock acquired by ${String(lockValue)}")
                    Mono.just(true)
                } else {
                    Mono.error(RuntimeException("Lock acquisition failed"))
                }
            }.retryWhen(
                Retry
                    .backoff(10, Duration.ofMillis(100))
                    .maxBackoff(Duration.ofMillis(200))
                    .jitter(0.1)
                    .filter { it is RuntimeException },
            ).onErrorResume {
                println("Final lock acquisition failed for ${String(lockValue)}")
                Mono.just(false)
            }

    // Release lock (using CheckAndMutate)
    private fun releaseLock(
        lockKey: ByteArray,
        expectedValue: ByteArray,
    ): Mono<Boolean> {
        val checkAndDelete =
            CheckAndMutate
                .newBuilder(lockKey)
                .ifEquals(f, q, expectedValue)
                .build(Delete(lockKey).addColumn(f, q))

        return Mono
            .fromFuture(table.checkAndMutate(checkAndDelete))
            .map {
                println("Lock released: ${it.isSuccess}")
                it.isSuccess
            }
    }

    // Simulate critical section work
    private fun performCriticalWork(lockKey: ByteArray): Mono<Void> {
        val dataKey = "data-${String(lockKey)}".toByteArray()
        val put =
            Put(dataKey)
                .addColumn(f, q, "critical-work-done".toByteArray())

        return Mono
            .fromFuture(table.put(put))
            .doOnSuccess { println("Critical work completed") }
            .then()
    }
}
