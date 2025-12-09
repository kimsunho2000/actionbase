package com.kakao.actionbase.v2.core.code;

public class KeyValue<T> {

  T key;
  T value;

  public KeyValue(T key, T value) {
    this.key = key;
    this.value = value;
  }

  public T getKey() {
    return key;
  }

  public T getValue() {
    return value;
  }
}
