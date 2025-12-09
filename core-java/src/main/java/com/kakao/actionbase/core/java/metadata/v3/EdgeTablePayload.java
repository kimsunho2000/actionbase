package com.kakao.actionbase.core.java.metadata.v3;

import com.kakao.actionbase.core.java.metadata.v3.common.EdgeSchema;
import com.kakao.actionbase.core.java.metadata.v3.common.SchemaType;

import org.immutables.value.Value;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableEdgeTablePayload.class)
@JsonDeserialize(as = ImmutableEdgeTablePayload.class)
@JsonTypeName(SchemaType.EDGE_TYPE)
public interface EdgeTablePayload extends TablePayload<EdgeSchema> {

  @Override
  @Value.Derived
  default SchemaType type() {
    return SchemaType.EDGE;
  }

  @Override
  EdgeSchema schema();

  @Override
  default EdgeTableDescriptor toTableDescriptor(String tenant, String database) {
    return ImmutableEdgeTableDescriptor.builder()
        .updatedAt(System.currentTimeMillis())
        .tenant(tenant)
        .database(database)
        .table(table())
        .storage(storage())
        .comment(comment())
        .schema(schema())
        .mode(mode())
        .build();
  }
}
