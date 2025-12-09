package com.kakao.actionbase.core.java.metadata.v2.common;

import com.kakao.actionbase.core.java.annotation.Nullable;

import java.util.List;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@Value.Style(allParameters = true)
@JsonSerialize(as = ImmutableTopNCompositeKey.class)
@JsonDeserialize(as = ImmutableTopNCompositeKey.class)
public interface TopNCompositeKey {

  List<String> fields();

  @Nullable
  String desc();
}
