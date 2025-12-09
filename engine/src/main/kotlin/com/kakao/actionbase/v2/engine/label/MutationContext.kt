package com.kakao.actionbase.v2.engine.label

import com.kakao.actionbase.v2.core.code.HashEdgeValue
import com.kakao.actionbase.v2.core.metadata.Active
import com.kakao.actionbase.v2.core.metadata.EdgeOperation
import com.kakao.actionbase.v2.engine.util.getLogger

import kotlin.math.max

data class MutationContext(
    val active: Active,
    val expectedResult: EdgeOperationStatus,
    val acc: Long,
    val insertTs: Long?,
    val deleteTs: Long?,
) {
    companion object {
        val logger = getLogger()

        /**
         * new = oldTs <= ts
         * old = otherwise
         * newI(nsert) = deletedTs == null || ts >= deletedTs
         * newD(elete) = insertTs == null || ts >= insertTs
         *
         * | oldState  | op  | new/old    | newI/D  | current   | status   | acc |
         * |-----------|-----|------------|---------|-----------|----------|-----|
         * | INITIAL   | I   | N/A        |         | ACTIVE    | CREATED  | 1   |
         * | INITIAL   | U   | N/A        |         | INACTIVE  | IDLE     | 0   |
         * | INITIAL   | D   | N/A        |         | INACTIVE  | IDLE     | 0   |
         * | ACTIVE    | I   | new/old    |         | ACTIVE    | UPDATED  | 0   |
         * | ACTIVE    | U   | new/old    |         | ACTIVE    | UPDATED  | 0   |
         * | ACTIVE    | D   | new        |         | INACTIVE  | DELETED  | -1  |
         * | ACTIVE    | D   | old        | newD    | INACTIVE  | DELETED  | -1  |
         * | ACTIVE    | D   | old        | oldD    | ACTIVE    | IDLE     | 0   |
         * | INACTIVE  | I   | new        |         | ACTIVE    | CREATED  | 1   |
         * | INACTIVE  | I   | old        | newI    | ACTIVE    | CREATED  | 1   |
         * | INACTIVE  | I   | old        | oldI    | INACTIVE  | IDLE     | 0   |
         * | INACTIVE  | U   | new/old    |         | INACTIVE  | IDLE     | 0   |
         * | INACTIVE  | D   | new/old    |         | INACTIVE  | IDLE     | 0   |
         */
        fun handleInitial(
            ts: Long,
            op: EdgeOperation,
        ): MutationContext =
            when (op) {
                EdgeOperation.INSERT -> {
                    logger.debug(
                        "op: {}, ts: {} - transition from INITIAL to ACTIVE, insertTs: {}, deleteTs: null",
                        op,
                        ts,
                        ts,
                    )
                    MutationContext(Active.ACTIVE, EdgeOperationStatus.CREATED, 1, ts, null)
                }
                EdgeOperation.UPDATE -> {
                    logger.debug(
                        "op: {}, ts: {} - transition from INITIAL to INACTIVE, insertTs: null, deleteTs: null",
                        op,
                        ts,
                    )
                    MutationContext(Active.INACTIVE, EdgeOperationStatus.IDLE, 0, null, null)
                }
                EdgeOperation.DELETE -> {
                    logger.debug(
                        "op: {}, ts: {} - transition from INITIAL to INACTIVE, insertTs: null, deleteTs: {}",
                        op,
                        ts,
                        ts,
                    )
                    MutationContext(Active.INACTIVE, EdgeOperationStatus.IDLE, 0, null, ts)
                }
                else -> {
                    error("invalid operation $op on the initial state")
                }
            }

        @Suppress("LongMethod")
        fun handleActive(
            op: EdgeOperation,
            oldTs: Long,
            ts: Long,
            insertTs: Long?,
            deleteTs: Long?,
        ): MutationContext {
            val isNew = oldTs <= ts
            return when (op) {
                EdgeOperation.INSERT -> {
                    val maxInsertTs = max(insertTs ?: Long.MIN_VALUE, ts)
                    logger.debug(
                        "op: {}, oldTs: {}, ts: {}, insertTs: {}, deleteTs: {} - transition from ACTIVE to ACTIVE, isNew: {}, insertTs: {}, deleteTs: {}",
                        op,
                        oldTs,
                        ts,
                        insertTs,
                        deleteTs,
                        isNew,
                        maxInsertTs,
                        deleteTs,
                    )
                    MutationContext(Active.ACTIVE, EdgeOperationStatus.UPDATED, 0, maxInsertTs, deleteTs)
                }
                EdgeOperation.UPDATE -> {
                    logger.debug(
                        "op: {}, oldTs: {}, ts: {}, insertTs: {}, deleteTs: {} - transition from ACTIVE to ACTIVE, isNew: {}, insertTs: {}, deleteTs: {}",
                        op,
                        oldTs,
                        ts,
                        insertTs,
                        deleteTs,
                        isNew,
                        insertTs,
                        deleteTs,
                    )
                    MutationContext(Active.ACTIVE, EdgeOperationStatus.UPDATED, 0, insertTs, deleteTs)
                }
                EdgeOperation.DELETE -> {
                    val isNewDelete = insertTs == null || ts >= insertTs
                    val maxDeleteTs = max(deleteTs ?: Long.MIN_VALUE, ts)
                    if (isNew || isNewDelete) {
                        logger.debug(
                            "op: {}, oldTs: {}, ts: {}, insertTd: {}, deleteTs: {} - transition from ACTIVE to INACTIVE, isNew: {}, isNewDelete: {}, insertTs: {}, deleteTs: {}",
                            op,
                            oldTs,
                            ts,
                            insertTs,
                            deleteTs,
                            isNew,
                            isNewDelete,
                            insertTs,
                            maxDeleteTs,
                        )
                        MutationContext(Active.INACTIVE, EdgeOperationStatus.DELETED, -1, insertTs, maxDeleteTs)
                    } else {
                        logger.debug(
                            "op: {}, oldTs: {}, ts: {}, insertTd: {}, deleteTs: {} - transition from ACTIVE to ACTIVE, isNew: {}, isNewDelete: {}, insertTs: {}, deleteTs: {}",
                            op,
                            oldTs,
                            ts,
                            insertTs,
                            deleteTs,
                            isNew,
                            isNewDelete,
                            insertTs,
                            maxDeleteTs,
                        )
                        MutationContext(Active.ACTIVE, EdgeOperationStatus.IDLE, 0, insertTs, maxDeleteTs)
                    }
                }
                else -> {
                    error("invalid operation $op on the active state")
                }
            }
        }

        @Suppress("LongMethod")
        fun handleInactive(
            op: EdgeOperation,
            oldTs: Long,
            ts: Long,
            insertTs: Long?,
            deleteTs: Long?,
        ): MutationContext {
            val isNew = oldTs <= ts
            return when (op) {
                EdgeOperation.INSERT -> {
                    val isNewInsert = deleteTs == null || ts >= deleteTs
                    val maxInsertTs = max(insertTs ?: Long.MIN_VALUE, ts)
                    if (isNew || isNewInsert) {
                        logger.debug(
                            "op: {}, oldTs: {}, ts: {}, insertTs: {}, deleteTs: {} - transition from INACTIVE to ACTIVE, isNew: {}, isNewInsert: {}, insertTs: {}, deleteTs: {}",
                            op,
                            oldTs,
                            ts,
                            insertTs,
                            deleteTs,
                            isNew,
                            isNewInsert,
                            maxInsertTs,
                            deleteTs,
                        )
                        MutationContext(Active.ACTIVE, EdgeOperationStatus.CREATED, 1, maxInsertTs, deleteTs)
                    } else {
                        logger.debug(
                            "op: {}, oldTs: {}, ts: {}, insertTs: {}, deleteTs: {} - transition from INACTIVE to INACTIVE, isNew: {}, isNewInsert: {}, insertTs: {}, deleteTs: {}",
                            op,
                            oldTs,
                            ts,
                            insertTs,
                            deleteTs,
                            isNew,
                            isNewInsert,
                            maxInsertTs,
                            deleteTs,
                        )
                        MutationContext(Active.INACTIVE, EdgeOperationStatus.IDLE, 0, maxInsertTs, deleteTs)
                    }
                }
                EdgeOperation.UPDATE -> {
                    logger.debug(
                        "op: {}, oldTs: {}, ts: {}, insertTs: {}, deleteTs: {} - transition from INACTIVE to INACTIVE, isNew: {}, insertTs: {}, deleteTs: {}",
                        op,
                        oldTs,
                        ts,
                        insertTs,
                        deleteTs,
                        isNew,
                        insertTs,
                        deleteTs,
                    )
                    MutationContext(Active.INACTIVE, EdgeOperationStatus.IDLE, 0, insertTs, deleteTs)
                }
                EdgeOperation.DELETE -> {
                    val maxDeleteTs = max(deleteTs ?: Long.MIN_VALUE, ts)
                    logger.debug(
                        "op: {}, oldTs: {}, ts: {}, insertTs: {}, deleteTs: {} - transition from INACTIVE to INACTIVE, isNew: {}, insertTs: {}, deleteTs: {}",
                        op,
                        oldTs,
                        ts,
                        insertTs,
                        deleteTs,
                        isNew,
                        insertTs,
                        maxDeleteTs,
                    )
                    MutationContext(Active.INACTIVE, EdgeOperationStatus.IDLE, 0, insertTs, maxDeleteTs)
                }
                else -> {
                    error("invalid operation $op on the inactive state")
                }
            }
        }

        fun ifExists(
            ts: Long,
            op: EdgeOperation,
            oldTs: Long,
            oldActive: Active,
            insertTs: Long?,
            deleteTs: Long?,
        ): MutationContext =
            when (oldActive) {
                Active.ACTIVE -> handleActive(op, oldTs, ts, insertTs, deleteTs)
                Active.INACTIVE -> handleInactive(op, oldTs, ts, insertTs, deleteTs)
            }

        fun ifNotExists(
            ts: Long,
            op: EdgeOperation,
        ): MutationContext = handleInitial(ts, op)

        @Suppress("CyclomaticComplexMethod")
        fun update(
            mutationContext: MutationContext,
            oldValue: HashEdgeValue?,
            newValue: HashEdgeValue,
        ): MutationContext {
            val newMutationContext =
                if (oldValue == null) {
                    when (newValue.active) {
                        Active.ACTIVE ->
                            mutationContext.copy(
                                expectedResult = EdgeOperationStatus.CREATED,
                                acc = 1,
                            )
                        Active.INACTIVE ->
                            mutationContext.copy(
                                expectedResult = EdgeOperationStatus.IDLE,
                                acc = 0,
                            )
                        null -> error("Invalid state transition from null to ${newValue.active}")
                    }
                } else {
                    when {
                        // ACTIVE -> ACTIVE
                        oldValue.active == Active.ACTIVE && newValue.active == Active.ACTIVE ->
                            mutationContext.copy(
                                expectedResult = EdgeOperationStatus.UPDATED,
                                acc = 0,
                            )
                        // ACTIVE -> INACTIVE
                        oldValue.active == Active.ACTIVE && newValue.active == Active.INACTIVE ->
                            mutationContext.copy(
                                expectedResult = EdgeOperationStatus.DELETED,
                                acc = -1,
                            )

                        // INACTIVE -> ACTIVE
                        oldValue.active == Active.INACTIVE && newValue.active == Active.ACTIVE ->
                            mutationContext.copy(
                                expectedResult = EdgeOperationStatus.CREATED,
                                acc = 1,
                            )

                        // INACTIVE -> INACTIVE
                        oldValue.active == Active.INACTIVE && newValue.active == Active.INACTIVE ->
                            mutationContext.copy(
                                expectedResult = EdgeOperationStatus.IDLE,
                                acc = 0,
                            )

                        else -> error("Invalid state transition from ${oldValue.active} to ${newValue.active}")
                    }
                }

            return when {
                // Change IDLE to UPDATED if the edge is actually updated and active
                newMutationContext.active == Active.ACTIVE &&
                    newMutationContext.expectedResult == EdgeOperationStatus.IDLE &&
                    oldValue != newValue -> {
                    newMutationContext.copy(expectedResult = EdgeOperationStatus.UPDATED)
                }
                // Change UPDATED to IDLE if the edge is not actually updated
                newMutationContext.expectedResult == EdgeOperationStatus.UPDATED &&
                    oldValue == newValue -> {
                    newMutationContext.copy(expectedResult = EdgeOperationStatus.IDLE)
                }
                else -> newMutationContext
            }
        }
    }
}
