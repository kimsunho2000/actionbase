package com.kakao.actionbase.core.java.pipeline;

import org.immutables.value.Value;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface Message {

  String version();

  @JsonIgnore
  @Value.Auxiliary
  byte[] key();

  @JsonIgnore
  @Value.Auxiliary
  default byte[] value() throws JsonProcessingException {
    return MessageConstants.objectMapper.writeValueAsBytes(this);
  }
}
