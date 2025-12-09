package com.kakao.actionbase.core.java.pipeline.common;

import com.kakao.actionbase.core.java.annotation.Nullable;
import com.kakao.actionbase.core.java.context.RequestContext;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableV3Context.class)
@JsonDeserialize(as = ImmutableV3Context.class)
public interface V3Context {
  String tenant();

  String database();

  @Nullable
  String alias();

  String table();

  RequestContext request();
}
