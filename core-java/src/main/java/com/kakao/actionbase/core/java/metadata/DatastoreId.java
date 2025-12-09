package com.kakao.actionbase.core.java.metadata;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@Value.Style(allParameters = true)
@JsonSerialize(as = ImmutableDatastoreId.class)
@JsonDeserialize(as = ImmutableDatastoreId.class)
public interface DatastoreId extends Id {

  String tenant();

  String datastore();
}
