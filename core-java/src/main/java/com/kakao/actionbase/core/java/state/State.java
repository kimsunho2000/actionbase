package com.kakao.actionbase.core.java.state;

import java.util.Map;

public interface State {

  boolean active();

  long version();

  Long createdAt();

  Long deletedAt();

  Map<String, StateValue> properties();
}
