package com.kakao.actionbase.core.java.metadata.v3;

import com.kakao.actionbase.core.java.metadata.v3.common.DatastoreType;

import java.util.Map;

import org.immutables.value.Value;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableStoragePayload.class)
@JsonDeserialize(as = ImmutableStoragePayload.class)
public interface StoragePayload {

  String storage();

  DatastoreType type();

  String comment();

  String datastore();

  // in case of HBase,
  // namespace, table are required.
  Map<String, Object> configuration();

  @JsonIgnore
  @Value.Auxiliary
  default StorageDescriptor toStorageDescriptor(String tenant, String database) {
    return ImmutableStorageDescriptor.builder()
        .updatedAt(System.currentTimeMillis())
        .tenant(tenant)
        .database(database)
        .type(type())
        .storage(storage())
        .datastore(datastore())
        .configuration(configuration())
        .comment(comment())
        .build();
  }
}
