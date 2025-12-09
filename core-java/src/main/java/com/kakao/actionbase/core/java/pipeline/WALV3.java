package com.kakao.actionbase.core.java.pipeline;

import com.kakao.actionbase.core.java.pipeline.common.WALV3MutationMode;
import com.kakao.actionbase.core.java.state.Event;

public interface WALV3<E extends Event> extends WAL, MessageV3 {

  E event();

  WALV3MutationMode mode();
}
