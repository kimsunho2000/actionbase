package com.kakao.actionbase.core.java.edge;

import com.kakao.actionbase.core.java.annotation.AllowNulls;
import com.kakao.actionbase.core.java.state.EventType;
import com.kakao.actionbase.core.java.state.base.EventCompanion;
import com.kakao.actionbase.core.java.types.TypeValidator;

import java.util.Map;

import org.immutables.value.Value;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableEdgeEventPayload.class)
@JsonDeserialize(as = ImmutableEdgeEventPayload.class)
public interface EdgeEventPayload extends Edge {

  EventType type();

  @Override
  @AllowNulls
  @Value.Default
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  default Map<String, Object> properties() {
    return Edges.EMPTY_PROPS;
  }

  @Value.Check
  default void check() {
    TypeValidator.validateValue(source());
    TypeValidator.validateValue(target());
    TypeValidator.validateProperties(properties());
  }

  default EdgeEvent toEvent() {
    return ImmutableEdgeEvent.builder()
        .version(version())
        .id(EventCompanion.generateRandomId())
        .type(type())
        .source(source())
        .target(target())
        .properties(properties())
        .build();
  }
}
