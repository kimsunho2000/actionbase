package com.kakao.actionbase.v2.engine.producer

import com.kakao.actionbase.v2.engine.entity.EntityName

import reactor.core.publisher.Mono

interface Producer {
    fun produce(message: Log): Mono<Void>

    fun produceHeartBeat(
        labelName: EntityName,
        hostName: String,
    ): Mono<Void>
}
