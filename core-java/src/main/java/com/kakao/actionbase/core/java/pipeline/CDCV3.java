package com.kakao.actionbase.core.java.pipeline;

import com.kakao.actionbase.core.java.annotation.Nullable;
import com.kakao.actionbase.core.java.payload.cdc.CDCPayload;
import com.kakao.actionbase.core.java.payload.cdc.ImmutableCDCPayload;
import com.kakao.actionbase.core.java.state.Event;
import com.kakao.actionbase.core.java.state.State;

import java.util.List;

public interface CDCV3<E extends Event, S extends State> extends CDC, MessageV3 {

  String status();

  List<E> events();

  @Nullable
  S before();

  @Nullable
  S after();

  long accumulator();

  @Nullable
  String message();

  long lockAt();

  default CDCPayload<E, S> toPayloadItem() {
    return ImmutableCDCPayload.<E, S>builder()
        .events(events())
        .before(before())
        .after(after())
        .build();
  }
}
