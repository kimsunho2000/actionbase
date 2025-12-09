package com.kakao.actionbase.core.java.edge;

import java.util.List;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableEdgeEventPayloadList.class)
@JsonDeserialize(as = ImmutableEdgeEventPayloadList.class)
public interface EdgeEventPayloadList {
  List<EdgeEventPayload> edges();
}
