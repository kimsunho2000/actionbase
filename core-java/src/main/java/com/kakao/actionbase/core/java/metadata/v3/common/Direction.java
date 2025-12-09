package com.kakao.actionbase.core.java.metadata.v3.common;

public enum Direction {
  // A --> B
  OUT((byte) 2),

  // A <-- B
  IN((byte) 3);

  private final byte code;

  Direction(byte code) {
    this.code = code;
  }

  public byte getCode() {
    return code;
  }

  public static Direction fromCode(byte code) {
    for (Direction direction : values()) {
      if (direction.code == code) {
        return direction;
      }
    }
    throw new IllegalArgumentException("Invalid code: " + code);
  }
}
