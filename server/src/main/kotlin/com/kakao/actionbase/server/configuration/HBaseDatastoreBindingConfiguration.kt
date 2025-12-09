package com.kakao.actionbase.server.configuration

import com.kakao.actionbase.engine.datastore.hbase.admin.HBaseAdmin
import com.kakao.actionbase.v2.engine.Graph
import com.kakao.actionbase.v2.engine.compat.DefaultHBaseCluster

import org.apache.hadoop.hbase.NamespaceDescriptor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ConditionalOnHBaseDatastore
class HBaseDatastoreBindingConfiguration(
    // DefaultHBaseCluster is initialized in graph, so graph configuration must be completed before hbase admin injection is possible.
    private val graph: Graph,
) {
    @Bean
    fun hBaseAdmin(): HBaseAdmin =
        HBaseAdmin(
            DefaultHBaseCluster.INSTANCE.connectionMono
                .map { it.admin }
                .cache(),
        )

    @Bean
    fun namespaceDescriptor(serverProperties: ServerProperties): NamespaceDescriptor =
        serverProperties.datastore.configuration[ServerProperties.DatastoreProperties.CONFIG_KEY_ACTIONBASE_HBASE_NAMESPACE]
            ?.let { namespace ->
                NamespaceDescriptor.create(namespace).build()
            } ?: throw java.lang.IllegalArgumentException("Missing required configuration: ‘actionbase.hbase.namespace’ must be specified in the datastore configuration.")
}
