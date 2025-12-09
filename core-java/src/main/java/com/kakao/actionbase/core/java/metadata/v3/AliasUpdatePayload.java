package com.kakao.actionbase.core.java.metadata.v3;

import com.kakao.actionbase.core.java.annotation.Nullable;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableAliasUpdatePayload.class)
@JsonDeserialize(as = ImmutableAliasUpdatePayload.class)
public interface AliasUpdatePayload {

  @Nullable
  String table();

  @Nullable
  String comment();

  @Value.Check
  default void check() {
    if (table() == null && comment() == null) {
      throw new IllegalArgumentException("Either table or comment must be provided");
    }
  }
}
