package com.kakao.actionbase.core.java.edge.index;

import com.kakao.actionbase.core.java.annotation.Nullable;
import com.kakao.actionbase.core.java.codec.common.hbase.Order;

import org.immutables.value.Value;

@Value.Immutable
@Value.Style(allParameters = true)
public interface EncodableIndexValue {

  @Nullable
  Object value();

  Order order();
}
