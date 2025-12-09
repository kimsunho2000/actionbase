package com.kakao.actionbase.v2.engine.producer

import com.kakao.actionbase.v2.engine.entity.EntityName
import com.kakao.actionbase.v2.engine.util.getLogger

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

class ProducerList(
    producers: List<Producer>,
) : Producer {
    companion object {
        private val logger = getLogger()
    }

    private val producers: List<Producer>

    private val primaryIdx = 0
    private val backUpIdx = 1

    init {
        assert(producers.size <= 2) { "producers size should be less than 2" }
        this.producers = producers
    }

    private fun producePrimaryAndBackUp(message: Log): Mono<Void> {
        val primary = producers[primaryIdx].produce(message)
        return primary
            .onErrorResume { error ->
                logger.error("primary producer send failed: {}", error.toString(), error)
                producers[backUpIdx]
                    .produce(message)
                    .doOnError {
                        logger.error("secondary producer send failed: {}", error.toString(), it)
                    }
            }
    }

    private fun producePrimaryOnly(message: Log): Mono<Void> = producers[0].produce(message)

    override fun produce(message: Log): Mono<Void> =
        if (producers.size == 1) {
            producePrimaryOnly(message)
        } else {
            producePrimaryAndBackUp(message)
        }

    override fun produceHeartBeat(
        labelName: EntityName,
        hostName: String,
    ): Mono<Void> =
        Flux
            .fromIterable(producers)
            .flatMap { producer ->
                val mono =
                    producer
                        .produceHeartBeat(labelName, hostName)
                        .onErrorContinue { error, _ -> logger.warn("heartbeat send failed: {}", error.toString(), error) }
                mono
            }.collectList()
            .then()
}
