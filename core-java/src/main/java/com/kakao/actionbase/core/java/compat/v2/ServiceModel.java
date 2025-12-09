package com.kakao.actionbase.core.java.compat.v2;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableServiceModel.class)
@JsonDeserialize(as = ImmutableServiceModel.class)
public interface ServiceModel {
  boolean active();

  String name();

  String desc();
}
