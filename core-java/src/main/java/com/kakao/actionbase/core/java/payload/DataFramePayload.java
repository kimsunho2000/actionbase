package com.kakao.actionbase.core.java.payload;

import com.kakao.actionbase.core.java.annotation.AllowNulls;
import com.kakao.actionbase.core.java.annotation.Nullable;

import java.util.Collections;
import java.util.Map;

import org.immutables.value.Value;

public abstract class DataFramePayload {

  @Value.Default
  public int total() {
    return -1;
  }

  @Value.Default
  @Nullable
  public String offset() {
    return null;
  }

  @Value.Default
  public boolean hasNext() {
    return false;
  }

  @AllowNulls
  @Value.Default
  public Map<String, Object> context() {
    return Collections.emptyMap();
  }
}
