package com.kakao.actionbase.core.java.compat.v2;

import com.kakao.actionbase.core.java.state.EventType;

import java.util.List;
import java.util.stream.Collectors;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableEdgeEventList.class)
@JsonDeserialize(as = ImmutableEdgeEventList.class)
public interface EdgeEventList {

  List<Edge> edges();

  default List<com.kakao.actionbase.core.java.edge.EdgeEvent> toV3EdgeEvents(EventType type) {
    return edges().stream().map(edge -> edge.toV3Event(type)).collect(Collectors.toList());
  }
}
