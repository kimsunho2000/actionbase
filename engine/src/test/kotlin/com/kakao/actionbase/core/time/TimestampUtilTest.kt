package com.kakao.actionbase.core.time

import com.kakao.actionbase.core.java.time.TimestampUtil

import kotlin.test.Test
import kotlin.test.assertTrue

class TimestampUtilTest {
    @Test
    fun testPrecision() {
        // val nanoPart = TimestampUtil.getCurrentTimeNanos() % 1_000
        val microPart = TimestampUtil.getCurrentTimeNanos() % 1_000_000
        val millisPart = TimestampUtil.getCurrentTimeNanos() % 1_000_000_000

        val microPart2 =
            if (microPart > 0L) {
                microPart
            } else {
                TimestampUtil.getCurrentTimeNanos() % 1_000_000
            }

        val millisPart2 =
            if (millisPart > 0L) {
                millisPart
            } else {
                TimestampUtil.getCurrentTimeNanos() % 1_000_000_000
            }

        assertTrue(microPart2 != 0L)
        assertTrue(millisPart2 != 0L)
    }
}
