package com.kakao.actionbase.core.java.metadata;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@Value.Style(allParameters = true)
@JsonSerialize(as = ImmutableAliasId.class)
@JsonDeserialize(as = ImmutableAliasId.class)
public interface AliasId extends Id {

  String tenant();

  String database();

  String alias();
}
