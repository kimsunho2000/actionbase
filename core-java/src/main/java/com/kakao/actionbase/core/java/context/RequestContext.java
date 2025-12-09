package com.kakao.actionbase.core.java.context;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableRequestContext.class)
@JsonDeserialize(as = ImmutableRequestContext.class)
public interface RequestContext {

  @Value.Parameter
  String requestId();

  @Value.Parameter
  String actor();

  RequestContext DEFAULT = ImmutableRequestContext.of("", "default");
}
