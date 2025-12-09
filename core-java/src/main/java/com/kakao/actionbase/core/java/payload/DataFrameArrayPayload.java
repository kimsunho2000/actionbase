package com.kakao.actionbase.core.java.payload;

import com.kakao.actionbase.core.java.annotation.AllowNulls;

import java.util.List;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableDataFrameArrayPayload.class)
@JsonDeserialize(as = ImmutableDataFrameArrayPayload.class)
public abstract class DataFrameArrayPayload extends DataFramePayload {

  @AllowNulls
  public abstract List<Object[]> rows();

  @Value.Derived
  public int count() {
    return rows().size();
  }
}
