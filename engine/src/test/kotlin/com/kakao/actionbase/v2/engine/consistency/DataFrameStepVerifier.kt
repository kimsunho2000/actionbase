package com.kakao.actionbase.v2.engine.consistency

import com.kakao.actionbase.v2.engine.sql.DataFrame

import io.kotest.assertions.withClue
import io.kotest.matchers.shouldBe
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

class DataFrameStepVerifier(
    private val monos: Array<out Mono<DataFrame>>,
) {
    companion object {
        fun create(vararg monos: Mono<DataFrame>): DataFrameStepVerifier = DataFrameStepVerifier(monos)

        fun create(monos: List<Mono<DataFrame>>): DataFrameStepVerifier = DataFrameStepVerifier(monos.toTypedArray())
    }

    private var shouldCompareEqual: Boolean = false
    private var fieldNames: List<String> = emptyList()

    fun assertFieldsEqual(vararg fields: String): DataFrameStepVerifier {
        shouldCompareEqual = true
        this.fieldNames = fields.toList()
        return this
    }

    fun verifyComplete() {
        val verifier =
            StepVerifier.create(
                Flux
                    .concat(*monos)
                    .collectList()
                    .map { dataFrames ->
                        if (shouldCompareEqual) {
                            val ref = dataFrames[0]
                            val refFieldIndices = fieldNames.map { ref.schema.fieldIndex(it) }

                            for (i in 1 until dataFrames.size) {
                                val target = dataFrames[i]
                                val targetFieldIndices = fieldNames.map { target.schema.fieldIndex(it) }

                                ref.rows.zip(target.rows).forEach { (refRow, targetRow) ->
                                    for ((idx, refFieldIndex) in refFieldIndices.withIndex()) {
                                        val fieldName = fieldNames[idx]
                                        val targetFieldIndex = targetFieldIndices[idx]

                                        val refValue = refRow.array[refFieldIndex]
                                        val targetValue = targetRow.array[targetFieldIndex]

                                        withClue("Field '$fieldName' mismatch") {
                                            refValue shouldBe targetValue
                                        }
                                    }
                                }
                            }
                        }
                        dataFrames
                    },
            )

        verifier
            .expectNextCount(1)
            .verifyComplete()
    }
}
