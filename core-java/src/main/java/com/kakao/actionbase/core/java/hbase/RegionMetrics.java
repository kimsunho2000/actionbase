package com.kakao.actionbase.core.java.hbase;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableRegionMetrics.class)
@JsonDeserialize(as = ImmutableRegionMetrics.class)
public interface RegionMetrics {

  long storeCount();

  long storeFileCount();

  SizeMetric storeFileSize();

  SizeMetric memStoreSize();

  SizeMetric bloomFilterSize();

  SizeMetric uncompressedStoreFileSize();

  long writeRequestCount();

  long readRequestCount();

  long filteredReadRequestCount();

  long dataLocality();

  long lastMajorCompactionTimestamp();

  SizeMetric storeFileIndexSize();

  SizeMetric storeFileRootLevelIndexSize();

  SizeMetric storeFileUncompressedDataIndexSize();

  long requestCount();

  @Value.Auxiliary
  static RegionMetrics empty() {
    return ImmutableRegionMetrics.builder()
        .storeCount(0)
        .storeFileCount(0)
        .storeFileSize(SizeMetric.empty(SizeMetricUnit.BYTE))
        .memStoreSize(SizeMetric.empty(SizeMetricUnit.BYTE))
        .bloomFilterSize(SizeMetric.empty(SizeMetricUnit.BYTE))
        .uncompressedStoreFileSize(SizeMetric.empty(SizeMetricUnit.BYTE))
        .writeRequestCount(0)
        .readRequestCount(0)
        .filteredReadRequestCount(0)
        .dataLocality(0)
        .lastMajorCompactionTimestamp(0)
        .storeFileIndexSize(SizeMetric.empty(SizeMetricUnit.BYTE))
        .storeFileRootLevelIndexSize(SizeMetric.empty(SizeMetricUnit.BYTE))
        .storeFileUncompressedDataIndexSize(SizeMetric.empty(SizeMetricUnit.BYTE))
        .requestCount(0)
        .build();
  }
}
