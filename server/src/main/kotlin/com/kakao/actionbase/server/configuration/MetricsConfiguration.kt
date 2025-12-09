package com.kakao.actionbase.server.configuration

import kotlin.math.absoluteValue

import org.springframework.boot.actuate.info.InfoEndpoint
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

import io.micrometer.core.instrument.Gauge
import io.micrometer.core.instrument.binder.MeterBinder

@Configuration
class MetricsConfiguration {
    @Bean
    fun gitInfoMetrics(infoEndpoint: InfoEndpoint): MeterBinder =
        MeterBinder { registry ->
            val artifactInfo = ArtifactInfo.of(infoEndpoint)
            // 0 ~ 9999
            val uniqueValue = artifactInfo.hashCode().absoluteValue % 10000

            Gauge
                .builder("artifact_info") { uniqueValue }
                .description("Artifact Info")
                .setArtifactInfo(artifactInfo)
                .register(registry)
        }

    private fun Gauge.Builder<*>.setArtifactInfo(artifactInfo: ArtifactInfo): Gauge.Builder<*> =
        apply {
            artifactInfo.commitId?.let { tag("commit_id", it) }
            artifactInfo.commitTime?.let { tag("commit_time", it) }
            artifactInfo.branch?.let { tag("branch", it) }
            artifactInfo.dirty?.let { tag("dirty", it) }
        }
}

data class ArtifactInfo(
    val commitId: String?,
    val commitTime: String?,
    val branch: String?,
    val dirty: String?,
) {
    override fun toString(): String =
        if (dirty?.toBoolean() == true) {
            "$commitId(dirty) on $branch"
        } else {
            "$commitId on $branch"
        }

    companion object {
        fun of(infoEndpoint: InfoEndpoint): ArtifactInfo {
            val info = infoEndpoint.info()

            val gitInfo = info["git"]?.let { it as Map<*, *> }
            val branch = gitInfo?.get("branch")?.toString()
            val dirty = gitInfo?.get("dirty")?.toString()
            val commitInfo = gitInfo?.get("commit")?.let { it as Map<*, *> }
            val commitTime = commitInfo?.get("time")?.toString()
            val commitId =
                commitInfo
                    ?.get("id")
                    ?.let { it as Map<*, *> }
                    ?.get("abbrev")
                    ?.toString()

            return ArtifactInfo(commitId, commitTime, branch, dirty)
        }
    }
}
