package com.kakao.actionbase.core.java.vertex;

import com.kakao.actionbase.core.java.annotation.AllowNulls;
import com.kakao.actionbase.core.java.payload.DataFrameVertexPayload;
import com.kakao.actionbase.core.java.payload.ImmutableDataFrameVertexPayload;
import com.kakao.actionbase.core.java.state.EventType;
import com.kakao.actionbase.core.java.types.TypeValidator;

import java.util.Map;

import org.immutables.value.Value;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableVertexPayload.class)
@JsonDeserialize(as = ImmutableVertexPayload.class)
public interface VertexPayload extends Vertex {

  @Override
  Object key();

  @Override
  @AllowNulls
  @Value.Default
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  default Map<String, Object> properties() {
    return VerticeCompanion.EMPTY_PROPS;
  }

  @Value.Check
  default void check() {
    TypeValidator.validateValue(key());
    TypeValidator.validateProperties(properties());
  }

  default VertexEvent toEvent(EventType type) {
    return VerticeCompanion.toEvent(this, type);
  }

  default VertexEvent toEvent(EventType type, long version) {
    return VerticeCompanion.toEvent(this, type, version);
  }

  default VertexEvent toEvent(EventType type, long version, String externalId) {
    return VerticeCompanion.toEvent(this, type, version, externalId);
  }

  default DataFrameVertexPayload toDataFrameVertexPayload() {
    return ImmutableDataFrameVertexPayload.builder().addVertices(this).build();
  }
}
