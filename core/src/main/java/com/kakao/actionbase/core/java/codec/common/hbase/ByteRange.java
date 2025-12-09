package com.kakao.actionbase.core.java.codec.common.hbase;

// simplified org.apache.hadoop.hbase.util.ByteRange;
public interface ByteRange {
  byte[] getBytes();

  ByteRange unset();

  ByteRange set(int var1);

  ByteRange set(byte[] var1);

  ByteRange set(byte[] var1, int var2, int var3);

  int getOffset();

  ByteRange setOffset(int var1);

  int getLength();

  ByteRange setLength(int var1);

  boolean isEmpty();

  byte get(int var1);

  short getShort(int var1);

  int getInt(int var1);

  long getLong(int var1);

  ByteRange get(int var1, byte[] var2);

  ByteRange get(int var1, byte[] var2, int var3, int var4);

  ByteRange put(int var1, byte var2);

  ByteRange putShort(int var1, short var2);

  ByteRange putInt(int var1, int var2);

  ByteRange putLong(int var1, long var2);

  ByteRange put(int var1, byte[] var2);

  ByteRange put(int var1, byte[] var2, int var3, int var4);
}
