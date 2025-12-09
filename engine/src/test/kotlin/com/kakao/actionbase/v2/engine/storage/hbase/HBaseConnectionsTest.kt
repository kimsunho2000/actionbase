package com.kakao.actionbase.v2.engine.storage.hbase

import org.apache.hadoop.conf.Configuration

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class HBaseConnectionsTest :
    FunSpec({
        test("cacheKey | Returns the same cache key when property values injected into HBaseConfig are the same, different cache key when different") {
            val zkHosts = "test-hm1.exmaple.com:2181,test-hm2.exmaple.com:2181,test-hm2.exmaple.com:2181"
            val hbaseConfig = createConfiguration(zkHosts)
            val hbaseConfigSameProps = createConfiguration(zkHosts)
            val hbaseConfigDifferentZkHosts =
                createConfiguration(zkHosts = "another-zk1.exmaple.com:2181,another-zk2.exmaple.com:2181")
            val hbaseConfigDifferentProp =
                createConfiguration(zkHosts).let {
                    it.set("prop", "value")
                    it
                }

            val expected = HBaseConnections.getCacheKey(zkHosts, hbaseConfig)
            HBaseConnections.getCacheKey(zkHosts, hbaseConfig) shouldBe expected
            HBaseConnections.getCacheKey(zkHosts, hbaseConfigSameProps) shouldBe expected
            HBaseConnections.getCacheKey(zkHosts, hbaseConfigDifferentZkHosts) shouldNotBe expected
            HBaseConnections.getCacheKey(zkHosts, hbaseConfigDifferentProp) shouldNotBe expected
        }
    }) {
    companion object {
        private fun createConfiguration(zkHosts: String): Configuration {
            val config = Configuration()
            config.set("hbase.zookeeper.quorum", zkHosts)
            return config
        }
    }
}
