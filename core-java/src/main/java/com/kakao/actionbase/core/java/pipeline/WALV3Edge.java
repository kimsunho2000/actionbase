package com.kakao.actionbase.core.java.pipeline;

import com.kakao.actionbase.core.java.edge.Edge;
import com.kakao.actionbase.core.java.edge.EdgeEvent;

import org.immutables.value.Value;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableWALV3Edge.class)
@JsonDeserialize(as = ImmutableWALV3Edge.class)
public interface WALV3Edge extends WALV3<EdgeEvent> {
  @Override
  @Value.Derived
  @Value.Auxiliary
  default String type() {
    return MessageConstants.EDGE_TYPE;
  }

  @Override
  EdgeEvent event();

  @Override
  @JsonIgnore
  @Value.Auxiliary
  default byte[] key() {
    Edge edge = event();
    String partitionKey = edge.source() + ":" + edge.target();
    return partitionKey.getBytes();
  }
}
