package com.kakao.actionbase.core.java.information;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableKafkaMessageInformation.class)
@JsonDeserialize(as = ImmutableKafkaMessageInformation.class)
public interface KafkaMessageInformation {

  @Value.Parameter
  String cluster();

  @Value.Parameter
  String topic();
}
