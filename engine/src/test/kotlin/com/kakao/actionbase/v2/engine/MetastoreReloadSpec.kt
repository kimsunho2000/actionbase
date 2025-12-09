package com.kakao.actionbase.v2.engine

import com.kakao.actionbase.v2.engine.test.GraphFixtures

import java.time.Duration
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

import org.slf4j.Logger

import io.kotest.core.spec.style.StringSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import reactor.core.publisher.Mono

class MetastoreReloadSpec :
    StringSpec({

        lateinit var graph: Graph
        lateinit var logger: Logger

        beforeTest {
            graph = spyk(GraphFixtures.create())
            logger = mockk(relaxed = true)
        }

        afterTest {
            graph.close()
        }

        "startMetastoreReload should handle errors during updateStorages" {
            val countDownLatch = CountDownLatch(1)

            every { graph.updateStorages() } answers {
                Mono.error<Void>(RuntimeException("Storage update failed")).doOnTerminate { countDownLatch.countDown() }
            }
            every { graph.updateServices() } returns Mono.empty()
            every { graph.updateLabels() } returns Mono.empty()

            graph.startMetastoreReload(Duration.ZERO, Duration.ofDays(1), logger)

            countDownLatch.await(2, TimeUnit.SECONDS) // wait for reload or timeout after 2 seconds

            verify(exactly = 1) {
                logger.error(match { it.contains("Error occurred during metastore reload") }, any<Any>(), any<Throwable>())
            }
        }
    })
