package com.kakao.actionbase.core.java.information;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableV2CompatInformation.class)
@JsonDeserialize(as = ImmutableV2CompatInformation.class)
public interface V2CompatInformation {

  String phase();

  KafkaMessageInformation wal();

  KafkaMessageInformation cdc();
}
