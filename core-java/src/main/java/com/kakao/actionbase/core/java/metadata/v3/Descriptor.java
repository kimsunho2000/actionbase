package com.kakao.actionbase.core.java.metadata.v3;

import com.kakao.actionbase.core.java.constant.Constant;
import com.kakao.actionbase.core.java.edge.EdgePayload;
import com.kakao.actionbase.core.java.edge.EdgeState;
import com.kakao.actionbase.core.java.metadata.Id;

import org.immutables.value.Value;

public interface Descriptor<ID extends Id> {

  ID id();

  @Value.Default
  default boolean active() {
    return true;
  }

  String tenant();

  @Value.Default
  default String comment() {
    return Constant.DEFAULT_COMMENT;
  }

  EdgePayload toEdgePayload();

  EdgeState toEdgeState();

  @Value.Default
  default long revision() {
    return Constant.DEFAULT_REVISION;
  }

  @Value.Default
  default long createdAt() {
    return Constant.DEFAULT_CREATED_AT;
  }

  @Value.Default
  default String createdBy() {
    return Constant.DEFAULT_CREATED_BY;
  }

  @Value.Default
  default long updatedAt() {
    return Constant.DEFAULT_UPDATED_AT;
  }

  @Value.Default
  default String updatedBy() {
    return Constant.DEFAULT_UPDATED_BY;
  }
}
