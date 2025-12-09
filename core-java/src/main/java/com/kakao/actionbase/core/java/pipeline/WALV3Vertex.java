package com.kakao.actionbase.core.java.pipeline;

import com.kakao.actionbase.core.java.vertex.Vertex;
import com.kakao.actionbase.core.java.vertex.VertexEvent;

import org.immutables.value.Value;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableWALV3Vertex.class)
@JsonDeserialize(as = ImmutableWALV3Vertex.class)
public interface WALV3Vertex extends WALV3<VertexEvent> {
  @Override
  @Value.Derived
  @Value.Auxiliary
  default String type() {
    return MessageConstants.VERTEX_TYPE;
  }

  @Override
  VertexEvent event();

  @Override
  @JsonIgnore
  @Value.Auxiliary
  default byte[] key() {
    Vertex vertex = event();
    String partitionKey = vertex.key().toString();
    return partitionKey.getBytes();
  }
}
