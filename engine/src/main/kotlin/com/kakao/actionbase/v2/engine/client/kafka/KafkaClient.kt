package com.kakao.actionbase.v2.engine.client.kafka

import reactor.core.publisher.Mono

interface KafkaClient {
    fun send(value: Mono<Pair<ByteArray?, ByteArray>>): Mono<Void>
}
