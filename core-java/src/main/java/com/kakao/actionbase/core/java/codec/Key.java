package com.kakao.actionbase.core.java.codec;

import org.immutables.value.Value;

@Value.Immutable
public interface Key<T> extends Encoded {

  @Value.Parameter
  T key();
}
