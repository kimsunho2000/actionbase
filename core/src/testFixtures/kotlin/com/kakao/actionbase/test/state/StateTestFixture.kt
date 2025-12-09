package com.kakao.actionbase.test.state

import com.kakao.actionbase.core.state.Schema
import com.kakao.actionbase.core.state.State
import com.kakao.actionbase.test.handleSpecialValue
import com.kakao.actionbase.test.toBooleanFlexible

import kotlin.test.assertEquals

object StateTestFixture {
    const val NAME_KEY = "name"
    const val AGE_KEY = "age"
    const val EMAIL_KEY = "email"
    const val COMMENT_KEY = "comment"

    val fields =
        Schema(
            mapOf(
                NAME_KEY to false,
                AGE_KEY to false,
                EMAIL_KEY to true,
                COMMENT_KEY to true,
            ),
        )

    fun assertAll(
        actualState: State,
        expectedActive: String?,
        expectedVersion: Long?,
        expectedCreatedAt: Long?,
        expectedDeletedAt: Long?,
        expectedName: Any?,
        expectedAge: Any?,
        expectedEmail: Any?,
        expectedComment: Any?,
        expectedNameVersion: Long?,
        expectedAgeVersion: Long?,
        expectedEmailVersion: Long?,
        expectedCommentVersion: Long?,
        expectedPropertiesSize: Int?,
    ) {
        // then
        assertEquals(expectedActive?.toBooleanFlexible(), actualState.active, "Active state must match.")
        assertEquals(expectedVersion, actualState.version, "Version must match.")
        assertEquals(expectedCreatedAt, actualState.createdAt, "Creation time must match.")
        assertEquals(expectedDeletedAt, actualState.deletedAt, "Deletion time must match.")
        assertEquals(expectedName?.handleSpecialValue(), actualState.properties[NAME_KEY]?.value, "Name property must match.")
        assertEquals(expectedAge?.handleSpecialValue { toString().toInt() }, actualState.properties[AGE_KEY]?.value, "Age property must match.")
        assertEquals(expectedEmail?.handleSpecialValue(), actualState.properties[EMAIL_KEY]?.value, "Email property must match.")
        assertEquals(expectedComment?.handleSpecialValue(), actualState.properties[COMMENT_KEY]?.value, "Comment property must match.")
        assertEquals(
            expectedNameVersion,
            actualState.properties[NAME_KEY]?.version,
            "Name property version must match.",
        )
        assertEquals(
            expectedAgeVersion,
            actualState.properties[AGE_KEY]?.version,
            "Age property version must match.",
        )
        assertEquals(
            expectedEmailVersion,
            actualState.properties[EMAIL_KEY]?.version,
            "Email property version must match.",
        )
        assertEquals(
            expectedCommentVersion,
            actualState.properties[COMMENT_KEY]?.version,
            "Comment property version must match.",
        )
        assertEquals(expectedPropertiesSize, actualState.properties.size, "Property size must match.")
    }
}
