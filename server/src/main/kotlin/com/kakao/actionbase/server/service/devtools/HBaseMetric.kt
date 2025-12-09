package com.kakao.actionbase.server.service.devtools

import org.apache.hadoop.hbase.ClusterMetrics
import org.apache.hadoop.hbase.RegionMetrics
import org.apache.hadoop.hbase.TableName
import org.apache.hadoop.hbase.client.AsyncConnection
import org.apache.hadoop.hbase.client.RegionInfo

import reactor.core.publisher.Mono

class HBaseMetric {
    fun getRegionInfo(
        connection: AsyncConnection,
        tableName: String,
    ): Mono<List<RegionMetric>> {
        val admin = connection.admin
        val table = TableName.valueOf(tableName)
        val regionsMono = Mono.fromFuture(admin.getRegions(table))
        val clusterMetricsMono = Mono.fromFuture(admin.clusterMetrics)

        return regionsMono
            .zipWith(clusterMetricsMono)
            .map { tuple ->
                val regions = tuple.t1
                val metrics = tuple.t2
                regions.map { region ->
                    val metric = getRegionLoad(metrics, region)
                    RegionMetric(region, metric)
                }
            }
    }

    private fun getRegionLoad(
        clusterMetrics: ClusterMetrics,
        regionInfo: RegionInfo,
    ): RegionMetrics? {
        for (serverMetrics in clusterMetrics.liveServerMetrics.values) {
            val regionMetrics = serverMetrics.regionMetrics[regionInfo.regionName]
            if (regionMetrics != null) {
                return regionMetrics
            }
        }
        return null
    }

    data class RegionMetric(
        val regionInfo: RegionInfo,
        val regionMetrics: RegionMetrics?,
    )
}
