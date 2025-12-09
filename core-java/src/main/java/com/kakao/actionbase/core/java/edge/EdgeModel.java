package com.kakao.actionbase.core.java.edge;

import com.kakao.actionbase.core.java.state.Model;

public interface EdgeModel extends Model<EdgeKey> {

  Object source();

  Object target();

  @Override
  default EdgeKey key() {
    return ImmutableEdgeKey.of(source(), target());
  }

  @Override
  default boolean keyEquals(Model<?> model) {
    if (model instanceof EdgeModel) {
      EdgeModel edge = (EdgeModel) model;
      return key().equals(edge.key());
    }
    return false;
  }
}
