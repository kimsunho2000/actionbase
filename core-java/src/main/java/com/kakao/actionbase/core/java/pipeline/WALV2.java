package com.kakao.actionbase.core.java.pipeline;

import com.kakao.actionbase.core.java.compat.v2.ImmutableMutationContext;
import com.kakao.actionbase.core.java.compat.v2.MutationContext;
import com.kakao.actionbase.core.java.metadata.v2.common.MutationMode;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableWALV2.class)
@JsonDeserialize(as = ImmutableWALV2.class)
public interface WALV2 extends WAL, MessageV2 {

  @Value.Default
  default MutationContext mode() {
    return ImmutableMutationContext.builder().l(MutationMode.SYNC).r(null).queue(false).build();
  }
}
