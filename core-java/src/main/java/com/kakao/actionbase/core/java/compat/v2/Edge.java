package com.kakao.actionbase.core.java.compat.v2;

import com.kakao.actionbase.core.java.state.EventType;
import com.kakao.actionbase.core.java.state.base.EventCompanion;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableEdge.class)
@JsonDeserialize(as = ImmutableEdge.class)
public interface Edge extends EdgeModel {

  default com.kakao.actionbase.core.java.edge.EdgeEvent toV3Event(EventType type) {
    return com.kakao.actionbase.core.java.edge.ImmutableEdgeEvent.builder()
        .version(ts())
        .id(EventCompanion.generateRandomId())
        .type(type)
        .source(src())
        .target(tgt())
        .properties(props())
        .build();
  }
}
