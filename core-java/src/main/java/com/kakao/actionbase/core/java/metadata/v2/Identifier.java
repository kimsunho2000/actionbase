package com.kakao.actionbase.core.java.metadata.v2;

import org.immutables.value.Value;

@Value.Immutable
public interface Identifier {

  String service();

  String identifier();

  static Identifier of(String value) {
    String[] parts = value.split("\\.");
    if (parts.length < 2) {
      throw new IllegalArgumentException(
          "Invalid identifier format: " + value + ". Format must be <service>.<identifier>.");
    }
    return ImmutableIdentifier.builder().service(parts[0]).identifier(parts[1]).build();
  }
}
