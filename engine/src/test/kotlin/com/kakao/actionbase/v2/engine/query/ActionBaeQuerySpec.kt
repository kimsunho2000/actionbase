package com.kakao.actionbase.v2.engine.query

import com.kakao.actionbase.v2.engine.Graph
import com.kakao.actionbase.v2.engine.entity.EntityName
import com.kakao.actionbase.v2.engine.label.Label
import com.kakao.actionbase.v2.engine.test.GraphFixtures

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import reactor.kotlin.test.test

class ActionBaeQuerySpec :
    StringSpec({

        lateinit var graph: Graph

        lateinit var hbase: Label

        beforeSpec {
            graph = GraphFixtures.create()
            hbase = graph.getLabel(EntityName(GraphFixtures.serviceName, GraphFixtures.hbaseIndexed))
        }

        "ActionbaseQuery.Item.Self" {
            val service = hbase.name.service
            val label = hbase.name.nameNotNull
            val queryName = "get_query_result"

            // language=json
            val actionBaseQueryString =
                """
                {
                  "query": [
                    {
                      "type": "SELF",
                      "name": "$queryName",
                      "service": "$service",
                      "label": "$label",
                      "src": {
                        "type": "VALUE",
                        "value": [100]
                      },
                      "include": true
                    }
                  ]
                }
                """.trimIndent()

            val actionBaseQuery = ActionbaseQuery.from(actionBaseQueryString)

            graph
                .query(actionBaseQuery)
                .test()
                .assertNext {
                    it.size shouldBe 1
                    it.keys shouldBe setOf(queryName)
                    val df = it[queryName]!!
                    df.rows.size shouldBe 0 // currently no self edges
                }.verifyComplete()
        }

        /**
         * [ActionbaseQuery.Item.Get]
         */
        "ActionbaseQuery.Item.Get" {
            val service = hbase.name.service
            val label = hbase.name.nameNotNull
            val queryName = "get_query_result"

            // language=json
            val actionBaseQueryString =
                """
                {
                  "query": [
                    {
                      "type": "GET",
                      "name": "$queryName",
                      "service": "$service",
                      "label": "$label",
                      "src": {
                        "type": "VALUE",
                        "value": [100]
                      },
                      "tgt": {
                        "type": "VALUE",
                        "value": [1000, 1001]
                      },
                      "include": true
                    }
                  ]
                }
                """.trimIndent()
            val actionBaseQuery = ActionbaseQuery.from(actionBaseQueryString)

            graph
                .query(actionBaseQuery)
                .test()
                .assertNext {
                    it.size shouldBe 1
                    it.keys shouldBe setOf(queryName)
                    val df = it[queryName]!!
                    df
                        .toRowWithSchema()
                        .map { row -> row.getOrNull("permission") } shouldContainExactly listOf("na", "others")
                }.verifyComplete()
        }

        /**
         * [ActionbaseQuery.Item.Count]
         */
        "ActionbaseQuery.Item.Count" {
            val service = hbase.name.service
            val label = hbase.name.nameNotNull
            val queryName = "get_query_result"

            // language=json
            val actionBaseQueryString =
                """
                {
                  "query": [
                    {
                      "type": "COUNT",
                      "name": "$queryName",
                      "service": "$service",
                      "label": "$label",
                      "src": {
                        "type": "VALUE",
                        "value": [100]
                      },
                      "dir": "OUT",
                      "include": true
                    }
                  ]
                }
                """.trimIndent()
            val actionBaseQuery = ActionbaseQuery.from(actionBaseQueryString)

            graph
                .query(actionBaseQuery)
                .test()
                .assertNext {
                    it.size shouldBe 1
                    it.keys shouldBe setOf(queryName)
                    val df = it[queryName]!!
                    df
                        .toRowWithSchema()
                        .map { row -> row.getOrNull("COUNT(1)") } shouldContainExactly listOf(6L)
                }.verifyComplete()
        }

        /**
         * [ActionbaseQuery.Item.Scan]
         */
        "ActionbaseQuery.Item.Scan" {
            val service = hbase.name.service
            val label = hbase.name.nameNotNull
            val queryName = "get_query_result"

            // language=json
            val actionBaseQueryString =
                """
                {
                  "query": [
                    {
                      "type": "SCAN",
                      "name": "$queryName",
                      "service": "$service",
                      "label": "$label",
                      "src": {
                        "type": "VALUE",
                        "value": [100]
                      },
                      "dir": "OUT",
                      "index": "permission_created_at_desc",
                      "limit": 10,
                      "include": true
                    }
                  ]
                }
                """.trimIndent()
            val actionBaseQuery = ActionbaseQuery.from(actionBaseQueryString)

            graph
                .query(actionBaseQuery)
                .test()
                .assertNext {
                    it.size shouldBe 1
                    it.keys shouldBe setOf(queryName)
                    val df = it[queryName]!!
                    df
                        .toRowWithSchema()
                        .map { row -> row.getOrNull("permission") } shouldContainExactly
                        listOf(
                            "me",
                            "me",
                            "na",
                            "na",
                            "others",
                            "others",
                        )
                }.verifyComplete()
        }

        "Complex Query 1" {
            val service = hbase.name.service
            val label = hbase.name.nameNotNull

            val srcUser = 100

            // language=json
            val actionBaseQueryString =
                """
                {
                  "query": [
                    {
                      "type": "SCAN",
                      "name": "step1",
                      "service": "$service",
                      "label": "$label",
                      "src": {
                        "type": "VALUE",
                        "value": [$srcUser]
                      },
                      "dir": "OUT",
                      "index": "created_at_desc",
                      "limit": 10,
                      "include": true
                    },
                    {
                      "type": "SCAN",
                      "name": "step2",
                      "service": "$service",
                      "label": "$label",
                      "src": {
                        "type": "REF",
                        "ref": "step1",
                        "field": "tgt"
                      },
                      "dir": "IN",
                      "index": "created_at_desc",
                      "limit": 10,
                      "include": true
                    }
                  ]
                }
                """.trimIndent()
            val actionBaseQuery = ActionbaseQuery.from(actionBaseQueryString)

            graph
                .query(actionBaseQuery)
                .test()
                .assertNext {
                    it.size shouldBe 2
                    it.keys shouldBe setOf("step1", "step2")
                    val step1 = it["step1"]!!
                    step1.show()
                    val step2 = it["step2"]!!
                    step2.show()
                }.verifyComplete()
        }
    })
