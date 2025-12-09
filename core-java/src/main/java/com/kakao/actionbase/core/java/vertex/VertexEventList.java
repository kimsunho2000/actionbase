package com.kakao.actionbase.core.java.vertex;

import java.util.List;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableVertexEventList.class)
@JsonDeserialize(as = ImmutableVertexEventList.class)
public interface VertexEventList {
  List<VertexEvent> vertices();
}
