package com.kakao.actionbase.core.java.metadata.v3;

import com.kakao.actionbase.core.java.edge.EdgePayload;
import com.kakao.actionbase.core.java.edge.EdgeState;
import com.kakao.actionbase.core.java.jackson.ActionbaseObjectMapper;
import com.kakao.actionbase.core.java.metadata.DatastoreId;
import com.kakao.actionbase.core.java.metadata.ImmutableDatastoreId;
import com.kakao.actionbase.core.java.metadata.v3.common.DatastoreType;

import java.util.Map;

import org.immutables.value.Value;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableDatastoreDescriptor.class)
@JsonDeserialize(as = ImmutableDatastoreDescriptor.class)
public interface DatastoreDescriptor extends Descriptor<DatastoreId> {

  DatastoreType type();

  String datastore();

  Map<String, String> configuration();

  @Override
  @JsonIgnore
  @Value.Derived
  @Value.Auxiliary
  default DatastoreId id() {
    return ImmutableDatastoreId.of(tenant(), datastore());
  }

  @Override
  @JsonIgnore
  @Value.Auxiliary
  default EdgePayload toEdgePayload() {
    return Datastores.toEdgePayload(this);
  }

  @Override
  @JsonIgnore
  @Value.Auxiliary
  default EdgeState toEdgeState() {
    return Datastores.toEdgeState(this);
  }

  @JsonIgnore
  @Value.Auxiliary
  default Map<String, Object> toMap() {
    return ActionbaseObjectMapper.toMap(this);
  }
}
