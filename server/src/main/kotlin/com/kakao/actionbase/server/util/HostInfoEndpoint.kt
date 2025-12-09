package com.kakao.actionbase.server.util

import java.lang.management.ManagementFactory
import java.net.InetAddress

import org.springframework.boot.actuate.endpoint.annotation.Endpoint
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation
import org.springframework.stereotype.Component

@Component
@Endpoint(id = "host-info")
class HostInfoEndpoint {
    @ReadOperation
    fun hostInfo(): HostInfo {
        val runtime = Runtime.getRuntime()
        val os = System.getProperties()
        val bean = ManagementFactory.getRuntimeMXBean()

        return HostInfo(
            hostName = InetAddress.getLocalHost().hostName,
            hostAddress = InetAddress.getLocalHost().hostAddress,
            osName = os.getProperty("os.name"),
            osVersion = os.getProperty("os.version"),
            osArch = os.getProperty("os.arch"),
            availableProcessors = runtime.availableProcessors(),
            totalMemory = runtime.totalMemory(),
            freeMemory = runtime.freeMemory(),
            vmVersion = bean.vmVersion,
            vmVendor = bean.vmVendor,
        )
    }
}

data class HostInfo(
    val hostName: String,
    val hostAddress: String,
    val osName: String,
    val osVersion: String,
    val osArch: String,
    val availableProcessors: Int,
    val totalMemory: Long, // in bytes
    val freeMemory: Long, // in bytes
    val vmVersion: String,
    val vmVendor: String,
)
