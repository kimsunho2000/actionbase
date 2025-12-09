package com.kakao.actionbase.core.java.metadata.v2;

import com.kakao.actionbase.core.java.metadata.TenantId;
import com.kakao.actionbase.core.java.metadata.v3.DatabaseDescriptor;
import com.kakao.actionbase.core.java.metadata.v3.ImmutableDatabaseDescriptor;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@Value.Style(allParameters = true)
@JsonSerialize(as = ImmutableServiceDescriptor.class)
@JsonDeserialize(as = ImmutableServiceDescriptor.class)
public interface ServiceDescriptor extends Descriptor<DatabaseDescriptor> {

  boolean active();

  String name();

  String desc();

  @Value.Derived
  @Value.Auxiliary
  default Identifier identifier() {
    return ImmutableIdentifier.builder().service("origin").identifier(name()).build();
  }

  @Override
  default DatabaseDescriptor toV3(TenantId tenant) {
    return ImmutableDatabaseDescriptor.builder()
        .active(active())
        .tenant(tenant.tenant())
        .database(name())
        .comment(desc())
        .build();
  }
}
