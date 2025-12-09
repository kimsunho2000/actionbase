package com.kakao.actionbase.core.java.payload;

import com.kakao.actionbase.core.java.annotation.AllowNulls;
import com.kakao.actionbase.core.java.vertex.VertexPayload;

import java.util.List;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableDataFrameVertexPayload.class)
@JsonDeserialize(as = ImmutableDataFrameVertexPayload.class)
public abstract class DataFrameVertexPayload extends DataFramePayload {

  @AllowNulls
  public abstract List<VertexPayload> vertices();

  @Value.Derived
  public int count() {
    return vertices().size();
  }

  public DataFrameVertexPayload union(DataFrameVertexPayload other) {
    return ImmutableDataFrameVertexPayload.builder()
        .addAllVertices(vertices())
        .addAllVertices(other.vertices())
        .build();
  }
}
