package com.kakao.actionbase.v2.core.code;

import com.kakao.actionbase.v2.core.metadata.Active;

import java.util.HashMap;
import java.util.Map;

public class HashEdgeValue {

  Active active;

  long ts;

  final Map<String, VersionValue> map;

  final Long insertTs;

  final Long deleteTs;

  public HashEdgeValue(
      Active active, long ts, Map<String, VersionValue> map, Long insertTs, Long deleteTs) {
    this.active = active;
    this.ts = ts;
    this.map = map;
    this.insertTs = insertTs;
    this.deleteTs = deleteTs;
  }

  public static HashEdgeValue from(
      Active active, long ts, Map<String, Object> propertyAsMap, Long insertTs, Long deleteTs) {
    Map<String, VersionValue> map = new HashMap<>();
    for (Map.Entry<String, Object> e : propertyAsMap.entrySet()) {
      String key = e.getKey();
      Object value = e.getValue();
      map.put(key, new VersionValue(ts, value));
    }
    return new HashEdgeValue(active, ts, map, insertTs, deleteTs);
  }

  public HashEdgeValue copyWith(
      Active active,
      long newVersion,
      Map<String, Object> propertyAsMap,
      Long insertTs,
      Long deleteTs) {
    // create new values
    HashMap<String, VersionValue> newMap = new HashMap<>(map);
    long newTs = ts;

    for (Map.Entry<String, Object> e : propertyAsMap.entrySet()) {
      String key = e.getKey();
      Object value = e.getValue();
      VersionValue existingVersionValue = newMap.get(key);
      if (existingVersionValue == null || existingVersionValue.getVersion() <= newVersion) {
        newMap.put(key, new VersionValue(newVersion, value));
      }
    }
    if (newVersion > newTs) {
      newTs = newVersion;
    }
    return new HashEdgeValue(active, newTs, newMap, insertTs, deleteTs);
  }

  public Active getActive() {
    return active;
  }

  public long getTs() {
    return ts;
  }

  public Map<String, VersionValue> getMap() {
    return map;
  }

  public Long getInsertTs() {
    return insertTs;
  }

  public Long getDeleteTs() {
    return deleteTs;
  }

  public void addDegree(Long outbound, Long inbound, long version) {
    if (outbound != null) {
      map.put("_out_degree", new VersionValue(version, outbound));
    }
    if (inbound != null) {
      map.put("_in_degree", new VersionValue(version, inbound));
    }
  }

  @Override
  public int hashCode() {
    int result = active.getCode();
    result = 31 * result + (int) (ts ^ (ts >>> 32));
    result = 31 * result + map.hashCode();
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    HashEdgeValue that = (HashEdgeValue) obj;
    return active == that.active && ts == that.ts && map.equals(that.map);
  }
}
