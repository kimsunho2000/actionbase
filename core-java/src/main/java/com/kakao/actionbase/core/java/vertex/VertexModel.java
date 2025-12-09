package com.kakao.actionbase.core.java.vertex;

import com.kakao.actionbase.core.java.state.Model;

import java.util.Objects;

public interface VertexModel extends Model<Object> {

  @Override
  Object key();

  @Override
  default boolean keyEquals(Model<?> model) {
    if (model instanceof VertexModel) {
      VertexModel vertex = (VertexModel) model;
      return Objects.equals(key(), vertex.key());
    }
    return false;
  }
}
