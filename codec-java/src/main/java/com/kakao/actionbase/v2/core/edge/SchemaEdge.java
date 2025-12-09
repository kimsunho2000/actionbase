package com.kakao.actionbase.v2.core.edge;

import com.kakao.actionbase.v2.core.code.VersionValue;
import com.kakao.actionbase.v2.core.metadata.Direction;
import com.kakao.actionbase.v2.core.types.EdgeSchema;
import com.kakao.actionbase.v2.core.types.Field;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SchemaEdge {

  final boolean active;

  final long ts;

  final Object src;

  final Object tgt;

  final Direction dir;

  final List<Object> props;

  final EdgeSchema schema;

  final Map<String, VersionValue> rawData;

  public SchemaEdge(
      boolean active,
      long ts,
      Object src,
      Object tgt,
      Direction dir,
      List<Object> props,
      EdgeSchema schema,
      Map<String, VersionValue> rawData) {
    this.active = active;
    this.ts = ts;
    this.src = src;
    this.tgt = tgt;
    this.dir = dir;
    this.props = props;
    this.schema = schema;
    this.rawData = rawData;
  }

  public boolean isActive() {
    return active;
  }

  public long getTs() {
    return ts;
  }

  public Object getSrc() {
    return src;
  }

  public Object getTgt() {
    return tgt;
  }

  public Direction getDir() {
    return dir;
  }

  public List<Object> getProps() {
    return props;
  }

  public EdgeSchema getSchema() {
    return schema;
  }

  public Map<String, Object> getPropertyAsMap() {
    List<Object> props = getProps();
    Map<String, Object> propertyAsMap = new HashMap<>();
    if (schema != null && props != null) {
      int size = Math.min(schema.getFields().size(), props.size());
      for (int i = 0; i < size; i++) {
        Field field = schema.getFields().get(i);
        Object value = props.get(i);
        if (value != null) {
          propertyAsMap.put(field.getName(), value);
        }
      }
    }
    return propertyAsMap;
  }

  public Map<String, VersionValue> getRawData() {
    return rawData;
  }

  @Override
  public String toString() {
    return "SchemaEdge(active="
        + active
        + ", ts="
        + ts
        + ", src="
        + src
        + ", tgt="
        + tgt
        + ", dir="
        + dir
        + ", props="
        + props
        + ", schema="
        + schema
        + ")";
  }
}
