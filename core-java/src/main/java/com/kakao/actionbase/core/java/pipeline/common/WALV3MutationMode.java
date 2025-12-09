package com.kakao.actionbase.core.java.pipeline.common;

import com.kakao.actionbase.core.java.annotation.Nullable;
import com.kakao.actionbase.core.java.metadata.v3.common.MutationMode;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableWALV3MutationMode.class)
@JsonDeserialize(as = ImmutableWALV3MutationMode.class)
public interface WALV3MutationMode {

  MutationMode table();

  @Nullable
  MutationMode request();

  boolean queue();
}
