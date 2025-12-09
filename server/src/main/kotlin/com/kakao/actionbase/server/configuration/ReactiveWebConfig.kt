package com.kakao.actionbase.server.configuration

import com.kakao.actionbase.server.configuration.resolver.ServerRequestContextArgumentResolver

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.PageRequest
import org.springframework.data.web.ReactivePageableHandlerMethodArgumentResolver
import org.springframework.web.reactive.config.WebFluxConfigurer
import org.springframework.web.reactive.result.method.annotation.ArgumentResolverConfigurer

@Configuration
class ReactiveWebConfig(
    @Value("\${com.kakao.actionbase.server.entity.page.size}")
    private val pageSize: Int = 2000,
) : WebFluxConfigurer {
    override fun configureArgumentResolvers(configurer: ArgumentResolverConfigurer) {
        val pageableResolver =
            ReactivePageableHandlerMethodArgumentResolver().apply {
                setFallbackPageable(PageRequest.of(0, pageSize))
            }
        configurer.addCustomResolver(pageableResolver)

        val serverRequestContextArgumentResolver = ServerRequestContextArgumentResolver()
        configurer.addCustomResolver(serverRequestContextArgumentResolver)
    }
}
