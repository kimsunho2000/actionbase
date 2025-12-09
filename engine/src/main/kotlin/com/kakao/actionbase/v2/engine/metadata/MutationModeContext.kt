package com.kakao.actionbase.v2.engine.metadata

import com.kakao.actionbase.v2.core.metadata.MutationMode
import com.kakao.actionbase.v2.core.metadata.MutationMode.ASYNC
import com.kakao.actionbase.v2.core.metadata.MutationMode.IGNORE
import com.kakao.actionbase.v2.core.metadata.MutationMode.SYNC

data class MutationModeContext(
    val l: MutationMode, // label
    val r: MutationMode?, // request
    val queue: Boolean,
) {
    companion object {
        /**
         * | label  | request | queue   |
         * | ------ | ------- | ------- |
         * | ASYNC  | N/A     | true    |
         * | ASYNC  | SYNC    | false   |
         * | ASYNC  | ASYNC   | true    |
         * | ASYNC  | IGNORE  | true    |
         * | SYNC   | N/A     | false   |
         * | SYNC   | SYNC    | false   |
         * | SYNC   | ASYNC   | true    |
         * | SYNC   | IGNORE  | true    |
         * | IGNORE | N/A     | true    |
         * | IGNORE | SYNC    | Invalid |
         * | IGNORE | ASYNC   | true    |
         * | IGNORE | IGNORE  | true    |
         */
        @Suppress("CyclomaticComplexMethod")
        fun of(
            label: MutationMode,
            request: MutationMode?,
        ): MutationModeContext {
            val queue =
                when (label) {
                    ASYNC ->
                        when (request) {
                            null -> true
                            SYNC -> false
                            ASYNC -> true
                            IGNORE -> true
                        }

                    SYNC ->
                        when (request) {
                            null -> false
                            SYNC -> false
                            ASYNC -> true
                            IGNORE -> true
                        }

                    IGNORE ->
                        when (request) {
                            null -> true
                            SYNC -> throw IllegalArgumentException("Async is not allowed in IGNORE mode.")
                            ASYNC -> true
                            IGNORE -> true
                        }
                }
            return MutationModeContext(label, request, queue)
        }
    }
}
