package com.kakao.actionbase.engine.datastore

import com.kakao.actionbase.core.storage.HBaseRecord
import com.kakao.actionbase.core.storage.MutationRequest
import com.kakao.actionbase.engine.datastore.impl.ByteArrayStore

import reactor.core.publisher.Mono

class MemoryDatastoreBucket(
    private val store: ByteArrayStore,
) : DatastoreBucket() {
    override fun get(key: ByteArray): Mono<ByteArray> = Mono.fromCallable { store[key] }.mapNotNull { it }

    override fun get(keys: List<ByteArray>): Mono<List<HBaseRecord>> {
        val result =
            keys.mapNotNull { key ->
                store[key]?.let { HBaseRecord(key = key, value = it) }
            }
        return Mono.just(result)
    }

    override fun delete(key: ByteArray): Mono<Void> = Mono.fromCallable { store.remove(key) }.then()

    override fun scan(
        prefix: ByteArray,
        limit: Int,
        start: ByteArray?,
        stop: ByteArray?,
    ): Mono<List<HBaseRecord>> = Mono.fromCallable { store.prefixScan(prefix) }

    // lock

    override fun checkAndMutate(
        key: ByteArray,
        value: ByteArray?,
        request: MutationRequest,
    ): Mono<Boolean> =
        Mono.fromCallable {
            when (request) {
                is MutationRequest.Put -> store.checkAndSet(key, value, request.value)
                is MutationRequest.Delete -> store.checkAndSet(key, value, null)
                else -> throw IllegalArgumentException("Unsupported request type: $request")
            }
        }

    // mutate

    override fun batch(requests: List<MutationRequest>): Mono<Void> =
        Mono
            .fromCallable {
                requests.map {
                    when (it) {
                        is MutationRequest.Put -> store.set(it.key, it.value)
                        is MutationRequest.Delete -> store.remove(it.key)
                        is MutationRequest.Increment -> store.increment(it.key, it.value)
                    }
                }
            }.then()
}
