package com.kakao.actionbase.test.util

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object TimestampUtil {
    private const val DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss"
    private val DEFAULT_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_DATE_PATTERN)
    private val DEFAULT_ZONE_ID = ZoneId.of("GMT")

    fun stringToTimestamp(dateString: String): Long {
        val localDateTime = LocalDateTime.parse(dateString, DEFAULT_FORMATTER)
        return toTimestamp(localDateTime)
    }

    fun toTimestamp(
        localDateTime: LocalDateTime,
        zoneId: ZoneId = DEFAULT_ZONE_ID,
    ): Long = localDateTime.atZone(zoneId).toInstant().toEpochMilli()

    fun formatDateString(
        dateString: String,
        pattern: String = DEFAULT_DATE_PATTERN,
        format: String,
        zoneId: ZoneId = DEFAULT_ZONE_ID,
    ): String {
        val formatter = DateTimeFormatter.ofPattern(pattern)
        val localDateTime = LocalDateTime.parse(dateString, formatter)
        return localDateTime.format(DateTimeFormatter.ofPattern(format).withZone(zoneId))
    }

    fun formatInstant(
        instant: Instant,
        pattern: String,
        zoneId: ZoneId = DEFAULT_ZONE_ID,
    ): String {
        val formatter = DateTimeFormatter.ofPattern(pattern)
        return instant
            .atZone(zoneId)
            .format(formatter)
    }
}
