package com.kakao.actionbase.core.java.payload.cdc;

import com.kakao.actionbase.core.java.state.Event;
import com.kakao.actionbase.core.java.state.State;

import java.util.List;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableCDCListPayload.class)
@JsonDeserialize(as = ImmutableCDCListPayload.class)
public interface CDCListPayload<E extends Event, S extends State> {

  @Value.Parameter
  List<CDCPayload<E, S>> items();
}
