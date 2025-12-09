package com.kakao.actionbase.v2.engine.client.kafka

import java.util.Properties

interface KafkaClientFactory {
    fun create(properties: Properties): KafkaClient
}
