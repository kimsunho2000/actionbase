package com.kakao.actionbase.core.java.state;

import java.util.Map;

public interface EventRequest {

  String requestId();

  EventType type();

  long version();

  Map<String, Object> properties();
}
