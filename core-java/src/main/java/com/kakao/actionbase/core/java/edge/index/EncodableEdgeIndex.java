package com.kakao.actionbase.core.java.edge.index;

import com.kakao.actionbase.core.java.annotation.AllowNulls;
import com.kakao.actionbase.core.java.edge.Edge;
import com.kakao.actionbase.core.java.edge.Edges;
import com.kakao.actionbase.core.java.metadata.v3.common.Direction;

import java.util.List;
import java.util.Map;

import org.immutables.value.Value;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@Value.Style(allParameters = true)
@JsonSerialize(as = ImmutableEncodableEdgeIndex.class)
@JsonDeserialize(as = ImmutableEncodableEdgeIndex.class)
public interface EncodableEdgeIndex extends Edge {

  @Override
  long version();

  @Override
  Object source();

  @Override
  Object target();

  @Value.Derived
  @Value.Auxiliary
  default Object directedSource() {
    if (direction() == Direction.OUT) {
      return source();
    } else {
      return target();
    }
  }

  @Value.Derived
  @Value.Auxiliary
  default Object directedTarget() {
    if (direction() == Direction.OUT) {
      return target();
    } else {
      return source();
    }
  }

  @Override
  @AllowNulls
  @Value.Default
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  default Map<String, Object> properties() {
    return Edges.EMPTY_PROPS;
  }

  int tableCode();

  int indexCode();

  Direction direction();

  List<EncodableIndexValue> indexValues();
}
