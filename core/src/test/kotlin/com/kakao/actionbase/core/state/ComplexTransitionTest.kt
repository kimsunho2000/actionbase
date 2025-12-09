package com.kakao.actionbase.core.state

import com.kakao.actionbase.test.allCombinations
import com.kakao.actionbase.test.allPermutationsWithRepetition
import com.kakao.actionbase.test.cartesianProduct
import com.kakao.actionbase.test.permutations
import com.kakao.actionbase.test.state.StateTestFixture
import com.kakao.actionbase.test.toEventType

import java.util.stream.Stream

import kotlin.streams.toList
import kotlin.test.assertEquals

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

/**
 * Class that tests complex scenarios of state transitions
 * Validates state consistency through permutations of event sequences
 */
class ComplexTransitionTest {
    /**
     * Verifies that all permutations of event sequences produce the same final state
     * Confirms that state transitions guarantee consistent results regardless of order
     */
    @ParameterizedTest(name = "{index}. {0}")
    @MethodSource("eventSequenceProvider")
    fun `test complex transition`(
        testName: String,
        eventSequence: List<Event>,
    ) {
        with(StateTestFixture) {
            // given: Generate all permutations of event sequence
            val permutedSequences = eventSequence.permutations().toList()

            // when: Execute state transition for each permutation
            val finalStates =
                permutedSequences.map { sequence ->
                    sequence.fold(State.initial) { currentState, event ->
                        currentState.transit(event, fields)
                    }
                }

            // then: Final state of all permutations must be identical
            val expectedFinalState = finalStates.first()
            finalStates.forEach { actualFinalState ->
                assertEquals(expectedFinalState, actualFinalState)
            }
        }
    }

    companion object {
        private val EVENT_TYPES = listOf('I', 'U', 'D')
        private const val SEQUENCE_LENGTH = 3

        /**
         * Generates event sequences for testing
         * Creates all possible event combinations and field combinations to provide comprehensive testing
         */
        @JvmStatic
        fun eventSequenceProvider(): Stream<Arguments> =
            generateCompleteEventSequences(SEQUENCE_LENGTH)
                .map { eventSequence ->
                    Arguments.of(eventSequence.createTestName(), eventSequence)
                }

        /**
         * Generates all event sequence combinations of specified length
         */
        private fun generateCompleteEventSequences(length: Int): Stream<List<Event>> =
            EVENT_TYPES
                .allPermutationsWithRepetition(length)
                .filter { it.size > 1 } // Only allow sequences with at least 2 event types
                .flatMap { eventTypeSequence ->
                    generateEventCombinationsForSequence(eventTypeSequence)
                }

        /**
         * Generates all event combinations for a specific event type sequence
         */
        private fun generateEventCombinationsForSequence(eventTypeSequence: List<Char>): Stream<List<Event>> {
            val eventCombinationsPerPosition =
                eventTypeSequence.mapIndexed { index, eventTypeChar ->
                    val version = calculateVersionForPosition(index)
                    val eventType = eventTypeChar.toEventType()
                    generateEventsForType(eventType, version)
                }

            return eventCombinationsPerPosition.cartesianProduct()
        }

        /**
         * Calculates version per position
         */
        private fun calculateVersionForPosition(index: Int): Long = 10L * (index + 1)

        /**
         * Generates all possible event cases per event type
         */
        private fun generateEventsForType(
            eventType: EventType,
            version: Long,
        ): List<Event> =
            when (eventType) {
                EventType.INSERT -> createInsertEventVariations(version)
                EventType.UPDATE -> createUpdateEventVariations(version)
                EventType.DELETE -> createDeleteEventVariations(version)
            }

        /**
         * Generates all variations of INSERT events
         * All combinations of required fields (NAME, AGE) + optional fields (EMAIL, COMMENT)
         */
        private fun createInsertEventVariations(version: Long): List<Event> =
            with(StateTestFixture) {
                val requiredFields =
                    listOf(
                        NAME_KEY to "name_I_$version",
                        AGE_KEY to version,
                    )

                val optionalFieldCombinations =
                    listOf(
                        emptyList(), // Required fields only
                        listOf(EMAIL_KEY to "email_I_$version"), // + EMAIL
                        listOf(COMMENT_KEY to "comment_I_$version"), // + COMMENT
                        listOf( // + EMAIL, COMMENT
                            EMAIL_KEY to "email_I_$version",
                            COMMENT_KEY to "comment_I_$version",
                        ),
                    )

                optionalFieldCombinations.map { optionalFields ->
                    val allFields = requiredFields + optionalFields
                    Event.create(EventType.INSERT, version, *allFields.toTypedArray())
                }
            }

        /**
         * Generates all variations of UPDATE events
         * All non-empty combinations of fields
         */
        private fun createUpdateEventVariations(version: Long): List<Event> =
            with(StateTestFixture) {
                val updatableFields =
                    listOf(
                        NAME_KEY to "name_U_$version",
                        AGE_KEY to version + 1000,
                        EMAIL_KEY to "email_U_$version",
                        COMMENT_KEY to "comment_U_$version",
                    )

                updatableFields
                    .allCombinations()
                    .filter { it.isNotEmpty() } // Exclude empty field combinations
                    .map { fieldCombination ->
                        Event.create(EventType.UPDATE, version, *fieldCombination.toTypedArray())
                    }.toList()
            }

        /**
         * Creates DELETE events (no fields)
         */
        private fun createDeleteEventVariations(version: Long): List<Event> =
            with(StateTestFixture) {
                listOf(Event.create(EventType.DELETE, version))
            }

        /**
         * Generates summary information for event sequence
         * Concise representation for test identification
         */
        private fun List<Event>.createTestName(): String =
            joinToString(separator = "; ") { event ->
                val propertyInitials =
                    event.properties
                        .map { it.key.first().uppercase() }
                        .joinToString("")
                "${event.type.name.first()}${event.version}$propertyInitials"
            }
    }
}
