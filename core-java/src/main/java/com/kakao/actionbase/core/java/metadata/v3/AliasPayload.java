package com.kakao.actionbase.core.java.metadata.v3;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableAliasPayload.class)
@JsonDeserialize(as = ImmutableAliasPayload.class)
public interface AliasPayload {

  String alias();

  String table();

  String comment();

  default AliasDescriptor toAliasDescriptor(String tenant, String database) {
    return ImmutableAliasDescriptor.builder()
        .updatedAt(System.currentTimeMillis())
        .tenant(tenant)
        .database(database)
        .alias(alias())
        .table(table())
        .comment(comment())
        .build();
  }
}
