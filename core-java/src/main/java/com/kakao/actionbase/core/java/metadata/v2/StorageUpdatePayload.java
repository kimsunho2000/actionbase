package com.kakao.actionbase.core.java.metadata.v2;

import com.kakao.actionbase.core.java.metadata.v2.common.StorageType;

import java.util.Map;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@Value.Style(allParameters = true)
@JsonSerialize(as = ImmutableStorageUpdatePayload.class)
@JsonDeserialize(as = ImmutableStorageUpdatePayload.class)
public interface StorageUpdatePayload {

  String desc();

  StorageType type();

  Map<String, String> conf();

  Boolean active();
}
