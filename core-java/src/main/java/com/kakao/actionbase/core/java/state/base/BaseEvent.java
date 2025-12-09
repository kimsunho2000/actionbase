package com.kakao.actionbase.core.java.state.base;

import com.kakao.actionbase.core.java.annotation.AllowNulls;
import com.kakao.actionbase.core.java.annotation.Nullable;
import com.kakao.actionbase.core.java.state.Event;
import com.kakao.actionbase.core.java.state.EventType;

import java.util.Map;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableBaseEvent.class)
@JsonDeserialize(as = ImmutableBaseEvent.class)
public interface BaseEvent extends Event {

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
  long version();

  @Override
  @AllowNulls
  Map<String, Object> properties();

  @Value.Check
  default void check() {
    if (type() == EventType.DELETE && !properties().isEmpty()) {
      throw new IllegalArgumentException("fields must be empty when operation is DELETE");
    }
  }
}
