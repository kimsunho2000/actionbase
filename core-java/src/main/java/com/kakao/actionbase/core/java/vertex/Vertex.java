package com.kakao.actionbase.core.java.vertex;

import java.util.Map;

public interface Vertex extends VertexModel {

  @Override
  Object key();

  Map<String, Object> properties();
}
