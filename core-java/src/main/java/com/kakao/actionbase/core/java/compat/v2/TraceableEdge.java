package com.kakao.actionbase.core.java.compat.v2;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableTraceableEdge.class)
@JsonDeserialize(as = ImmutableTraceableEdge.class)
public interface TraceableEdge extends EdgeModel {

  String traceId();
}
