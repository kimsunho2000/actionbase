package com.kakao.actionbase.core.java.compat.v2;

import com.kakao.actionbase.core.java.annotation.Nullable;
import com.kakao.actionbase.core.java.metadata.v2.common.MutationMode;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableMutationContext.class)
@JsonDeserialize(as = ImmutableMutationContext.class)
public interface MutationContext {

  MutationMode l();

  @Nullable
  MutationMode r();

  boolean queue();
}
