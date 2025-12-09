package com.kakao.actionbase.core.java.payload;

import com.kakao.actionbase.core.java.annotation.AllowNulls;
import com.kakao.actionbase.core.java.metadata.v3.common.Direction;

import java.util.Collections;
import java.util.Map;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableEdgeCountPayload.class)
@JsonDeserialize(as = ImmutableEdgeCountPayload.class)
public interface EdgeCountPayload {

  @Value.Parameter
  Object start();

  @Value.Parameter
  Direction direction();

  @Value.Parameter
  long count();

  @AllowNulls
  @Value.Default
  default Map<String, Object> context() {
    return Collections.emptyMap();
  }
}
