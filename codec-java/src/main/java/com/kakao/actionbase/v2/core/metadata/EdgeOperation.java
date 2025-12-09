package com.kakao.actionbase.v2.core.metadata;

public enum EdgeOperation {
  INSERT, // exists then raise error else insert
  UPDATE, // exists then update else no-op
  DELETE, // exists then delete else no-op
  PURGE // exists then purge else no-op
}
