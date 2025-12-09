package com.kakao.actionbase.core.java.pipeline;

import com.kakao.actionbase.core.java.annotation.Nullable;
import com.kakao.actionbase.core.java.compat.v2.EdgeState;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableCDCV2.class)
@JsonDeserialize(as = ImmutableCDCV2.class)
public interface CDCV2 extends CDC, MessageV2 {

  String status();

  @Nullable
  EdgeState before();

  @Nullable
  EdgeState after();

  long acc();

  @Nullable
  String message();
}
