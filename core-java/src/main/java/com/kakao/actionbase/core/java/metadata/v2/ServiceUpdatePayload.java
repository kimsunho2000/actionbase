package com.kakao.actionbase.core.java.metadata.v2;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@Value.Style(allParameters = true)
@JsonSerialize(as = ImmutableServiceUpdatePayload.class)
@JsonDeserialize(as = ImmutableServiceUpdatePayload.class)
public interface ServiceUpdatePayload {
  Boolean active();

  String desc();
}
