package com.kakao.actionbase.core.java.metadata.v2.common;

import com.kakao.actionbase.core.java.annotation.Nullable;
import com.kakao.actionbase.core.java.types.common.Field;
import com.kakao.actionbase.core.java.types.common.ImmutableField;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@Value.Style(allParameters = true)
@JsonSerialize(as = ImmutableVertexField.class)
@JsonDeserialize(as = ImmutableVertexField.class)
public interface VertexField {

  VertexType type();

  @Nullable
  String desc();

  default Field toField() {
    ImmutableField.Builder builder = ImmutableField.builder().type(type().getType());
    if (desc() != null) {
      builder.comment(desc());
    }
    return builder.build();
  }
}
