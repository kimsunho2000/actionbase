package com.kakao.actionbase.core.java.metadata;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@Value.Style(allParameters = true)
@JsonSerialize(as = ImmutableKafkaId.class)
@JsonDeserialize(as = ImmutableKafkaId.class)
public interface KafkaId extends Id {

  String tenant();

  String kafka();
}
