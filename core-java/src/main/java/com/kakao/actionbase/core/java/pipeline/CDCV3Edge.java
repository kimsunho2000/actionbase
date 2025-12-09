package com.kakao.actionbase.core.java.pipeline;

import com.kakao.actionbase.core.java.annotation.Nullable;
import com.kakao.actionbase.core.java.edge.Edge;
import com.kakao.actionbase.core.java.edge.EdgeEvent;
import com.kakao.actionbase.core.java.edge.EdgeState;

import java.util.List;

import org.immutables.value.Value;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableCDCV3Edge.class)
@JsonDeserialize(as = ImmutableCDCV3Edge.class)
public interface CDCV3Edge extends CDCV3<EdgeEvent, EdgeState> {
  @Override
  @Value.Derived
  @Value.Auxiliary
  default String type() {
    return MessageConstants.EDGE_TYPE;
  }

  @Override
  List<EdgeEvent> events();

  @Nullable
  EdgeState before();

  @Nullable
  EdgeState after();

  @Override
  @JsonIgnore
  @Value.Auxiliary
  default byte[] key() {
    Edge edge = events().get(0);
    String partitionKey = edge.source() + ":" + edge.target();
    return partitionKey.getBytes();
  }

  @Value.Check
  default void check() {
    if (events().isEmpty()) {
      throw new IllegalArgumentException("Events list cannot be empty");
    }
  }
}
