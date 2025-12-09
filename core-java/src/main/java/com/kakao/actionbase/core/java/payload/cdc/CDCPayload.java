package com.kakao.actionbase.core.java.payload.cdc;

import com.kakao.actionbase.core.java.annotation.Nullable;
import com.kakao.actionbase.core.java.state.Event;
import com.kakao.actionbase.core.java.state.State;

import java.util.List;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableCDCPayload.class)
@JsonDeserialize(as = ImmutableCDCPayload.class)
public interface CDCPayload<E extends Event, S extends State> {

  List<E> events();

  @Nullable
  S before();

  @Nullable
  S after();
}
