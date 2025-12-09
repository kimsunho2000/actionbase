package com.kakao.actionbase.core.java.metadata.v3;

import com.kakao.actionbase.core.java.ApiVersion;
import com.kakao.actionbase.core.java.edge.EdgePayload;
import com.kakao.actionbase.core.java.edge.EdgeState;
import com.kakao.actionbase.core.java.metadata.ImmutableTenantId;
import com.kakao.actionbase.core.java.metadata.TenantId;

import org.immutables.value.Value;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableTenantDescriptor.class)
@JsonDeserialize(as = ImmutableTenantDescriptor.class)
public interface TenantDescriptor extends Descriptor<TenantId> {

  String DEFAULT_SOURCE = "origin";

  @JsonIgnore
  @Value.Derived
  default String origin() {
    return DEFAULT_SOURCE;
  }

  ApiVersion apiVersion();

  @Override
  @JsonIgnore
  @Value.Derived
  @Value.Auxiliary
  default TenantId id() {
    return ImmutableTenantId.of(tenant());
  }

  @Override
  @JsonIgnore
  @Value.Auxiliary
  default EdgePayload toEdgePayload() {
    return Tenants.toEdgePayload(this);
  }

  @Override
  @JsonIgnore
  @Value.Auxiliary
  default EdgeState toEdgeState() {
    return Tenants.toEdgeState(this);
  }
}
