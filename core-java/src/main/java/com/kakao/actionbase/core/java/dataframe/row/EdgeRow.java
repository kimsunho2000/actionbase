package com.kakao.actionbase.core.java.dataframe.row;

import com.kakao.actionbase.core.java.annotation.AllowNulls;
import com.kakao.actionbase.core.java.edge.EdgePayload;
import com.kakao.actionbase.core.java.types.StructType;

import org.immutables.value.Value;

@Value.Immutable
public abstract class EdgeRow extends Row {

  @Override
  public RowType type() {
    return RowType.EDGE;
  }

  @AllowNulls
  public abstract EdgePayload data();

  @Override
  public abstract StructType schema();

  @Override
  @Value.Derived
  public int size() {
    // source, target, properties
    return 3;
  }

  @Override
  public Object get(int index) {
    if (index == 0) {
      return data().source();
    } else if (index == 1) {
      return data().target();
    } else if (index == 2) {
      return data().properties();
    }
    throw new IndexOutOfBoundsException("EdgeRow does not support index " + index + ".");
  }

  @Override
  public Object get(String fieldName) {
    if (fieldName.equals("source")) {
      return data().source();
    } else if (fieldName.equals("target")) {
      return data().target();
    } else if (fieldName.startsWith("properties")) {
      return data().properties();
    }
    throw new UnsupportedOperationException("MapRow does not support field '" + fieldName + "'.");
  }
}
