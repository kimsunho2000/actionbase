package com.kakao.actionbase.core.java.metadata.v3;

import com.kakao.actionbase.core.java.metadata.v3.common.EdgeSchema;
import com.kakao.actionbase.core.java.metadata.v3.common.SchemaType;

import org.immutables.value.Value;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableEdgeTableDescriptor.class)
@JsonDeserialize(as = ImmutableEdgeTableDescriptor.class)
public interface EdgeTableDescriptor extends TableDescriptor<EdgeSchema> {

  @Override
  @Value.Derived
  default SchemaType type() {
    return SchemaType.EDGE;
  }

  @Override
  EdgeSchema schema();

  @JsonIgnore
  @Value.Auxiliary
  default EdgeTablePayload toPayload() {
    return ImmutableEdgeTablePayload.builder()
        .table(table())
        .comment(comment())
        .schema(schema())
        .mode(mode())
        .storage(storage())
        .build();
  }
}
