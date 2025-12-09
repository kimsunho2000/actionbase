package com.kakao.actionbase.core.java.hbase;

import java.util.List;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableHBaseClusterInfo.class)
@JsonDeserialize(as = ImmutableHBaseClusterInfo.class)
public interface HBaseClusterInfo {
  String clusterName();

  List<String> zkHosts();

  int zkPort();
}
