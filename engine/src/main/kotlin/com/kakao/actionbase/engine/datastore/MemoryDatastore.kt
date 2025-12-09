package com.kakao.actionbase.engine.datastore

import com.kakao.actionbase.core.metadata.DatastoreDescriptor
import com.kakao.actionbase.engine.datastore.impl.ByteArrayStore

import java.util.concurrent.ConcurrentHashMap

import reactor.core.publisher.Mono

class MemoryDatastore private constructor() : Datastore() {
    private val stores = ConcurrentHashMap<String, DatastoreBucket>()

    override fun close() {}

    override fun getStorage(tableName: String): Mono<DatastoreBucket> =
        Mono
            .justOrEmpty(stores[tableName])
            .switchIfEmpty(
                Mono.defer {
                    val byteArrayStore = ByteArrayStore()
                    val store = MemoryDatastoreBucket(byteArrayStore)
                    stores[tableName] = store
                    Mono.just(store)
                },
            )

    companion object {
        fun create(descriptor: DatastoreDescriptor): MemoryDatastore = MemoryDatastore()
    }
}
