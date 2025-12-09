package com.kakao.actionbase.v2.core.code;

public class LockEdgeValue {

  final long ts;

  public LockEdgeValue(long ts) {
    this.ts = ts;
  }

  public long getTs() {
    return ts;
  }

  public boolean isStale(long current, long timeout) {
    return (current - ts) > timeout;
  }

  public boolean isStale(long timeout) {
    return isStale(System.currentTimeMillis(), timeout);
  }
}
