package com.kakao.actionbase.core.java.dataframe.column;

import com.kakao.actionbase.core.java.annotation.NotNull;
import com.kakao.actionbase.core.java.types.common.StructField;

import java.util.Arrays;
import java.util.Iterator;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableColumn.class)
@JsonDeserialize(as = ImmutableColumn.class)
public abstract class Column implements Iterable<Object> {

  public abstract Object[] data();

  public abstract StructField field();

  public Object get(int rowIndex) {
    return data()[rowIndex];
  }

  public int size() {
    return data().length;
  }

  @NotNull
  public Iterator<Object> iterator() {
    return Arrays.stream(data()).iterator();
  }
}
