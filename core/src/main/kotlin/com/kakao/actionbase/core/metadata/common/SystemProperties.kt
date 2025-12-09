package com.kakao.actionbase.core.metadata.common

enum class SystemProperties(
    private val str: String,
    private val index: Int,
) {
    VERSION("version", -1),
    SOURCE("source", -2),
    TARGET("target", -3),
    ;

    fun getStr(): String = str

    fun getIndex(): Int = index

    companion object {
        private val caseSensitiveMap: Map<String, SystemProperties> =
            values().associateBy { it.str }

        private val indexMap: Map<Int, SystemProperties> =
            values().associateBy { it.index }

        fun indexOf(str: String): Int? = caseSensitiveMap[str]?.index

        fun getOrNull(index: Int): SystemProperties? = indexMap[index]

        fun getOrNull(name: String): SystemProperties? = caseSensitiveMap[name]

        fun isSystemProperty(str: String): Boolean = caseSensitiveMap.containsKey(str)
    }
}
