package com.kakao.actionbase.server.api.graph.v2.metastore

import com.kakao.actionbase.v2.engine.metastore.MetastoreInspector

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

import reactor.core.publisher.Mono

@RestController
class MetastoreController(
    @Qualifier("metastoreInspector") private val metastoreInspector: MetastoreInspector,
    @Qualifier("localMetastoreInspector") private val localMetastoreInspector: MetastoreInspector,
) {
    @GetMapping("/graph/v2/metastore/global")
    fun metastoreGlobal(
        @PageableDefault(size = 100, page = 0) pageable: Pageable,
        @RequestParam(required = false) prefix: String?,
    ): Mono<Page<Map<String, Any?>>> =
        metastoreInspector
            .dumpMetastore(pageable.pageSize, pageable.offset, pageable.sort.toString(), prefix)
            .zipWith(metastoreInspector.getTotalCount(prefix))
            .map { tuple ->
                val content = tuple.t1
                val totalCount = tuple.t2
                PageImpl(content, pageable, totalCount)
            }

    @GetMapping("/graph/v2/metastore/local")
    fun metastoreLocal(
        @PageableDefault(size = 100, page = 0) pageable: Pageable,
        @RequestParam(required = false) prefix: String?,
    ): Mono<Page<Map<String, Any?>>> =
        localMetastoreInspector
            .dumpMetastore(
                pageable.pageSize,
                pageable.offset,
                pageable.sort.toString(),
                prefix,
            ).zipWith(localMetastoreInspector.getTotalCount(prefix))
            .map { tuple ->
                val content = tuple.t1
                val totalCount = tuple.t2
                PageImpl(content, pageable, totalCount)
            }
}
