package com.kakao.actionbase.v2.core.code;

public class VersionValue {

  public static long NO_VERSION = -1;
  final long version;
  final Object value;

  public VersionValue(long version, Object value) {
    this.version = version;
    this.value = value;
  }

  public static VersionValue noVersionValue(Object value) {
    return new VersionValue(NO_VERSION, value);
  }

  public boolean hasVersion() {
    return version != NO_VERSION;
  }

  public long getVersion() {
    return version;
  }

  public Object getValue() {
    return value;
  }

  public String toString() {
    return "(" + version + ", " + value + ")";
  }

  @Override
  public int hashCode() {
    int result = (int) (version ^ (version >>> 32));
    result = 31 * result + value.hashCode();
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    VersionValue that = (VersionValue) obj;
    return version == that.version && value.equals(that.value);
  }
}
