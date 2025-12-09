package com.kakao.actionbase.server.client.kafka

import com.kakao.actionbase.v2.engine.client.kafka.KafkaClient

import org.apache.kafka.clients.producer.ProducerRecord
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate

import reactor.core.publisher.Mono
import reactor.kafka.sender.SenderRecord

class SpringKafkaClient(
    private val kafkaTemplate: ReactiveKafkaProducerTemplate<ByteArray, ByteArray>,
    private val topic: String,
) : KafkaClient {
    override fun send(value: Mono<Pair<ByteArray?, ByteArray>>): Mono<Void> {
        val record =
            value.map {
                SenderRecord.create(
                    ProducerRecord<ByteArray, ByteArray>(topic, it.first, it.second),
                    null,
                )
            }
        return kafkaTemplate.send(record).then()
    }
}
