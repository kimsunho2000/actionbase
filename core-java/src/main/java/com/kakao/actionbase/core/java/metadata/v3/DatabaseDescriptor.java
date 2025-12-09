package com.kakao.actionbase.core.java.metadata.v3;

import com.kakao.actionbase.core.java.edge.EdgePayload;
import com.kakao.actionbase.core.java.edge.EdgeState;
import com.kakao.actionbase.core.java.jackson.ActionbaseObjectMapper;
import com.kakao.actionbase.core.java.metadata.DatabaseId;
import com.kakao.actionbase.core.java.metadata.ImmutableDatabaseId;

import java.util.Map;

import org.immutables.value.Value;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableDatabaseDescriptor.class)
@JsonDeserialize(as = ImmutableDatabaseDescriptor.class)
public interface DatabaseDescriptor extends Descriptor<DatabaseId> {

  String database();

  @Override
  @JsonIgnore
  @Value.Derived
  @Value.Auxiliary
  default DatabaseId id() {
    return ImmutableDatabaseId.of(tenant(), database());
  }

  @Override
  @JsonIgnore
  @Value.Auxiliary
  default EdgePayload toEdgePayload() {
    return Databases.toEdgePayload(this);
  }

  @Override
  @JsonIgnore
  @Value.Auxiliary
  default EdgeState toEdgeState() {
    return Databases.toEdgeState(this);
  }

  @JsonIgnore
  @Value.Auxiliary
  default Map<String, Object> toMap() {
    return ActionbaseObjectMapper.toMap(this);
  }

  @JsonIgnore
  @Value.Auxiliary
  default DatabasePayload toPayload() {
    return ImmutableDatabasePayload.builder().database(database()).comment(comment()).build();
  }
}
