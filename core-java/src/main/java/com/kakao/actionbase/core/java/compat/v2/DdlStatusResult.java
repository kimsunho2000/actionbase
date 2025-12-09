package com.kakao.actionbase.core.java.compat.v2;

import com.kakao.actionbase.core.java.annotation.Nullable;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(as = ImmutableDdlStatusResult.class)
@JsonDeserialize(as = ImmutableDdlStatusResult.class)
public interface DdlStatusResult<EntityResult> {
  String status();

  @Nullable
  EntityResult result();

  @Nullable
  String message();

  default EntityResult get() {
    return result();
  }
}
