package com.kakao.actionbase.v2.engine.cdc

import com.kakao.actionbase.v2.engine.client.kafka.KafkaClientFactory

import java.util.Properties

interface CdcFactory {
    fun create(
        properties: List<Properties>,
        kafkaClientFactory: KafkaClientFactory,
    ): Cdc
}
