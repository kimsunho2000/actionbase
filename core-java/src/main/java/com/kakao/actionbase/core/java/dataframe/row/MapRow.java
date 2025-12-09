package com.kakao.actionbase.core.java.dataframe.row;

import com.kakao.actionbase.core.java.annotation.AllowNulls;
import com.kakao.actionbase.core.java.types.StructType;

import java.util.Map;

import org.immutables.value.Value;

@Value.Immutable
public abstract class MapRow extends Row {

  @Override
  public RowType type() {
    return RowType.MAP;
  }

  @AllowNulls
  public abstract Map<String, Object> data();

  @Override
  public abstract StructType schema();

  @Override
  @Value.Derived
  public int size() {
    return data().size();
  }

  @Override
  public Object get(int index) {
    throw new UnsupportedOperationException("MapRow does not support index access.");
  }

  @Override
  public Object get(String fieldName) {
    if (schema().hasField(fieldName)) {
      return data().get(fieldName);
    }
    throw new IllegalArgumentException(
        "Field name '" + fieldName + "' does not exist in the schema.");
  }
}
