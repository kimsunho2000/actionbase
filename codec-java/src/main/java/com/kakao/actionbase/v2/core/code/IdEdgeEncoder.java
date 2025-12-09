package com.kakao.actionbase.v2.core.code;

public interface IdEdgeEncoder {

  String encode(Object src, Object tgt);

  KeyValue<Object> decode(String edgeId);
}
