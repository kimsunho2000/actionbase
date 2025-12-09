package com.kakao.actionbase.core.java.metadata.v3;

import com.kakao.actionbase.core.java.constant.Constant;
import com.kakao.actionbase.core.java.edge.Edge;
import com.kakao.actionbase.core.java.edge.EdgePayload;
import com.kakao.actionbase.core.java.edge.EdgeState;
import com.kakao.actionbase.core.java.edge.ImmutableEdgePayload;
import com.kakao.actionbase.core.java.metadata.ImmutableTableId;
import com.kakao.actionbase.core.java.metadata.TableId;
import com.kakao.actionbase.core.java.metadata.TenantId;
import com.kakao.actionbase.core.java.metadata.v3.common.MutationMode;
import com.kakao.actionbase.core.java.state.EventType;
import com.kakao.actionbase.core.java.types.DataType;

import java.util.Map;

public final class Databases {

  static class E {

    public static String tenant(Edge edge) {
      return DataType.STRING.cast(edge.source());
    }

    public static String database(Edge edge) {
      return DataType.STRING.cast(edge.target());
    }

    public static String comment(Edge edge) {
      return DataType.STRING.cast(edge.properties().get(Descriptors.COMMENT_FIELD));
    }
  }

  static class M {

    public static boolean active(DatabaseDescriptor instance) {
      return instance.active();
    }

    public static long version(DatabaseDescriptor instance) {
      return System.currentTimeMillis();
    }

    public static String source(DatabaseDescriptor instance) {
      return instance.tenant();
    }

    public static String target(DatabaseDescriptor instance) {
      return instance.database();
    }

    public static Map.Entry<String, ? super Object> comment(DatabaseDescriptor instance) {
      return new java.util.AbstractMap.SimpleEntry<>(Descriptors.COMMENT_FIELD, instance.comment());
    }
  }

  public static EdgePayload toEdgePayload(DatabaseDescriptor instance) {
    return ImmutableEdgePayload.builder()
        .version(instance.updatedAt())
        .source(M.source(instance))
        .target(M.target(instance))
        .putProperties(M.comment(instance))
        .build();
  }

  public static DatabaseDescriptor fromEdge(Edge edge) {
    return ImmutableDatabaseDescriptor.builder()
        .tenant(E.tenant(edge))
        .database(E.database(edge))
        .comment(E.comment(edge))
        .build();
  }

  public static EdgeState toEdgeState(DatabaseDescriptor instance) {
    return toEdgePayload(instance)
        .toEvent(EventType.INSERT)
        .toState(Descriptors.Schema.DATABASE.getPropertiesSchema());
  }

  public static DatabaseDescriptor fromEdgeState(EdgeState state) {
    return fromEdge(state.toPayload());
  }

  public static DatabaseDescriptor sys(TenantId tenantId) {
    return ImmutableDatabaseDescriptor.builder()
        .active(true)
        .tenant(tenantId.tenant())
        .database(Constant.SYSTEM_DATABASE_NAME)
        .comment(Constant.SYSTEM_DATABASE_COMMENT)
        .build();
  }

  public static EdgeTableDescriptor databaseTable(TenantId tenantId) {
    return ImmutableEdgeTableDescriptor.builder()
        .comment(Constant.Table.DATABASE_COMMENT)
        .tenant(tenantId.tenant())
        .database(Constant.SYSTEM_DATABASE_NAME)
        .table(Constant.Table.DATABASE_NAME)
        .storage(Storages.Schemas.METADATA_NAME)
        .schema(Descriptors.Schema.DATABASE)
        .mode(MutationMode.SYNC)
        .build();
  }

  public static class Schemas {

    public static TableId getTableId(TenantId tenantId) {
      return ImmutableTableId.of(
          tenantId.tenant(), Constant.SYSTEM_DATABASE_NAME, Constant.Table.DATABASE_NAME);
    }
  }
}
