package com.kakao.actionbase.core.java.codec;

import com.kakao.actionbase.core.java.annotation.AllowNulls;
import com.kakao.actionbase.core.java.annotation.Nullable;
import com.kakao.actionbase.core.java.edge.EdgeState;
import com.kakao.actionbase.core.java.edge.ImmutableEdgeState;
import com.kakao.actionbase.core.java.state.StateValue;
import com.kakao.actionbase.core.java.types.StructType;

import java.util.Map;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableEdgeStateNoSchema.class)
@JsonDeserialize(as = ImmutableEdgeStateNoSchema.class)
public interface EdgeStateNoSchema {

  boolean active();

  @Value.Default
  default long version() {
    return Long.MIN_VALUE;
  }

  Object source();

  Object target();

  @Nullable
  Long createdAt();

  @Nullable
  Long deletedAt();

  @AllowNulls
  Map<String, StateValue> properties();

  default EdgeState toEdgeState(StructType schema) {
    return ImmutableEdgeState.builder()
        .active(active())
        .version(version())
        .source(source())
        .target(target())
        .createdAt(createdAt())
        .deletedAt(deletedAt())
        .properties(properties())
        .build();
  }
}
