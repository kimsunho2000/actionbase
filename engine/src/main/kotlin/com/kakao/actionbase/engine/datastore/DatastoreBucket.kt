package com.kakao.actionbase.engine.datastore

import com.kakao.actionbase.core.storage.HBaseRecord
import com.kakao.actionbase.core.storage.MutationRequest

import reactor.core.publisher.Mono

abstract class DatastoreBucket {
    abstract fun get(key: ByteArray): Mono<ByteArray>

    abstract fun delete(key: ByteArray): Mono<Void>

    abstract fun get(keys: List<ByteArray>): Mono<List<HBaseRecord>>

    abstract fun scan(
        prefix: ByteArray,
        limit: Int,
        start: ByteArray?,
        stop: ByteArray?,
    ): Mono<List<HBaseRecord>>

    // lock

    abstract fun checkAndMutate(
        key: ByteArray,
        value: ByteArray?,
        request: MutationRequest,
    ): Mono<Boolean>

    // mutate

    abstract fun batch(requests: List<MutationRequest>): Mono<Void>
}
