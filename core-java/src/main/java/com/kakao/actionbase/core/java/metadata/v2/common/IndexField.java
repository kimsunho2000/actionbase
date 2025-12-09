package com.kakao.actionbase.core.java.metadata.v2.common;

import com.kakao.actionbase.core.java.codec.common.hbase.Order;
import com.kakao.actionbase.core.java.compat.v2.EdgeField;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@Value.Style(allParameters = true)
@JsonSerialize(as = ImmutableIndexField.class)
@JsonDeserialize(as = ImmutableIndexField.class)
public interface IndexField {

  String name();

  Order order();

  default com.kakao.actionbase.core.java.metadata.v3.common.IndexField toV3() {
    String v3IndexFieldName = EdgeField.convertV2ToV3FieldName(name());
    return com.kakao.actionbase.core.java.metadata.v3.common.ImmutableIndexField.of(
        v3IndexFieldName, order());
  }
}
