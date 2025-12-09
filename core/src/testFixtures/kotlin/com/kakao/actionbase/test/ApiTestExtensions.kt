package com.kakao.actionbase.test

fun <K, V> Map<K, V>.toQueries(): Map<K, String> =
    this
        .filter {
            it.value != null &&
                !(it.value is List<*> && (it.value as List<*>).isEmpty())
        }.mapValues {
            val value = it.value

            if (value is List<*>) {
                return@mapValues value.joinToString(",")
            }

            value.toString()
        }
