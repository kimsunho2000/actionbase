package com.kakao.actionbase.core.java.vertex;

import com.kakao.actionbase.core.java.annotation.AllowNulls;
import com.kakao.actionbase.core.java.annotation.Nullable;
import com.kakao.actionbase.core.java.state.Event;
import com.kakao.actionbase.core.java.state.EventType;
import com.kakao.actionbase.core.java.state.base.EventCompanion;
import com.kakao.actionbase.core.java.types.StructType;

import java.util.Map;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableVertexEvent.class)
@JsonDeserialize(as = ImmutableVertexEvent.class)
public interface VertexEvent extends Vertex, Event {

  @Override
  @Value.Default
  default String id() {
    return EventCompanion.generateRandomId();
  }

  @Nullable
  @Value.Default
  default String requestId() {
    return null;
  }

  @Override
  EventType type();

  @Override
  @Value.Default
  default long version() {
    return System.currentTimeMillis();
  }

  @Override
  Object key();

  @Override
  @AllowNulls
  Map<String, Object> properties();

  default VertexState toState(StructType schema) {
    return VerticeCompanion.toState(this, schema);
  }
}
