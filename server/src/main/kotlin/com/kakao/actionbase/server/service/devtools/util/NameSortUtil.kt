package com.kakao.actionbase.server.service.devtools.util

data class SortedEntry<T>(
    val entry: T,
    val prefix: String,
    val timestamp: String?,
)

object NameSortUtil {
    fun extractPrefixAndTimestamp(fullName: String): Pair<String, String?> {
        val regex = Regex("(.*)_([0-9]{8}_[0-9]{6})$")
        val matchResult = regex.find(fullName)
        return if (matchResult != null) {
            val (prefix, timestamp) = matchResult.destructured
            prefix to timestamp
        } else {
            fullName to null
        }
    }

    fun <T> sortList(
        entries: List<T>,
        nameExtractor: (T) -> String,
        updateEntry: (T, Boolean) -> T,
    ): List<T> {
        val sortedEntries =
            entries.map { entry ->
                val name = nameExtractor(entry)
                val (prefix, timestamp) = extractPrefixAndTimestamp(name)
                SortedEntry(entry, prefix, timestamp)
            }

        val groupedEntries = sortedEntries.groupBy { it.prefix }
        return groupedEntries.flatMap { (_, group) ->
            val (withTimestamp, withoutTimestamp) = group.partition { it.timestamp != null }
            val sortedWithTimestamp = withTimestamp.sortedByDescending { it.timestamp }
            val sortedWithoutTimestamp = withoutTimestamp.sortedBy { it.prefix }

            val resultList = mutableListOf<T>()
            if (sortedWithTimestamp.isNotEmpty()) {
                resultList.add(updateEntry(sortedWithTimestamp.first().entry, true))
                resultList.addAll(sortedWithTimestamp.drop(1).map { updateEntry(it.entry, false) })
            }
            resultList.addAll(sortedWithoutTimestamp.map { updateEntry(it.entry, true) })

            resultList
        }
    }
}
