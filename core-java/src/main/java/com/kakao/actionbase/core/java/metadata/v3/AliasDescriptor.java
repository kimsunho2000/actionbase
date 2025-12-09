package com.kakao.actionbase.core.java.metadata.v3;

import com.kakao.actionbase.core.java.edge.EdgePayload;
import com.kakao.actionbase.core.java.edge.EdgeState;
import com.kakao.actionbase.core.java.metadata.AliasId;
import com.kakao.actionbase.core.java.metadata.ImmutableAliasId;
import com.kakao.actionbase.core.java.metadata.ImmutableTableId;
import com.kakao.actionbase.core.java.metadata.TableId;

import org.immutables.value.Value;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableAliasDescriptor.class)
@JsonDeserialize(as = ImmutableAliasDescriptor.class)
public interface AliasDescriptor extends Descriptor<AliasId> {

  String database();

  String alias();

  String table();

  @Override
  @JsonIgnore
  @Value.Derived
  @Value.Auxiliary
  default AliasId id() {
    return ImmutableAliasId.of(tenant(), database(), alias());
  }

  @JsonIgnore
  @Value.Derived
  @Value.Auxiliary
  default TableId tableId() {
    return ImmutableTableId.of(tenant(), database(), table());
  }

  @Override
  @JsonIgnore
  @Value.Auxiliary
  default EdgePayload toEdgePayload() {
    return Aliases.toEdge(this);
  }

  @Override
  @JsonIgnore
  @Value.Auxiliary
  default EdgeState toEdgeState() {
    return null;
  }

  @JsonIgnore
  @Value.Auxiliary
  default AliasPayload toPayload() {
    return ImmutableAliasPayload.builder().alias(alias()).table(table()).comment(comment()).build();
  }
}
