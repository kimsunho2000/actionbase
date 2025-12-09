package com.kakao.actionbase.v2.engine.sql

import com.kakao.actionbase.v2.core.types.StructType

@JvmInline
value class Row(
    val array: Array<Any?>,
) {
    inline val size: Int
        get() = array.size

    operator fun get(index: Int): Any? = array[index]

    operator fun set(
        index: Int,
        value: Any?,
    ) {
        array[index] = value
    }

    operator fun plus(other: Row): Row {
        val result = this.array.copyOf(this.array.size + other.array.size)
        System.arraycopy(other.array, 0, result, this.array.size, other.array.size)
        return Row(result)
    }

    infix fun contentEquals(other: Row): Boolean = array.contentEquals(other.array)
}

fun rowOf(vararg values: Any?): Row = Row(arrayOf(*values))

data class RowWithSchema(
    val row: Row,
    val schema: StructType,
) {
    // ... existing methods ...

    operator fun get(name: String): Any {
        val index = schema.fieldIndex(name)
        return get(index)
    }

    fun get(i: Int): Any = row[i] ?: throw InvalidRowDataException("Unexpected type at index $i")

    fun getOrNull(i: Int): Any? = row[i]

    fun getOrNull(name: String): Any? {
        val index = schema.fieldIndex(name)
        return getOrNull(index)
    }

    // Getters by column name
    fun getInt(name: String): Int = castTo(name)

    fun getBoolean(name: String): Boolean = castTo(name)

    fun getString(name: String): String = castTo(name)

    fun getLong(name: String): Long = castTo(name)

    // Getters by column index
    fun getLong(i: Int): Long = castTo(i)

    fun getString(i: Int): String = castTo(i)

    private inline fun <reified T> castTo(name: String): T = get(name) as? T ?: throw AssertionError("Unexpected type mismatch detected.")

    private inline fun <reified T> castTo(i: Int): T = get(i) as? T ?: throw AssertionError("Unexpected type mismatch detected.")

    fun toMap(): Map<String, Any?> = schema.fieldNames.zip(row.array).toMap()
}
