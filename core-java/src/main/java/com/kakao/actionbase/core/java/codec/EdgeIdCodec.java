package com.kakao.actionbase.core.java.codec;

public interface EdgeIdCodec {

  String encode(Object src, Object tgt);

  KeyValue<Object> decode(String edgeId);
}
