package com.kakao.actionbase.core.java.edge;

import com.kakao.actionbase.core.java.annotation.AllowNulls;
import com.kakao.actionbase.core.java.payload.DataFrameEdgePayload;
import com.kakao.actionbase.core.java.payload.ImmutableDataFrameEdgePayload;
import com.kakao.actionbase.core.java.state.EventType;
import com.kakao.actionbase.core.java.types.TypeValidator;

import java.util.Collections;
import java.util.Map;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableEdgePayload.class)
@JsonDeserialize(as = ImmutableEdgePayload.class)
public interface EdgePayload extends Edge {

  @Override
  long version();

  @Override
  Object source();

  @Override
  Object target();

  @Override
  @AllowNulls
  @Value.Default
  default Map<String, Object> properties() {
    return Edges.EMPTY_PROPS;
  }

  @AllowNulls
  @Value.Default
  default Map<String, Object> context() {
    return Collections.emptyMap();
  }

  @Value.Check
  default void check() {
    TypeValidator.validateValue(source());
    TypeValidator.validateValue(target());
    TypeValidator.validateProperties(properties());
  }

  default EdgeEvent toEvent(EventType type) {
    return Edges.toEvent(this, type);
  }

  default EdgeEventPayload toEventPayload(EventType type) {
    return ImmutableEdgeEventPayload.builder()
        .type(type)
        .version(version())
        .source(source())
        .target(target())
        .properties(properties())
        .build();
  }

  default EdgeEvent toEvent(EventType type, String externalId) {
    return Edges.toEvent(this, type, externalId);
  }

  default DataFrameEdgePayload toDataFrameEdgePayload() {
    return ImmutableDataFrameEdgePayload.builder().addEdges(this).build();
  }
}
