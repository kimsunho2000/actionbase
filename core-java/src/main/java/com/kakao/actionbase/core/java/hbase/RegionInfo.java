package com.kakao.actionbase.core.java.hbase;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableRegionInfo.class)
@JsonDeserialize(as = ImmutableRegionInfo.class)
public interface RegionInfo {

  long regionId();

  String regionName();

  String startKey();

  String endKey();

  RegionInfoTable table();
}
