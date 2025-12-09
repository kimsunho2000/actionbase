package com.kakao.actionbase.core.java.metadata.v3.common;

import com.kakao.actionbase.core.java.types.DataType;
import com.kakao.actionbase.core.java.types.ImmutableStructType;
import com.kakao.actionbase.core.java.types.StructType;
import com.kakao.actionbase.core.java.types.common.Field;
import com.kakao.actionbase.core.java.types.common.ImmutableField;
import com.kakao.actionbase.core.java.types.common.StructField;

import java.util.List;

import org.immutables.value.Value;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableVertexSchema.class)
@JsonDeserialize(as = ImmutableVertexSchema.class)
public interface VertexSchema extends Schema {

  @Override
  @Value.Default
  default SchemaType type() {
    return SchemaType.VERTEX;
  }

  Field key();

  @Override
  List<StructField> properties();

  @Override
  @Value.Default
  @JsonIgnore
  default StructType getPropertiesSchema() {
    return ImmutableStructType.builder().fields(properties()).build();
  }

  abstract class Builder implements Schema.Builder<ImmutableVertexSchema.Builder> {

    public abstract ImmutableVertexSchema.Builder key(Field k);

    public final ImmutableVertexSchema.Builder key(DataType<?> type, String comment) {
      return key(ImmutableField.builder().type(type).comment(comment).build());
    }
  }
}
