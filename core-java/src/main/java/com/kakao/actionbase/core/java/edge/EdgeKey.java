package com.kakao.actionbase.core.java.edge;

import org.immutables.value.Value;

@Value.Immutable
public interface EdgeKey {

  @Value.Parameter
  Object source();

  @Value.Parameter
  Object target();
}
