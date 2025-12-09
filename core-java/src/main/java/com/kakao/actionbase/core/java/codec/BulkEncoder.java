package com.kakao.actionbase.core.java.codec;

import com.kakao.actionbase.core.java.edge.EdgeState;
import com.kakao.actionbase.core.java.edge.index.EdgeIndexes;
import com.kakao.actionbase.core.java.edge.index.EncodableEdgeIndex;
import com.kakao.actionbase.core.java.metadata.v3.EdgeTableDescriptor;
import com.kakao.actionbase.core.java.metadata.v3.common.Direction;
import com.kakao.actionbase.core.java.metadata.v3.common.DirectionType;
import com.kakao.actionbase.core.java.metadata.v3.common.SchemaType;

import java.util.ArrayList;
import java.util.List;

public class BulkEncoder {

  public static List<Encoded> encodeAll(
      StateCodec codec, EdgeState state, EdgeTableDescriptor label) {
    SchemaType labelType = label.type();

    int labelId = label.code();
    boolean active = state.active();

    List<Encoded> encodedKeyValues = new ArrayList<>();

    // encode a single hash edge
    if (labelType == SchemaType.EDGE) {
      KeyValue<byte[]> kv = codec.encodeEdgeState(state, labelId);
      encodedKeyValues.add(kv);
    }

    if (active) {
      List<EncodableEdgeIndex> encodableEdgeIndexes = EdgeIndexes.build(state, label);
      for (EncodableEdgeIndex encodableEdgeIndex : encodableEdgeIndexes) {
        KeyValue<byte[]> kv = codec.encodeEdgeIndex(encodableEdgeIndex, label.code());
        encodedKeyValues.add(kv);
      }

      // encode counter keys
      if (label.schema().direction() == DirectionType.BOTH) {
        byte[] outboundKey = codec.encodeEdgeCountKey(state.source(), Direction.OUT, labelId);
        byte[] inboundKey = codec.encodeEdgeCountKey(state.target(), Direction.IN, labelId);
        encodedKeyValues.add(ImmutableKey.of(outboundKey));
        encodedKeyValues.add(ImmutableKey.of(inboundKey));
      } else if (label.schema().direction() == DirectionType.OUT) {
        byte[] outboundKey = codec.encodeEdgeCountKey(state.source(), Direction.OUT, labelId);
        encodedKeyValues.add(ImmutableKey.of(outboundKey));
      } else if (label.schema().direction() == DirectionType.IN) {
        byte[] inboundKey = codec.encodeEdgeCountKey(state.target(), Direction.IN, labelId);
        encodedKeyValues.add(ImmutableKey.of(inboundKey));
      }
    }

    return encodedKeyValues;
  }
}
