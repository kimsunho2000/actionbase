package com.kakao.actionbase.core.java.codec;

import org.immutables.value.Value;

@Value.Immutable
public interface KeyValue<T> extends Encoded {

  @Value.Parameter
  T key();

  @Value.Parameter
  T value();
}
