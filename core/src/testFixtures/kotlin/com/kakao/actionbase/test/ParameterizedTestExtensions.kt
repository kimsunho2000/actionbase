package com.kakao.actionbase.test

import com.kakao.actionbase.core.state.Event
import com.kakao.actionbase.core.state.EventType
import com.kakao.actionbase.core.state.SpecialStateValue
import com.kakao.actionbase.core.state.StateValue

import java.util.stream.IntStream
import java.util.stream.Stream

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

fun String.toBooleanFlexible(): Boolean =
    when (lowercase()) {
        "t" -> true
        "f" -> false
        else -> toBoolean()
    }

fun String.toEventSequence(createEvent: (String, Long) -> Event): List<Event> =
    this.split(";").map { s ->
        val parts = s.trim().toCharArray()
        if (parts.size != 2) {
            throw IllegalArgumentException("Event must be in format 'event:version' but was: $s")
        }
        val event = parts[0].toString()
        val version = parts[1].toString().toLong()
        createEvent(event, version)
    }

fun String.toEventType(): EventType =
    when (this) {
        "I" -> EventType.INSERT
        "U" -> EventType.UPDATE
        "D" -> EventType.DELETE
        else -> EventType.valueOf(this)
    }

fun Char.toEventType(): EventType = toString().toEventType()

fun Any?.toStateValue(version: Long): StateValue = StateValue(version, this)

fun <T : Any> T.handleSpecialValue(block: T.() -> Any = { this }): Any =
    when (this) {
        "U" -> SpecialStateValue.UNSET.code()
        "D" -> SpecialStateValue.DELETED.code()
        else -> this.block()
    }

// 1. Basic permutations (no duplicates, order matters)
// Creates arrays with different orders using all elements
// Example: [1,2,3] → [[1,2,3], [1,3,2], [2,1,3], [2,3,1], [3,1,2], [3,2,1]]
fun <T> List<T>.permutations(): Stream<List<T>> =
    when (size) {
        0 -> Stream.of(emptyList())
        1 -> Stream.of(this)
        else ->
            IntStream
                .range(0, size)
                .boxed()
                .flatMap { index ->
                    val element = this[index]
                    (this - element).permutations().map { listOf(element) + it }
                }
    }

// 2. Basic combinations (no duplicates, order doesn't matter)
// Selects r elements to create combinations regardless of order
// Example: [1,2,3].combinations(2) → [[1,2], [1,3], [2,3]]
fun <T> List<T>.combinations(r: Int): Stream<List<T>> =
    when {
        r < 0 -> Stream.empty()
        r == 0 -> Stream.of(emptyList())
        r > size -> Stream.empty()
        r == size -> Stream.of(this)
        else -> {
            val head = this[0]
            val tail = this.drop(1)
            Stream.concat(
                tail.combinations(r - 1).map { listOf(head) + it },
                tail.combinations(r),
            )
        }
    }

// 3. All possible size combinations
// Creates all possible combinations from 0 to all elements
// Example: [1,2] → [[], [1], [2], [1,2]]
fun <T> List<T>.allCombinations(): Stream<List<T>> =
    IntStream
        .rangeClosed(0, size)
        .boxed()
        .flatMap { r -> combinations(r) }

// 4. Permutations with repetition (Cartesian Product)
// Creates permutations of specified length using the same element multiple times
// Example: [1,2].permutationsWithRepetition(2) → [[1,1], [1,2], [2,1], [2,2]]
fun <T> List<T>.permutationsWithRepetition(length: Int): Stream<List<T>> =
    when (length) {
        0 -> Stream.of(emptyList())
        1 -> stream().map { listOf(it) }
        else ->
            permutationsWithRepetition(length - 1).flatMap { prefix ->
                stream().map { element -> prefix + element }
            }
    }

// 5. Combinations with repetition
// Creates combinations selecting r elements using the same element multiple times
// Example: [1,2].combinationsWithRepetition(2) → [[1,1], [1,2], [2,2]]
fun <T> List<T>.combinationsWithRepetition(r: Int): Stream<List<T>> =
    when {
        r < 0 -> Stream.empty()
        r == 0 -> Stream.of(emptyList())
        isEmpty() -> Stream.empty()
        else -> {
            val head = this[0]
            val tail = this.drop(1)
            Stream.concat(
                combinationsWithRepetition(r - 1).map { listOf(head) + it },
                tail.combinationsWithRepetition(r),
            )
        }
    }

// 6. All length permutations with repetition
// Creates permutations with repetition for all lengths from 1 to maxLength
// Example: [1,2].allPermutationsWithRepetition(2) → [[1], [2], [1,1], [1,2], [2,1], [2,2]]
fun <T> List<T>.allPermutationsWithRepetition(maxLength: Int): Stream<List<T>> =
    IntStream
        .rangeClosed(1, maxLength)
        .boxed()
        .flatMap { length -> permutationsWithRepetition(length) }

// 7. All length combinations with repetition
// Creates combinations with repetition for all lengths from 1 to maxLength
// Example: [1,2].allCombinationsWithRepetition(2) → [[1], [2], [1,1], [1,2], [2,2]]
fun <T> List<T>.allCombinationsWithRepetition(maxLength: Int): Stream<List<T>> =
    IntStream
        .rangeClosed(1, maxLength)
        .boxed()
        .flatMap { r -> combinationsWithRepetition(r) }

// 8. Cartesian product calculation helper function
// Selects one element from each of multiple lists to create all possible combinations
// Example: cartesianProduct([[1,2], [a,b]]) → [[1,a], [1,b], [2,a], [2,b]]
fun <T> List<List<T>>.cartesianProduct(): Stream<List<T>> {
    if (isEmpty()) return Stream.of(emptyList())
    if (size == 1) return this[0].stream().map { listOf(it) }

    val head = this[0]
    val tailLists = drop(1)

    return head.stream().flatMap { headItem ->
        tailLists.cartesianProduct().map { tailCombination ->
            listOf(headItem) + tailCombination
        }
    }
}

private val yamlMapper = ObjectMapper(YAMLFactory()).registerKotlinModule()

fun String.parseAsList(): List<Any?> = yamlMapper.readValue<List<Any?>>(this)

val Stream<*>.size
    get() = this.count().toInt()
