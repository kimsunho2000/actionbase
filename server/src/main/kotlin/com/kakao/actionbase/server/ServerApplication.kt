package com.kakao.actionbase.server

import com.kakao.actionbase.server.configuration.GraphProperties

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(GraphProperties::class)
@ConfigurationPropertiesScan
class ServerApplication

fun main(args: Array<String>) {
    @Suppress("SpreadOperator")
    runApplication<ServerApplication>(*args)
}
