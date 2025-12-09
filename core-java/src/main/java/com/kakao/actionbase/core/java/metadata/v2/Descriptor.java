package com.kakao.actionbase.core.java.metadata.v2;

import com.kakao.actionbase.core.java.metadata.TenantId;

public interface Descriptor<V3 extends com.kakao.actionbase.core.java.metadata.v3.Descriptor<?>> {

  V3 toV3(TenantId tenant);
}
