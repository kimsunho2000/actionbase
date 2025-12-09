package com.kakao.actionbase.v2.core.metadata;

import java.util.HashMap;
import java.util.Map;

public enum Direction {
  // A --> B
  OUT((byte) 2),

  // A <-- B
  IN((byte) 3),
  ;

  private static final Map<String, Direction> NAME_TO_VALUE_MAP = new HashMap<>();
  private static final Map<Byte, Direction> CODE_TO_VALUE_MAP = new HashMap<>();

  static {
    for (Direction direction : Direction.values()) {
      NAME_TO_VALUE_MAP.put(direction.name(), direction);
      CODE_TO_VALUE_MAP.put(direction.code, direction);
    }
  }

  private final byte code;

  Direction(byte code) {
    this.code = code;
  }

  public static Direction of(String value) {
    return NAME_TO_VALUE_MAP.get(value.toUpperCase());
  }

  public static Direction of(byte code) {
    return CODE_TO_VALUE_MAP.get(code);
  }

  public byte getCode() {
    return code;
  }
}
