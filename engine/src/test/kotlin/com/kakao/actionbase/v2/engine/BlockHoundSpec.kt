package com.kakao.actionbase.v2.engine

import java.time.Duration

import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.core.spec.style.StringSpec
import reactor.blockhound.BlockHound
import reactor.core.publisher.Mono

class BlockHoundSpec :
    StringSpec({

        "BlockHound should detect blocking calls".config(enabled = false) {
            // BlockHound.install() affects all tests in the same JVM
            BlockHound.install()

            shouldThrowAny {
                Mono
                    .delay(Duration.ofSeconds(1))
                    .doOnNext {
                        Thread.sleep(10)
                    }.block()
            }
        }
    })
