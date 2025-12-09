package com.kakao.actionbase.core.java.compat.v2;

import com.kakao.actionbase.core.java.annotation.AllowNulls;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

public interface EdgeModel {

  long ts();

  Object src();

  Object tgt();

  @AllowNulls
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  Map<String, Object> props();
}
