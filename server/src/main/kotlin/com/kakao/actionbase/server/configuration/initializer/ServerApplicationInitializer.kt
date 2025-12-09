package com.kakao.actionbase.server.configuration.initializer

import com.kakao.actionbase.server.configuration.ServerProperties

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.info.BuildProperties
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component

@Component
class ServerApplicationInitializer(
    private val serverProperties: ServerProperties,
    private val buildProperties: BuildProperties,
    private val environment: Environment,
) : CommandLineRunner {
    private val logger =
        LoggerFactory.getLogger(
            ServerApplicationInitializer::class.java,
        )

    override fun run(vararg args: String) {
        val activeProfiles = environment.activeProfiles.takeIf { it.isNotEmpty() }?.joinToString(", ") ?: "default"
        val startupTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        val javaVersion = System.getProperty("java.version")
        val buildTime =
            buildProperties.time
                ?.let { instant ->
                    LocalDateTime
                        .ofInstant(instant, java.time.ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                } ?: "Unknown"

        println(
            """

                _        _   _             _
               / \   ___| |_(_) ___  _ __ | |__   __ _ ___  ___
              / _ \ / __| __| |/ _ \| '_ \| '_ \ / _` / __|/ _ \
             / ___ \ (__| |_| | (_) | | | | |_) | (_| \__ \  __/
            /_/   \_\___|\__|_|\___/|_| |_|_.__/ \__,_|___/\___| (${buildProperties.version}, $buildTime)

            * Java: $javaVersion
            * Tenant: ${serverProperties.tenant}
            * startUp: $startupTime
            * activeProfiles: $activeProfiles
            * Datastore: ${serverProperties.datastore.type} ${serverProperties.datastore.configuration}

            """.trimIndent(),
        )
        logger.info("Actionbase application has started.")
    }
}
