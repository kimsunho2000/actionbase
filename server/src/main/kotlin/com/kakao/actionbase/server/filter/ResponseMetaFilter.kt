package com.kakao.actionbase.server.filter

import com.kakao.actionbase.server.configuration.HttpHeaderConstants
import com.kakao.actionbase.server.filter.model.ResponseMetaContext
import com.kakao.actionbase.server.util.RFC9651Mapper

import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain

import reactor.core.publisher.Mono

class ResponseMetaFilter(
    private val responseMetaFactory: ResponseMetaFactory,
) : WebFilter {
    private val mapper = RFC9651Mapper()

    override fun filter(
        exchange: ServerWebExchange,
        chain: WebFilterChain,
    ): Mono<Void?> {
        exchange.response.beforeCommit {
            val responseMetaValue = responseMetaFactory.createResponseMeta(exchange.response.headers)
            exchange.response.headers[HttpHeaderConstants.RESPONSE_META] = mapper.serialize(responseMetaValue)

            // delete all context keys to prevent leakage.
            ResponseMetaContext.entries
                .forEach { entry ->
                    exchange.response.headers.remove(entry.contextKey)
                }

            // simpl operation, return empty.
            Mono.empty()
        }
        return chain.filter(exchange)
    }
}
