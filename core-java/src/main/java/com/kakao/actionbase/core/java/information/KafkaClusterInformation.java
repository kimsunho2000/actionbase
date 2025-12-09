package com.kakao.actionbase.core.java.information;

import java.util.Map;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableKafkaClusterInformation.class)
@JsonDeserialize(as = ImmutableKafkaClusterInformation.class)
public interface KafkaClusterInformation {

  @Value.Parameter
  String cluster();

  @Value.Parameter
  Map<String, String> configuration();
}
