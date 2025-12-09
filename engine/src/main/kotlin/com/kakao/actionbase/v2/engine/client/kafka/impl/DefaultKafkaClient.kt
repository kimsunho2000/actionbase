package com.kakao.actionbase.v2.engine.client.kafka.impl

import com.kakao.actionbase.v2.engine.client.kafka.KafkaClient

import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord

import reactor.core.publisher.Mono

class DefaultKafkaClient(
    private val producer: KafkaProducer<ByteArray, ByteArray>,
    private val topic: String,
) : KafkaClient {
    override fun send(value: Mono<Pair<ByteArray?, ByteArray>>): Mono<Void> {
        val record = value.map { ProducerRecord<ByteArray, ByteArray>(topic, it.first, it.second) }
        return record.map { producer.send(it).get() }.then()
    }
}
