package com.kakao.actionbase.engine.util

import com.kakao.actionbase.core.java.codec.common.hbase.Order
import com.kakao.actionbase.core.storage.HBaseRecord

import java.util.concurrent.TimeUnit

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine

import reactor.core.publisher.Mono

class HBaseRecordCache(
    cache: Cache<Key, Value>,
) : Cache<HBaseRecordCache.Key, HBaseRecordCache.Value> by cache {
    fun getIfNotExpired(
        row: ByteArray,
        from: ByteArray,
        to: ByteArray,
        order: Order,
        ttl: Long,
    ): Mono<List<HBaseRecord>>? = getIfPresent(Key(row, from, to, order))?.takeIf { !it.isExpired(ttl) }?.mono

    fun put(
        row: ByteArray,
        from: ByteArray,
        to: ByteArray,
        order: Order,
        value: Mono<List<HBaseRecord>>,
    ) {
        put(Key(row, from, to, order), Value(value.cache()))
    }

    data class Key(
        val key: ByteArray,
        val from: ByteArray,
        val to: ByteArray,
        val order: Order,
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Key) return false
            return key.contentEquals(other.key) &&
                from.contentEquals(other.from) &&
                to.contentEquals(other.to) &&
                order == other.order // Added order comparison
        }

        override fun hashCode(): Int {
            var result = key.contentHashCode()
            result = 31 * result + from.contentHashCode()
            result = 31 * result + to.contentHashCode()
            result = 31 * result + order.hashCode() // Added order hash code
            return result
        }
    }

    data class Value(
        val mono: Mono<List<HBaseRecord>>,
        private val timestamp: Long = System.currentTimeMillis(),
    ) {
        fun isExpired(ttl: Long): Boolean = System.currentTimeMillis() - timestamp > ttl
    }

    companion object {
        fun create(
            maxSize: Long = 500_000, // Calculation basis: average 200byte, 500k * 200byte ≈ 100MB
            expireAfterWriteMillis: Long = 60_000,
        ): HBaseRecordCache {
            val cache: Cache<Key, Value> =
                Caffeine
                    .newBuilder()
                    .maximumSize(maxSize)
                    .expireAfterWrite(expireAfterWriteMillis, TimeUnit.MILLISECONDS)
                    .build()
            return HBaseRecordCache(cache)
        }
    }
}
