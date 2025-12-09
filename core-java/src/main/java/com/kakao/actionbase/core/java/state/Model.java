package com.kakao.actionbase.core.java.state;

public interface Model<KEY> {
  KEY key();

  boolean keyEquals(Model<?> model);
}
