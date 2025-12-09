package com.kakao.actionbase.core.state

data class State(
    val active: Boolean,
    val version: Long,
    val createdAt: Long?,
    val deletedAt: Long?,
    val properties: Map<String, StateValue>,
) {
    companion object {
        val initial: State =
            State(
                active = false,
                version = Long.MIN_VALUE,
                createdAt = null,
                deletedAt = null,
                properties = emptyMap(),
            )

        fun create(
            active: Boolean,
            version: Long,
            createdAt: Long? = null,
            deletedAt: Long? = null,
            properties: List<Pair<String, StateValue>>,
        ): State =
            State(
                active = active,
                version = version,
                createdAt = createdAt,
                deletedAt = deletedAt,
                properties = properties.toMap(),
            )

        fun create(
            active: Boolean,
            version: Long,
            createdAt: Long? = null,
            deletedAt: Long? = null,
            vararg properties: Pair<String, StateValue>,
        ): State =
            create(
                active = active,
                version = version,
                createdAt = createdAt,
                deletedAt = deletedAt,
                properties = properties.toList(),
            )

        fun createNotNull(
            active: Boolean,
            version: Long,
            createdAt: Long? = null,
            deletedAt: Long? = null,
            vararg properties: Pair<String, StateValue>?,
        ): State =
            create(
                active = active,
                version = version,
                createdAt = createdAt,
                deletedAt = deletedAt,
                properties = properties.filterNotNull(),
            )
    }
}
