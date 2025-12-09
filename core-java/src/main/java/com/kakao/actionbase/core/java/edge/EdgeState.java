package com.kakao.actionbase.core.java.edge;

import com.kakao.actionbase.core.java.annotation.AllowNulls;
import com.kakao.actionbase.core.java.annotation.Nullable;
import com.kakao.actionbase.core.java.state.StateValue;
import com.kakao.actionbase.core.java.state.TransitableState;
import com.kakao.actionbase.core.java.types.StructType;

import java.util.Map;

import org.immutables.value.Value;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableEdgeState.class)
@JsonDeserialize(as = ImmutableEdgeState.class)
public interface EdgeState extends TransitableState<EdgeEvent, EdgeState>, EdgeModel {

  @Override
  boolean active();

  @Override
  @Value.Default
  default long version() {
    return Long.MIN_VALUE;
  }

  @Override
  Object source();

  @Override
  Object target();

  @Override
  @Nullable
  Long createdAt();

  @Override
  @Nullable
  Long deletedAt();

  @Override
  @AllowNulls
  Map<String, StateValue> properties();

  @JsonIgnore
  @Value.Auxiliary
  default EdgePayload toPayload() {
    return Edges.toPayload(this);
  }

  @Override
  default EdgeState transit(EdgeEvent event, StructType schema) {
    return Edges.transit(this, event, schema);
  }
}
