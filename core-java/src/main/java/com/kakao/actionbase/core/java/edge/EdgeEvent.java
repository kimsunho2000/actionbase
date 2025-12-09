package com.kakao.actionbase.core.java.edge;

import com.kakao.actionbase.core.java.annotation.AllowNulls;
import com.kakao.actionbase.core.java.annotation.Nullable;
import com.kakao.actionbase.core.java.state.Event;
import com.kakao.actionbase.core.java.state.EventType;
import com.kakao.actionbase.core.java.types.StructType;

import java.util.Map;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableEdgeEvent.class)
@JsonDeserialize(as = ImmutableEdgeEvent.class)
public interface EdgeEvent extends Edge, Event {

  @Override
  EventType type();

  @Override
  long version();

  @Override
  Object source();

  @Override
  Object target();

  @Override
  @AllowNulls
  Map<String, Object> properties();

  @Nullable
  @Value.Default
  default String requestId() {
    return null;
  }

  @Override
  String id();

  default EdgeState toState(StructType schema) {
    return Edges.toState(this, schema);
  }
}
