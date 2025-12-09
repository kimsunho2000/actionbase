package com.kakao.actionbase.core.java.pipeline;

import com.kakao.actionbase.core.java.annotation.Nullable;
import com.kakao.actionbase.core.java.vertex.Vertex;
import com.kakao.actionbase.core.java.vertex.VertexEvent;
import com.kakao.actionbase.core.java.vertex.VertexState;

import java.util.List;

import org.immutables.value.Value;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableCDCV3Vertex.class)
@JsonDeserialize(as = ImmutableCDCV3Vertex.class)
public interface CDCV3Vertex extends CDCV3<VertexEvent, VertexState> {
  @Override
  @Value.Derived
  @Value.Auxiliary
  default String type() {
    return MessageConstants.VERTEX_TYPE;
  }

  @Override
  List<VertexEvent> events();

  @Nullable
  VertexState before();

  @Nullable
  VertexState after();

  @Override
  @JsonIgnore
  @Value.Auxiliary
  default byte[] key() {
    Vertex vertex = events().get(1);
    String partitionKey = vertex.key().toString();
    return partitionKey.getBytes();
  }

  @Value.Check
  default void check() {
    if (events().isEmpty()) {
      throw new IllegalArgumentException("Events list cannot be empty");
    }
  }
}
