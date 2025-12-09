package com.kakao.actionbase.core.java.edge;

import java.util.Map;

public interface Edge extends EdgeModel {

  long version();

  @Override
  Object source();

  @Override
  Object target();

  Map<String, Object> properties();
}
