package com.kakao.actionbase.core.java.information;

import java.util.List;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutablePipelineInformation.class)
@JsonDeserialize(as = ImmutablePipelineInformation.class)
public interface PipelineInformation {

  List<KafkaClusterInformation> clusters();

  KafkaMessageInformation wal();

  KafkaMessageInformation cdc();
}
