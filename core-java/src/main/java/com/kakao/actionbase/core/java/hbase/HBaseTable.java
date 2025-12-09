package com.kakao.actionbase.core.java.hbase;

import org.immutables.value.Value;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableHBaseTable.class)
@JsonDeserialize(as = ImmutableHBaseTable.class)
public interface HBaseTable {

  String name();

  @JsonIgnore
  @Value.Auxiliary
  default String namespace() {
    if (name().contains(":")) {
      return name().split(":")[0];
    } else {
      return "default";
    }
  }

  @JsonIgnore
  @Value.Auxiliary
  default String tableName() {
    return name().replaceFirst(namespace() + ":", "");
  }

  boolean isEnabled();

  String bloomFilter();

  boolean inMemory();

  int versions();

  String keepDeletedCells();

  String dataBlockEncoding();

  String compression();

  int ttl();

  int minVersions();

  boolean blockCache();

  int blockSize();

  int replicationScope();
}
