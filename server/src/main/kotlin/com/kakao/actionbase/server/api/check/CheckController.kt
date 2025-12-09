package com.kakao.actionbase.server.api.check

import com.kakao.actionbase.server.filter.model.ResponseMetaContext.Companion.withResponseMetaTest

import java.time.Duration

import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ServerWebExchange

import reactor.core.publisher.Mono

@RestController
@Suppress("TooGenericExceptionThrown")
class CheckController {
    private val logger = LoggerFactory.getLogger(this.javaClass.canonicalName)

    @GetMapping("/graph/check/response-meta")
    fun setResponseMeta(
        @RequestParam(name = "value", defaultValue = "response-meta-value") value: String,
    ): Mono<ResponseEntity<String>> =
        Mono.just(
            ResponseEntity
                .ok()
                .withResponseMetaTest(value)
                .body(value),
        )

    @GetMapping("/graph/check/emoji")
    fun emoji(): Mono<ResponseEntity<String>> = Mono.just(ResponseEntity.ok("🍏"))

    @GetMapping("/graph/check/error")
    fun error(): Mono<ResponseEntity<String>> = throw RuntimeException("/check/error is called")

    @Suppress("TooGenericExceptionThrown")
    @GetMapping("/graph/check/mono")
    fun monoError(): Mono<ResponseEntity<String>> = Mono.error(RuntimeException("/check/mono is called"))

    @GetMapping("/graph/check/sentry")
    fun sentryError(): Mono<ResponseEntity<String>> {
        try {
            throw RuntimeException("/check/sentry is called")
        } catch (e: Exception) {
            logger.error(e.message, e)
        }
        return Mono.empty()
    }

    /*
        Stop the request with ctrl-c before 10 seconds to verify that the process is immediately cancelled and a cancel signal is generated
        curl -H "Connection: close" -H "Authorization: s:0" http://localhost:8080/graph/check/delay_without_cache
     */
    @GetMapping("/graph/check/delay_without_cache")
    fun delayWithoutCache(): Mono<ResponseEntity<String>> =
        Mono
            .just(ResponseEntity.ok("delayWithoutCache"))
            .delayElement(Duration.ofSeconds(10))
            .doOnCancel { logger.info("delayWithoutCache is cancelled") }
            .doFinally { signal -> logger.info("delayWithoutCache is finally $signal") }

    /*
        Stop the request with ctrl-c before 10 seconds to verify that the process is immediately cancelled and a cancel signal is generated
        curl -H "Connection: close" -H "Authorization: s:0" http://localhost:8080/graph/check/delay_with_cache
     */
    @GetMapping("/graph/check/delay_with_cache")
    fun delayWithCache(): Mono<ResponseEntity<String>> =
        Mono
            .just(ResponseEntity.ok("delayWithoutCache"))
            .delayElement(Duration.ofSeconds(10))
            .doOnCancel { logger.info("delayWithoutCache is cancelled") }
            .doFinally { signal -> logger.info("delayWithCache is finally $signal") }
            .timeout(Duration.ofSeconds(8))
            .cache()

    @RequestMapping(
        "/graph/check/echo",
        method = [RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE],
    )
    fun echoRequest(exchange: ServerWebExchange): Mono<Map<String, Any>> =
        exchange.request.body
            .collectList()
            .map {
                val body = it.joinToString { buffer -> buffer.asInputStream().readAllBytes().decodeToString() }
                mapOf(
                    "path" to exchange.request.path.value(),
                    "method" to exchange.request.method.toString(),
                    "query" to exchange.request.queryParams,
                    "headers" to exchange.request.headers,
                    "body" to body,
                )
            }

    @RequestMapping(
        "/graph/check/virtual",
        method = [RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE],
    )
    fun virtualRequest(
        @RequestParam("delay", defaultValue = "1") delay: Long,
        @RequestParam("size", defaultValue = "1") size: Int,
        @RequestParam("status", defaultValue = "200") status: Int,
    ): Mono<Map<String, Any>> =
        Mono
            .delay(Duration.ofMillis(delay))
            .map {
                val body =
                    (0 until size)
                        .map { i -> ('a' + i % 26) }
                        .joinToString("")

                mapOf(
                    "delay" to delay,
                    "size" to size,
                    "status" to status,
                    "body" to body,
                )
            }
}
