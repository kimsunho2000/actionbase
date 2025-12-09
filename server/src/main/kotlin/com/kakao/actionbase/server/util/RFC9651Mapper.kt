package com.kakao.actionbase.server.util

/**
 * Partial implementation of RFC 9651 Structured Field Values (only supports String type for Dictionary)
 *
 * Example:
 * serialize: version="1.0", host="server-01"
 * deserialize: {"version": "1.0", "host": "server-01"}
 */
class RFC9651Mapper {
    fun serialize(metaMap: Map<String, String>): String =
        metaMap.entries.joinToString(", ") { (key, value) ->
            "$key=\"${escapeString(value)}\""
        }

    fun deserialize(metaString: String): Map<String, String> {
        if (metaString.isBlank()) return emptyMap()

        return try {
            metaString
                .split(",")
                .mapNotNull { part ->
                    val trimmed = part.trim()
                    val equalIndex = trimmed.indexOf('=')
                    if (equalIndex == -1) return@mapNotNull null

                    val key = trimmed.substring(0, equalIndex).trim()
                    val value =
                        trimmed
                            .substring(equalIndex + 1)
                            .trim()
                            .removeSurrounding("\"")
                            .replace("\\\"", "\"")
                            .replace("\\\\", "\\")

                    if (key.isNotEmpty()) Pair(key, value) else null
                }.toMap()
        } catch (e: Exception) {
            emptyMap()
        }
    }

    private fun escapeString(value: String): String = value.replace("\\", "\\\\").replace("\"", "\\\"")
}
