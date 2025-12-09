package com.kakao.actionbase.core.java.metadata.v3;

import com.kakao.actionbase.core.java.metadata.v3.common.SchemaType;
import com.kakao.actionbase.core.java.metadata.v3.common.VertexSchema;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableEdgeTableDescriptor.class)
@JsonDeserialize(as = ImmutableEdgeTableDescriptor.class)
public interface VertexTableDescriptor extends TableDescriptor<VertexSchema> {

  @Override
  @Value.Derived
  default SchemaType type() {
    return SchemaType.VERTEX;
  }

  @Override
  VertexSchema schema();
}
