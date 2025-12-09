package com.kakao.actionbase.core.java.compat.v2;

import com.kakao.actionbase.core.java.edge.EdgeEvent;
import com.kakao.actionbase.core.java.edge.EdgeState;
import com.kakao.actionbase.core.java.payload.cdc.CDCListPayload;
import com.kakao.actionbase.core.java.payload.cdc.CDCPayload;

import java.util.List;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableEdgeMutationResult.class)
@JsonDeserialize(as = ImmutableEdgeMutationResult.class)
public interface EdgeMutationResult {

  List<EdgeMutationResultItem> result();

  static EdgeMutationResult fromV3(CDCListPayload<EdgeEvent, EdgeState> cdcList) {
    ImmutableEdgeMutationResult.Builder builder = ImmutableEdgeMutationResult.builder();
    for (CDCPayload<EdgeEvent, EdgeState> cdc : cdcList.items()) {
      String status = StatusFromV3Cdc.deriveStatus(cdc);
      builder.addResult(EdgeMutationResultItem.fromV3(cdc.events().get(0), cdc.after(), status));
    }
    return builder.build();
  }
}
