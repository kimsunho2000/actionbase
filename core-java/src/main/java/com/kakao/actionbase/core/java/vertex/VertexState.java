package com.kakao.actionbase.core.java.vertex;

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
@JsonSerialize(as = ImmutableVertexState.class)
@JsonDeserialize(as = ImmutableVertexState.class)
public interface VertexState extends TransitableState<VertexEvent, VertexState>, VertexModel {

  @Override
  boolean active();

  @Override
  @Value.Default
  default long version() {
    return Long.MIN_VALUE;
  }

  @Override
  Object key();

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
  default VertexPayload toPayload() {
    return VerticeCompanion.toPayload(this);
  }

  @Override
  default VertexState transit(VertexEvent event, StructType schema) {
    throw new UnsupportedOperationException("VertexState transit method not implemented");
  }
}
