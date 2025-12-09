package com.kakao.actionbase.core.java.codec.common;

import org.immutables.value.Value;

@Value.Immutable
@Value.Style(allParameters = true)
public interface LockEdgeValue {

  long version();

  default boolean isStale(long current, long timeout) {
    return (current - version()) > timeout;
  }

  default boolean isStale(long timeout) {
    return isStale(System.currentTimeMillis(), timeout);
  }
}
