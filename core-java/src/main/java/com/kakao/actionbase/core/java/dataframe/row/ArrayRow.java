package com.kakao.actionbase.core.java.dataframe.row;

import com.kakao.actionbase.core.java.annotation.AllowNulls;
import com.kakao.actionbase.core.java.annotation.Nullable;
import com.kakao.actionbase.core.java.types.StructType;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.node.ObjectNode;

@Value.Immutable
public abstract class ArrayRow extends Row {

  @Override
  public RowType type() {
    return RowType.ARRAY;
  }

  @AllowNulls
  public abstract Object[] data();

  @Override
  @Nullable
  public abstract StructType schema();

  @Override
  @Value.Derived
  public int size() {
    return data().length;
  }

  @Override
  public Object get(int index) {
    return data()[index];
  }

  @Override
  public Object get(String fieldName) {
    if (schema() == null) {
      throw new IllegalArgumentException("Schema information is missing.");
    } else {
      int idx = schema().getFieldIndex(fieldName);
      if (idx == -1) {
        throw new IllegalArgumentException(
            "Field name '" + fieldName + "' does not exist in schema.");
      }
      return data()[idx];
    }
  }

  @Value.Check
  protected void check() {
    if (schema() != null && data().length != schema().size()) {
      throw new IllegalArgumentException("Data length does not match schema.");
    }
    for (Object value : data()) {
      if (!isSupportedType(value)) {
        throw new IllegalArgumentException("Unsupported data type: " + value.getClass());
      }
    }
  }

  public static boolean isSupportedType(Object value) {
    return value == null
        || value instanceof String
        || value instanceof Integer
        || value instanceof Long
        || value instanceof Double
        || value instanceof Boolean
        || value instanceof Object[]
        || value instanceof ObjectNode;
  }
}
