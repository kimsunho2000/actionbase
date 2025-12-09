package com.kakao.actionbase.core.java.payload.cdc;

import com.kakao.actionbase.core.java.annotation.Nullable;
import com.kakao.actionbase.core.java.metadata.v3.Descriptor;

public interface MetadataCDC<M extends Descriptor<?>> {

  M before();

  M after();

  interface Builder<M extends Descriptor<?>> {
    Builder<M> before(@Nullable M before);

    Builder<M> after(@Nullable M before);

    MetadataCDC<M> build();
  }
}
