package com.kakao.actionbase.v2.engine.producer

import com.kakao.actionbase.v2.engine.entity.EntityName

import reactor.core.publisher.Mono

class NilProducer : Producer {
    override fun produce(message: Log): Mono<Void> = Mono.empty()

    override fun produceHeartBeat(
        labelName: EntityName,
        hostName: String,
    ): Mono<Void> = Mono.empty()
}
