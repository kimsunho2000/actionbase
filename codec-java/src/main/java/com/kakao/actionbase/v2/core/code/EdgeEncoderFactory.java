package com.kakao.actionbase.v2.core.code;

import java.util.concurrent.ConcurrentLinkedQueue;

public class EdgeEncoderFactory {

  final ConcurrentLinkedQueue<EdgeBuffer> pool;

  final BytesKeyValueEdgeEncoder bytesKeyValueEdgeEncoder;
  final StringKeyFieldValueEdgeEncoder stringKeyFieldValueEdgeEncoder;

  public EdgeEncoderFactory() {
    pool = null;
    bytesKeyValueEdgeEncoder = new BytesKeyValueEdgeEncoder();
    stringKeyFieldValueEdgeEncoder = new StringKeyFieldValueEdgeEncoder();
  }

  public EdgeEncoderFactory(int poolSize) {
    pool = new ConcurrentLinkedQueue<>();
    for (int i = 0; i < poolSize; i++) {
      pool.add(new EdgeBuffer());
    }
    bytesKeyValueEdgeEncoder = new BytesKeyValueEdgeEncoder(pool);
    stringKeyFieldValueEdgeEncoder = new StringKeyFieldValueEdgeEncoder(pool);
  }

  public BytesKeyValueEdgeEncoder getBytesKeyValueEncoder() {
    return bytesKeyValueEdgeEncoder;
  }

  public StringKeyFieldValueEdgeEncoder getStringKeyFieldValueEncoder() {
    return stringKeyFieldValueEdgeEncoder;
  }
}
