package com.kakao.actionbase.engine.datastore

import com.kakao.actionbase.core.metadata.DatastoreDescriptor
import com.kakao.actionbase.core.metadata.common.DatastoreType

import java.lang.AutoCloseable

import org.slf4j.LoggerFactory

import reactor.core.publisher.Mono

abstract class Datastore : AutoCloseable {
    abstract fun getStorage(tableName: String): Mono<DatastoreBucket>

    companion object {
        private val LOGGER = LoggerFactory.getLogger(Datastore::class.java)

        fun create(descriptor: DatastoreDescriptor): Datastore =
            when (descriptor.type) {
                DatastoreType.MEMORY -> MemoryDatastore.create(descriptor)
                else -> {
                    LOGGER.warn("Unsupported datastore type: ${descriptor.type}. Defaulting to MemoryDatastore.")
                    MemoryDatastore.create(descriptor)
                }
            }
    }
}
