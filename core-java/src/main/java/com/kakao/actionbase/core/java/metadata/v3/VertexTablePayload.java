package com.kakao.actionbase.core.java.metadata.v3;

import com.kakao.actionbase.core.java.metadata.v3.common.SchemaType;
import com.kakao.actionbase.core.java.metadata.v3.common.VertexSchema;

import org.immutables.value.Value;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableVertexTablePayload.class)
@JsonDeserialize(as = ImmutableVertexTablePayload.class)
@JsonTypeName(SchemaType.VERTEX_TYPE)
public interface VertexTablePayload extends TablePayload<VertexSchema> {

  @Override
  @Value.Derived
  default SchemaType type() {
    return SchemaType.VERTEX;
  }

  @Override
  VertexSchema schema();

  @Override
  default VertexTableDescriptor toTableDescriptor(String tenant, String database) {
    return ImmutableVertexTableDescriptor.builder()
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
