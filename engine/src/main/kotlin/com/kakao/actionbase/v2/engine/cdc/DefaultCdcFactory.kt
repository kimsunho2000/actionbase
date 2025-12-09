package com.kakao.actionbase.v2.engine.cdc

import com.kakao.actionbase.v2.engine.client.kafka.KafkaClientFactory
import com.kakao.actionbase.v2.engine.producer.KafkaProducer
import com.kakao.actionbase.v2.engine.producer.LoggerProducer
import com.kakao.actionbase.v2.engine.producer.Producer
import com.kakao.actionbase.v2.engine.producer.ProducerList

import java.util.Properties

object DefaultCdcFactory : CdcFactory {
    override fun create(
        properties: List<Properties>,
        kafkaClientFactory: KafkaClientFactory,
    ): Cdc {
        val producers =
            if (properties.isNotEmpty()) {
                properties.map { createCdcProducer(it, kafkaClientFactory) }
            } else {
                listOf(LoggerProducer())
            }.let { ProducerList(it) }
        return DefaultCdc(producers)
    }

    private fun createCdcProducer(
        properties: Properties,
        kafkaClientFactory: KafkaClientFactory,
    ): Producer =
        if (properties.getProperty("bootstrap.servers", "") != "" && properties.getProperty("topic", "") != "") {
            val client = kafkaClientFactory.create(properties)
            KafkaProducer.createCdcProducer(client, properties)
        } else {
            LoggerProducer()
        }
}
