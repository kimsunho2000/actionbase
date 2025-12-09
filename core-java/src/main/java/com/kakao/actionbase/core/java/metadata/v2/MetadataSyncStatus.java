package com.kakao.actionbase.core.java.metadata.v2;

import java.util.List;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@Value.Style(allParameters = true)
@JsonSerialize(as = ImmutableMetadataSyncStatus.class)
@JsonDeserialize(as = ImmutableMetadataSyncStatus.class)
public interface MetadataSyncStatus {
  List<HostMetadataSyncStatus> data();
}
