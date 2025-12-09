package com.kakao.actionbase.core.java.metadata.v3;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableDatabasePayload.class)
@JsonDeserialize(as = ImmutableDatabasePayload.class)
public interface DatabasePayload {

  String database();

  String comment();

  default DatabaseDescriptor toDatabaseDescriptor(String tenant) {
    return ImmutableDatabaseDescriptor.builder()
        .updatedAt(System.currentTimeMillis())
        .tenant(tenant)
        .database(database())
        .comment(comment())
        .build();
  }
}
