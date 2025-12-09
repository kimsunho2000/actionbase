package com.kakao.actionbase.core.java.time;

import java.time.Instant;

public class TimestampUtil {

  public static long getCurrentTimeNanos() {
    Instant now = Instant.now();
    return now.getEpochSecond() * 1_000_000_000L + now.getNano();
  }
}
