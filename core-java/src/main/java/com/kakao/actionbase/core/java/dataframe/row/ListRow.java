package com.kakao.actionbase.core.java.dataframe.row;

import com.kakao.actionbase.core.java.annotation.AllowNulls;
import com.kakao.actionbase.core.java.types.StructType;

import java.util.List;

import org.immutables.value.Value;

@Value.Immutable
public abstract class ListRow extends Row {

  @Override
  public RowType type() {
    return RowType.LIST;
  }

  @AllowNulls
  public abstract List<Object> data();

  @Override
  public abstract StructType schema();

  @Override
  public Object get(int index) {
    return data().get(index);
  }

  @Override
  public Object get(String fieldName) {
    int idx = schema().getFieldIndex(fieldName);
    if (idx == -1) {
      throw new IllegalArgumentException(
          "Field name '" + fieldName + "' does not exist in schema.");
    }
    return data().get(idx);
  }

  @Override
  @Value.Derived
  public int size() {
    return data().size();
  }

  @Value.Check
  protected void check() {
    if (data().size() != schema().size()) {
      throw new IllegalArgumentException("Data length does not match schema.");
    }
  }
}
