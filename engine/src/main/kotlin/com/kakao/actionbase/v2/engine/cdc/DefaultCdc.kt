package com.kakao.actionbase.v2.engine.cdc

import com.kakao.actionbase.v2.engine.entity.EntityName
import com.kakao.actionbase.v2.engine.producer.Producer

import reactor.core.publisher.Mono

class DefaultCdc(
    val producer: Producer,
) : Cdc {
    override fun write(log: CdcContext): Mono<Void> = producer.produce(log)

    override fun writeHeartBeat(
        labelName: EntityName,
        hostName: String,
    ): Mono<Void> = producer.produceHeartBeat(labelName, hostName)
}
