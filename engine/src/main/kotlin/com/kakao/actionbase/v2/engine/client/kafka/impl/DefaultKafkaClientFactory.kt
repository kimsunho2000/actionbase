package com.kakao.actionbase.v2.engine.client.kafka.impl

import com.kakao.actionbase.v2.engine.client.kafka.KafkaClient
import com.kakao.actionbase.v2.engine.client.kafka.KafkaClientFactory
import com.kakao.actionbase.v2.engine.util.getLogger

import java.util.Properties

import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.ByteArraySerializer

object DefaultKafkaClientFactory : KafkaClientFactory {
    val logger = getLogger()

    override fun create(properties: Properties): KafkaClient {
        logger.warn("Do not use DefaultKafkaClientFactory in production")
        val topic: String = properties.getProperty("topic")
        val configProperties =
            Properties().apply {
                putAll(properties)
                setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, ByteArraySerializer::class.qualifiedName)
                setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer::class.qualifiedName)
                remove("topic")
            }
        val producer: KafkaProducer<ByteArray, ByteArray> = KafkaProducer(configProperties)

        return DefaultKafkaClient(producer, topic)
    }
}
