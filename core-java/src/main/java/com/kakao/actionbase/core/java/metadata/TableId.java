package com.kakao.actionbase.core.java.metadata;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@Value.Style(allParameters = true)
@JsonSerialize(as = ImmutableTableId.class)
@JsonDeserialize(as = ImmutableTableId.class)
public interface TableId extends Id {

  String tenant();

  String database();

  String table();

  @Value.Derived
  @Value.Auxiliary
  default DatabaseId databaseId() {
    return ImmutableDatabaseId.of(tenant(), database());
  }

  @Value.Derived
  @Value.Auxiliary
  default AliasId asAliasId() {
    return ImmutableAliasId.of(tenant(), database(), table());
  }
}
