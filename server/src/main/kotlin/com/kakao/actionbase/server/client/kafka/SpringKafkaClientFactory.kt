package com.kakao.actionbase.server.client.kafka

import com.kakao.actionbase.v2.engine.client.kafka.KafkaClient
import com.kakao.actionbase.v2.engine.client.kafka.KafkaClientFactory

import java.util.Properties

import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.ByteArraySerializer
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate

import reactor.kafka.sender.SenderOptions

object SpringKafkaClientFactory : KafkaClientFactory {
    override fun create(properties: Properties): KafkaClient {
        val topic: String = properties.getProperty("topic")
        val configProperties =
            Properties().apply {
                putAll(properties)
                setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, ByteArraySerializer::class.qualifiedName)
                setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer::class.qualifiedName)
                remove("topic")
            }
        val options = SenderOptions.create<ByteArray, ByteArray>(configProperties)
        val kafkaTemplate = ReactiveKafkaProducerTemplate(options)
        return SpringKafkaClient(kafkaTemplate, topic)
    }
}
