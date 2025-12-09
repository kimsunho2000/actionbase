package com.kakao.actionbase.core.state

import com.kakao.actionbase.core.java.util.ULID

import java.util.Random

data class Event(
    val id: String,
    val type: EventType,
    val version: Long,
    val properties: Map<String, Any?>,
) {
    companion object {
        private val random = Random()

        fun issueEventId(): String = ULID.random(random)

        fun create(
            type: EventType,
            version: Long,
            properties: Map<String, Any?>,
        ): Event =
            Event(
                id = issueEventId(),
                type = type,
                version = version,
                properties = properties,
            )

        fun create(
            type: EventType,
            version: Long,
            vararg properties: Pair<String, Any?>,
        ): Event =
            create(
                type = type,
                version = version,
                properties = properties.toMap(),
            )

        fun createNotNull(
            type: EventType,
            version: Long,
            vararg properties: Pair<String, Any?>?,
        ): Event =
            Event(
                id = issueEventId(),
                type = type,
                version = version,
                properties = properties.filterNotNull().toMap(),
            )
    }
}
