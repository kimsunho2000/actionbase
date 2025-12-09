package com.kakao.actionbase.core.java.compat.v2;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableStatItem.class)
@JsonDeserialize(as = ImmutableStatItem.class)
public interface StatItem {

  @Value.Parameter
  String name();

  @Value.Parameter
  long value();
}
