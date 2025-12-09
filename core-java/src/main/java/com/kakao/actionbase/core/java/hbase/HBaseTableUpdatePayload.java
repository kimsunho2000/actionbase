package com.kakao.actionbase.core.java.hbase;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableHBaseTableUpdatePayload.class)
@JsonDeserialize(as = ImmutableHBaseTableUpdatePayload.class)
public interface HBaseTableUpdatePayload {
  boolean enable();
}
