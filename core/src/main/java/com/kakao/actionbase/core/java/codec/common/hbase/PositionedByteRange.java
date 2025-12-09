package com.kakao.actionbase.core.java.codec.common.hbase;

public interface PositionedByteRange extends ByteRange {
  int getPosition();

  PositionedByteRange setPosition(int var1);

  int getRemaining();

  byte peek();

  byte get();

  short getShort();

  int getInt();

  long getLong();

  PositionedByteRange get(byte[] var1);

  PositionedByteRange get(byte[] var1, int var2, int var3);

  PositionedByteRange put(byte var1);

  PositionedByteRange putShort(short var1);

  PositionedByteRange putInt(int var1);

  PositionedByteRange putLong(long var1);

  PositionedByteRange put(byte[] var1);

  PositionedByteRange put(byte[] var1, int var2, int var3);

  int getLimit();

  PositionedByteRange setLimit(int var1);

  PositionedByteRange unset();

  PositionedByteRange set(int var1);

  PositionedByteRange set(byte[] var1);

  PositionedByteRange set(byte[] var1, int var2, int var3);

  PositionedByteRange setOffset(int var1);

  PositionedByteRange setLength(int var1);

  PositionedByteRange get(int var1, byte[] var2);

  PositionedByteRange get(int var1, byte[] var2, int var3, int var4);

  PositionedByteRange put(int var1, byte var2);

  PositionedByteRange putShort(int var1, short var2);

  PositionedByteRange putInt(int var1, int var2);

  PositionedByteRange putLong(int var1, long var2);

  PositionedByteRange put(int var1, byte[] var2);

  PositionedByteRange put(int var1, byte[] var2, int var3, int var4);
}
