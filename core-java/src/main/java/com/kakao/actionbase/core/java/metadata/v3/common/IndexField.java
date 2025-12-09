package com.kakao.actionbase.core.java.metadata.v3.common;

import com.kakao.actionbase.core.java.codec.common.hbase.Order;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@Value.Style(allParameters = true)
@JsonSerialize(as = ImmutableIndexField.class)
@JsonDeserialize(as = ImmutableIndexField.class)
public interface IndexField {

  String field();

  Order order();
}
