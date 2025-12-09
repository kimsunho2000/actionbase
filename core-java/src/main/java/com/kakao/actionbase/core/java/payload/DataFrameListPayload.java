package com.kakao.actionbase.core.java.payload;

import com.kakao.actionbase.core.java.annotation.AllowNulls;

import java.util.List;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableDataFrameListPayload.class)
@JsonDeserialize(as = ImmutableDataFrameListPayload.class)
public abstract class DataFrameListPayload extends DataFramePayload {

  @AllowNulls
  public abstract List<List<Object>> rows();

  @Value.Derived
  public int count() {
    return rows().size();
  }
}
