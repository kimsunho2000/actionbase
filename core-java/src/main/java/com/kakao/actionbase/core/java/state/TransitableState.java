package com.kakao.actionbase.core.java.state;

import com.kakao.actionbase.core.java.types.StructType;

public interface TransitableState<E extends Event, S extends TransitableState<E, S>> extends State {

  S transit(E event, StructType schema);
}
