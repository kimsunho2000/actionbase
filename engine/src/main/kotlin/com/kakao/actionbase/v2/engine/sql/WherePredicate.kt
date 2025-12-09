package com.kakao.actionbase.v2.engine.sql

import com.kakao.actionbase.v2.core.types.EdgeSchema
import com.kakao.actionbase.v2.core.types.StructType

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(
    JsonSubTypes.Type(value = WherePredicate.In::class, name = "in"),
    JsonSubTypes.Type(value = WherePredicate.Eq::class, name = "="),
    JsonSubTypes.Type(value = WherePredicate.Gt::class, name = ">"),
    JsonSubTypes.Type(value = WherePredicate.Gte::class, name = ">="),
    JsonSubTypes.Type(value = WherePredicate.Lt::class, name = "<"),
    JsonSubTypes.Type(value = WherePredicate.Lte::class, name = "<="),
    JsonSubTypes.Type(value = WherePredicate.Between::class, name = "between"),
)
sealed class WherePredicate {
    abstract val key: String

    abstract fun normalize(): WherePredicate

    abstract fun eval(
        row: Row,
        schema: StructType,
    ): Boolean

    abstract fun toSql(): String

    data class In(
        override val key: String,
        val values: List<Any>,
    ) : WherePredicate() {
        override fun normalize() = copy(values = values.map { it.toString() })

        override fun eval(
            row: Row,
            schema: StructType,
        ): Boolean {
            val index = schema.fieldIndex(key)
            val value = row[index]
            return values.contains(value)
        }

        override fun toSql(): String = "`$key` IN (${values.joinToString(",", "'", "'")})"
    }

    data class Eq(
        override val key: String,
        val value: Any,
    ) : WherePredicate() {
        override fun normalize() = copy(value = value.toString())

        override fun eval(
            row: Row,
            schema: StructType,
        ): Boolean {
            val index = schema.fieldIndex(key)
            val value = row[index]
            return value == this.value
        }

        override fun toSql(): String = "`$key` = '$value'"
    }

    data class Gt(
        override val key: String,
        val value: Any,
    ) : WherePredicate() {
        override fun normalize() = copy(value = value.toString())

        override fun eval(
            row: Row,
            schema: StructType,
        ): Boolean {
            val index = schema.fieldIndex(key)
            val value = row[index]
            return value.toString().toDouble() > this.value.toString().toDouble()
        }

        override fun toSql(): String = "`$key` > '$value'"
    }

    data class Gte(
        override val key: String,
        val value: Any,
    ) : WherePredicate() {
        override fun normalize() = copy(value = value.toString())

        override fun eval(
            row: Row,
            schema: StructType,
        ): Boolean {
            val index = schema.fieldIndex(key)
            val value = row[index]
            return value.toString().toDouble() >= this.value.toString().toDouble()
        }

        override fun toSql(): String = "`$key` >= '$value'"
    }

    data class Lt(
        override val key: String,
        val value: Any,
    ) : WherePredicate() {
        override fun normalize() = copy(value = value.toString())

        override fun eval(
            row: Row,
            schema: StructType,
        ): Boolean {
            val index = schema.fieldIndex(key)
            val value = row[index]
            return value.toString().toDouble() < this.value.toString().toDouble()
        }

        override fun toSql(): String = "`$key` < '$value'"
    }

    data class Lte(
        override val key: String,
        val value: Any,
    ) : WherePredicate() {
        override fun normalize() = copy(value = value.toString())

        override fun eval(
            row: Row,
            schema: StructType,
        ): Boolean {
            val index = schema.fieldIndex(key)
            val value = row[index]
            return value.toString().toDouble() <= this.value.toString().toDouble()
        }

        override fun toSql(): String = "`$key` <= '$value'"
    }

    data class Between(
        override val key: String,
        val from: Any,
        val to: Any,
    ) : WherePredicate() {
        override fun normalize() = copy(from = from.toString(), to = to.toString())

        override fun eval(
            row: Row,
            schema: StructType,
        ): Boolean {
            val index = schema.fieldIndex(key)
            val value = row[index]
            return value.toString().toDouble() in this.from.toString().toDouble()..this.to.toString().toDouble()
        }

        override fun toSql(): String = "`$key` BETWEEN '$from' AND '$to'"
    }

    companion object {
        fun parse(filterString: String): List<WherePredicate> {
            val filters =
                filterString.split(";").map {
                    val (key, op, value) = it.split(":")
                    when (op) {
                        "in" -> In(key, value.split(","))
                        "eq" -> Eq(key, value)
                        "gt" -> Gt(key, value)
                        "gte" -> Gte(key, value)
                        "lt" -> Lt(key, value)
                        "lte" -> Lte(key, value)
                        "bt" -> {
                            val (from, to) = value.split(",")
                            Between(key, from, to)
                        }
                        else -> throw IllegalArgumentException("Unsupported operator: $op")
                    }
                }
            return filters
        }

        fun parse(
            filterString: String,
            schema: EdgeSchema,
        ): List<WherePredicate> {
            val filters =
                filterString.split(";").map {
                    val (key, op, value) = it.split(":")
                    val type = schema.getField(key).type
                    when (op) {
                        "in" -> In(key, value.split(",").map { x -> type.castNotNull(x.trim()) })
                        "eq" -> Eq(key, type.castNotNull(value))
                        "gt" -> Gt(key, type.castNotNull(value))
                        "gte" -> Gte(key, type.castNotNull(value))
                        "lt" -> Lt(key, type.castNotNull(value))
                        "lte" -> Lte(key, type.castNotNull(value))
                        "bt" -> {
                            val (from, to) = value.split(",")
                            Between(key, type.castNotNull(from), type.castNotNull(to))
                        }

                        else -> throw IllegalArgumentException("Unsupported operator: $op")
                    }
                }
            return filters
        }
    }
}
