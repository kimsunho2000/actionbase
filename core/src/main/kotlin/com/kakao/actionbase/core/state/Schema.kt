package com.kakao.actionbase.core.state

import com.kakao.actionbase.core.codec.XXHash32Wrapper

import com.fasterxml.jackson.annotation.JsonIgnore

interface AbstractSchema {
    @get:JsonIgnore
    val names: List<String>

    @get:JsonIgnore
    val nullabilityMap: Map<String, Boolean>

    @get:JsonIgnore
    val codeToName: Map<Int, String>

    fun isNullable(fieldName: String): Boolean =
        nullabilityMap[fieldName]
            ?: throw IllegalArgumentException("Field '$fieldName' does not exist in the nullability map.")

    fun nameOf(code: Int): String =
        codeToName[code]
            ?: throw IllegalArgumentException("Code '$code' does not exist in the code-to-name map.")

    fun nameOfOrNull(code: Int): String? = codeToName[code]

    companion object {
        private val hash = XXHash32Wrapper.default

        fun codeOf(name: String): Int = hash.stringHash(name)
    }
}

data class Schema(
    override val nullabilityMap: Map<String, Boolean>,
) : AbstractSchema {
    override val names: List<String> = nullabilityMap.keys.toList()

    override val codeToName: Map<Int, String> = nullabilityMap.keys.associateBy { name -> AbstractSchema.codeOf(name) }
}
