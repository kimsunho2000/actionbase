package com.kakao.actionbase.core.java.metadata.v3;

import com.kakao.actionbase.core.java.edge.EdgePayload;
import com.kakao.actionbase.core.java.edge.EdgeState;
import com.kakao.actionbase.core.java.jackson.ActionbaseObjectMapper;
import com.kakao.actionbase.core.java.metadata.DatastoreId;
import com.kakao.actionbase.core.java.metadata.ImmutableDatastoreId;
import com.kakao.actionbase.core.java.metadata.ImmutableStorageId;
import com.kakao.actionbase.core.java.metadata.StorageId;
import com.kakao.actionbase.core.java.metadata.v3.common.DatastoreType;

import java.util.Map;

import org.immutables.value.Value;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableStorageDescriptor.class)
@JsonDeserialize(as = ImmutableStorageDescriptor.class)
public interface StorageDescriptor extends Descriptor<StorageId> {

  DatastoreType type(); // TODO: Remove unnecessary field

  String database();

  String storage();

  String datastore();

  Map<String, Object> configuration();

  @Override
  @JsonIgnore
  @Value.Derived
  @Value.Auxiliary
  default StorageId id() {
    return ImmutableStorageId.of(tenant(), database(), storage());
  }

  @JsonIgnore
  @Value.Auxiliary
  default DatastoreId datastoreId() {
    return ImmutableDatastoreId.of(tenant(), datastore());
  }

  @Override
  @JsonIgnore
  @Value.Auxiliary
  default EdgeState toEdgeState() {
    return Storages.toEdgeState(this);
  }

  @Override
  @JsonIgnore
  @Value.Auxiliary
  default EdgePayload toEdgePayload() {
    return Storages.toEdgePayload(this);
  }

  @JsonIgnore
  @Value.Auxiliary
  default Map<String, Object> toMap() {
    return ActionbaseObjectMapper.toMap(this);
  }

  @JsonIgnore
  @Value.Auxiliary
  default StoragePayload toPayload() {
    return ImmutableStoragePayload.builder()
        .storage(storage())
        .type(type())
        .comment(comment())
        .datastore(datastore())
        .configuration(configuration())
        .build();
  }
}
