package com.kakao.actionbase.v2.engine.cdc

import com.kakao.actionbase.v2.engine.entity.EntityName

import reactor.core.publisher.Mono

interface Cdc {
    fun write(log: CdcContext): Mono<Void>

    fun writeHeartBeat(
        labelName: EntityName,
        hostName: String,
    ): Mono<Void>
}
