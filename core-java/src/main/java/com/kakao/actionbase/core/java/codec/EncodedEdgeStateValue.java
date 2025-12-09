package com.kakao.actionbase.core.java.codec;

import com.kakao.actionbase.core.java.annotation.AllowNulls;
import com.kakao.actionbase.core.java.annotation.Nullable;
import com.kakao.actionbase.core.java.edge.EdgeState;
import com.kakao.actionbase.core.java.edge.ImmutableEdgeState;
import com.kakao.actionbase.core.java.state.StateValue;
import com.kakao.actionbase.core.java.types.StructType;
import com.kakao.actionbase.core.java.util.HashUtils;

import java.util.HashMap;
import java.util.Map;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableEncodedEdgeStateValue.class)
@JsonDeserialize(as = ImmutableEncodedEdgeStateValue.class)
public interface EncodedEdgeStateValue {

  boolean active();

  long version();

  @Nullable
  Long createdAt();

  @Nullable
  Long deletedAt();

  @AllowNulls
  Map<Integer, StateValue> properties();

  default EdgeState toEdgeState(StructType schema, Object source, Object target) {
    Map<Integer, String> hashToFieldNameMap = schema.hashToFieldNameMap();
    Map<String, StateValue> newProperties = new HashMap<>();
    for (Map.Entry<Integer, StateValue> entry : properties().entrySet()) {
      String fieldName = hashToFieldNameMap.get(entry.getKey());
      if (fieldName == null) {
        fieldName = String.valueOf(entry.getKey());
      }
      newProperties.put(fieldName, entry.getValue());
    }
    return ImmutableEdgeState.builder()
        .active(active())
        .version(version())
        .source(source)
        .target(target)
        .createdAt(createdAt())
        .deletedAt(deletedAt())
        .properties(newProperties)
        .build();
  }

  static EncodedEdgeStateValue fromEdgeState(EdgeState edgeState) {
    Map<Integer, StateValue> newProperties = new HashMap<>();
    for (Map.Entry<String, StateValue> entry : edgeState.properties().entrySet()) {
      newProperties.put(HashUtils.stringHash(entry.getKey()), entry.getValue());
    }
    return ImmutableEncodedEdgeStateValue.builder()
        .active(edgeState.active())
        .version(edgeState.version())
        .createdAt(edgeState.createdAt())
        .deletedAt(edgeState.deletedAt())
        .properties(newProperties)
        .build();
  }
}
