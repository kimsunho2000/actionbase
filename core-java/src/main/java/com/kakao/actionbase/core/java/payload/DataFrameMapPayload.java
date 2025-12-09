package com.kakao.actionbase.core.java.payload;

import com.kakao.actionbase.core.java.annotation.AllowNulls;

import java.util.List;
import java.util.Map;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableDataFrameMapPayload.class)
@JsonDeserialize(as = ImmutableDataFrameMapPayload.class)
public abstract class DataFrameMapPayload extends DataFramePayload {

  @AllowNulls
  public abstract List<Map<String, Object>> data();

  @Value.Derived
  public int count() {
    return data().size();
  }
}
