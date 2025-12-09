package com.kakao.actionbase.server.configuration

import com.kakao.actionbase.server.service.devtools.HBaseAdminService.HBaseClusterInfo

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "hbase")
data class HBaseProperties(
    val cluster: Map<String, HBaseClusterInfo> = emptyMap(),
)
