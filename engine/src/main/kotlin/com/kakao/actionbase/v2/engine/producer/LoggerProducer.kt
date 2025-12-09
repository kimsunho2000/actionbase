package com.kakao.actionbase.v2.engine.producer

import com.kakao.actionbase.v2.engine.entity.EntityName
import com.kakao.actionbase.v2.engine.util.getLogger

import org.slf4j.Logger

import reactor.core.publisher.Mono

class LoggerProducer : Producer {
    companion object {
        private val logger: Logger = getLogger()
    }

    override fun produce(message: Log): Mono<Void> =
        Mono.fromRunnable {
            logger.info(message.toJsonString().second)
        }

    override fun produceHeartBeat(
        labelName: EntityName,
        hostName: String,
    ): Mono<Void> = Mono.empty()
}
