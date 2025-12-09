package com.kakao.actionbase.core.java.payload;

import com.kakao.actionbase.core.java.annotation.AllowNulls;
import com.kakao.actionbase.core.java.edge.EdgePayload;

import java.util.List;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableDataFrameEdgePayload.class)
@JsonDeserialize(as = ImmutableDataFrameEdgePayload.class)
public abstract class DataFrameEdgePayload extends DataFramePayload {

  @AllowNulls
  public abstract List<EdgePayload> edges();

  @Value.Derived
  public int count() {
    return edges().size();
  }

  public DataFrameEdgePayload union(DataFrameEdgePayload other) {
    return ImmutableDataFrameEdgePayload.builder()
        .addAllEdges(edges())
        .addAllEdges(other.edges())
        .build();
  }
}
