package com.kakao.actionbase.core.java.metadata.v3.common;

import com.kakao.actionbase.core.java.codec.common.hbase.Order;
import com.kakao.actionbase.core.java.edge.*;
import com.kakao.actionbase.core.java.state.ImmutableStateValue;
import com.kakao.actionbase.core.java.state.StateValue;
import com.kakao.actionbase.core.java.types.DataType;
import com.kakao.actionbase.core.java.types.ImmutableStructType;
import com.kakao.actionbase.core.java.types.StructType;
import com.kakao.actionbase.core.java.types.common.Field;
import com.kakao.actionbase.core.java.types.common.ImmutableField;
import com.kakao.actionbase.core.java.types.common.StructField;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.immutables.value.Value;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableEdgeSchema.class)
@JsonDeserialize(as = ImmutableEdgeSchema.class)
public interface EdgeSchema extends Schema {

  @Override
  @Value.Derived
  default SchemaType type() {
    return SchemaType.EDGE;
  }

  Field source();

  Field target();

  @Override
  List<StructField> properties();

  DirectionType direction();

  List<Index> indexes();

  @JsonIgnore
  @Value.Derived
  @Value.Auxiliary
  default Set<String> propertyNames() {
    return properties().stream().map(StructField::name).collect(Collectors.toSet());
  }

  @JsonIgnore
  @Value.Derived
  @Value.Auxiliary
  default Map<String, Index> indexMap() {
    return indexes().stream().collect(Collectors.toMap(Index::index, index -> index));
  }

  @Override
  @JsonIgnore
  @Value.Derived
  @Value.Auxiliary
  default StructType getPropertiesSchema() {
    return ImmutableStructType.builder().fields(properties()).build();
  }

  @Value.Check
  default void check() {
    for (Index index : indexes()) {
      if (index.fields().isEmpty()) {
        // TODO
        // throw new IllegalArgumentException("index fields cannot be empty");
      }
      for (IndexField field : index.fields()) {
        String fieldName = field.field();
        if (!fieldName.equals(Edges.VERSION_FIELD)
            && !fieldName.equals(Edges.SOURCE_FIELD)
            && !fieldName.equals(Edges.TARGET_FIELD)
            && !propertyNames().contains(field.field())) {
          throw new IllegalArgumentException(
              String.format("index field %s not found in properties", field.field()));
        }
      }
    }
  }

  @JsonIgnore
  @Value.Auxiliary
  default Object ensureType(Object start, Direction direction) {
    Field field = direction == Direction.OUT ? source() : target();
    return field.type().cast(start);
  }

  @JsonIgnore
  @Value.Auxiliary
  default EdgeKey ensureType(EdgeKey edgeKey) {
    Object castedSource = source().type().cast(edgeKey.source());
    Object castedTarget = target().type().cast(edgeKey.target());

    return ImmutableEdgeKey.builder().source(castedSource).target(castedTarget).build();
  }

  @JsonIgnore
  @Value.Auxiliary
  default EdgeEvent ensureType(EdgeEvent event) {
    StructType propertiesSchema = getPropertiesSchema();
    ImmutableEdgeEvent.Builder builder = ImmutableEdgeEvent.builder().from(event);

    Object castedSource = source().type().cast(event.source());
    Object castedTarget = target().type().cast(event.target());

    builder.source(castedSource).target(castedTarget);

    // overwrite
    for (Map.Entry<String, Object> entry : event.properties().entrySet()) {
      String key = entry.getKey();
      Object value = entry.getValue();
      if (value != null) {
        Object castedValue = propertiesSchema.getField(key).type().cast(value);
        builder.putProperties(key, castedValue);
      } else {
        builder.putProperties(key, null);
      }
    }
    return builder.build();
  }

  @JsonIgnore
  @Value.Auxiliary
  default EdgeState ensureType(EdgeState state) {
    StructType propertiesSchema = getPropertiesSchema();
    ImmutableEdgeState.Builder builder = ImmutableEdgeState.builder().from(state);

    Object castedSource = source().type().cast(state.source());
    Object castedTarget = target().type().cast(state.target());

    builder.source(castedSource).target(castedTarget);

    for (Map.Entry<String, StateValue> entry : state.properties().entrySet()) {
      String key = entry.getKey();
      StateValue value = entry.getValue();
      if (value != null) {
        if (value.value() != null) {
          Object castedValue = propertiesSchema.getField(key).type().cast(value.value());
          builder.putProperties(key, ImmutableStateValue.of(value.version(), castedValue));
        } else {
          builder.putProperties(key, value);
        }
      } else {
        builder.putProperties(key, null);
      }
    }
    return builder.build();
  }

  @JsonIgnore
  @Value.Auxiliary
  default DataType<?> getDataType(String fieldName) {
    if (fieldName.equals(Edges.SOURCE_FIELD)) {
      return source().type();
    } else if (fieldName.equals(Edges.TARGET_FIELD)) {
      return target().type();
    } else if (fieldName.equals(Edges.VERSION_FIELD)) {
      return DataType.LONG;
    } else if (propertyNames().contains(fieldName)) {
      return getPropertiesSchema().getField(fieldName).type();
    } else {
      throw new IllegalArgumentException("Unknown field: " + fieldName);
    }
  }

  static ImmutableEdgeSchema.Builder builder() {
    return ImmutableEdgeSchema.builder();
  }

  abstract class Builder implements Schema.Builder<ImmutableEdgeSchema.Builder> {

    public abstract ImmutableEdgeSchema.Builder source(Field src);

    public abstract ImmutableEdgeSchema.Builder target(Field tgt);

    public abstract ImmutableEdgeSchema.Builder addIndexes(Index element);

    public final EdgeSchema.Builder source(DataType<?> type, String comment) {
      return source(ImmutableField.builder().type(type).comment(comment).build());
    }

    public final EdgeSchema.Builder target(DataType<?> type, String comment) {
      return target(ImmutableField.builder().type(type).comment(comment).build());
    }

    public final ImmutableEdgeSchema.Builder addIndex(
        String name, String comment, IndexField... field) {
      return addIndexes(
          ImmutableIndex.builder()
              .index(name)
              .comment(comment)
              .fields(Arrays.asList(field))
              .build());
    }

    public final ImmutableEdgeSchema.Builder addIndex(String name, IndexField... field) {
      return addIndexes(ImmutableIndex.builder().index(name).fields(Arrays.asList(field)).build());
    }

    public final ImmutableEdgeSchema.Builder addDefaultMetadataIndex() {
      return addIndex(
          Edges.DEFAULT_INDEX_NAME, ImmutableIndexField.of(Edges.TARGET_FIELD, Order.ASC));
    }
  }
}
