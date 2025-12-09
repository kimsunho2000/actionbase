package com.kakao.actionbase.core.java.metadata.v3;

import com.kakao.actionbase.core.java.metadata.v3.common.DatastoreType;

import java.util.Map;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableStorageUpdatePayload.class)
@JsonDeserialize(as = ImmutableStorageUpdatePayload.class)
public interface StorageUpdatePayload {

  Boolean active();

  DatastoreType type();

  String comment();

  String datastore();

  Map<String, Object> configuration();
}
