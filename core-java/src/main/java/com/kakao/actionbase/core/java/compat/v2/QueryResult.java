package com.kakao.actionbase.core.java.compat.v2;

import com.kakao.actionbase.core.java.annotation.Nullable;
import com.kakao.actionbase.core.java.edge.EdgePayload;
import com.kakao.actionbase.core.java.metadata.v3.common.Direction;
import com.kakao.actionbase.core.java.payload.DataFrameEdgePayload;
import com.kakao.actionbase.core.java.state.SpecialStateValue;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableQueryResult.class)
@JsonDeserialize(as = ImmutableQueryResult.class)
public interface QueryResult {

  List<Map<String, Object>> data();

  @Value.Derived
  default long rows() {
    return data().size();
  }

  @Value.Default
  default List<StatItem> stats() {
    return Collections.emptyList();
  }

  @Nullable
  @Value.Default
  default String offset() {
    return null;
  }

  @Value.Default
  default boolean hasNext() {
    return false;
  }

  static QueryResult fromV3DataFrameEdgePayload(
      DataFrameEdgePayload payload, List<StatItem> stats, Direction direction) {
    ImmutableQueryResult.Builder builder = ImmutableQueryResult.builder();

    for (EdgePayload edge : payload.edges()) {
      Map<String, Object> item = new HashMap<>();
      item.put("dir", direction.name());
      item.put("ts", edge.version());
      if (direction == Direction.OUT) {
        item.put("src", edge.source());
        item.put("tgt", edge.target());
      } else {
        // flip for v2
        item.put("src", edge.target());
        item.put("tgt", edge.source());
      }
      for (Map.Entry<String, Object> entry : edge.properties().entrySet()) {
        Object value = entry.getValue();
        if (SpecialStateValue.isSpecialStateValue(value)) {
          item.put(entry.getKey(), null);
        } else {
          item.put(entry.getKey(), value);
        }
      }
      builder.addData(item);
    }
    builder.stats(stats);
    builder.hasNext(payload.hasNext());
    builder.offset(payload.offset());
    return builder.build();
  }

  static QueryResult fromV3EdgeState(
      com.kakao.actionbase.core.java.edge.EdgeState state,
      boolean includeInactive,
      String edgeId,
      List<StatItem> stats,
      Direction direction) {
    ImmutableQueryResult.Builder builder = ImmutableQueryResult.builder();
    Map<String, Object> item = new HashMap<>();
    if (state.active() || includeInactive) {
      if (includeInactive) {
        item.put("active", state.active());
        item.put("edgeId", edgeId);
      }
      item.put("dir", direction.name());
      item.put("ts", state.version());
      item.put("src", state.source());
      item.put("tgt", state.target());
      for (Map.Entry<String, Object> entry : state.toPayload().properties().entrySet()) {
        Object value = entry.getValue();
        if (SpecialStateValue.isSpecialStateValue(value)) {
          item.put(entry.getKey(), null);
        } else {
          item.put(entry.getKey(), value);
        }
      }
      builder.addData(item);
    }
    builder.stats(stats);
    return builder.build();
  }

  static QueryResult of(List<Map<String, Object>> values, List<StatItem> stats) {
    ImmutableQueryResult.Builder builder = ImmutableQueryResult.builder();
    builder.addAllData(values);
    builder.stats(stats);
    return builder.build();
  }
}
