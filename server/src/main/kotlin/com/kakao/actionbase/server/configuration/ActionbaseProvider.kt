package com.kakao.actionbase.server.configuration

import com.kakao.actionbase.engine.Actionbase
import com.kakao.actionbase.engine.datastore.Datastore

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ActionbaseProvider(
    private val serverProperties: ServerProperties,
) {
    @Bean
    fun provideDatastore(): Datastore = Datastore.create(serverProperties.datastore.toDescriptor())

    @Bean
    fun provideActionbase(datastore: Datastore): Actionbase = Actionbase(datastore)
}
