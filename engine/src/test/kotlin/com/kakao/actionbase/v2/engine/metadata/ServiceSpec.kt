package com.kakao.actionbase.v2.engine.metadata

import com.kakao.actionbase.v2.engine.Graph
import com.kakao.actionbase.v2.engine.entity.EntityName
import com.kakao.actionbase.v2.engine.service.ddl.DdlStatus
import com.kakao.actionbase.v2.engine.service.ddl.ServiceDeleteRequest
import com.kakao.actionbase.v2.engine.service.ddl.ServiceUpdateRequest
import com.kakao.actionbase.v2.engine.test.GraphFixtures
import com.kakao.actionbase.v2.engine.test.shouldContainServicesExactly
import com.kakao.actionbase.v2.engine.test.testFixtures

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import reactor.kotlin.test.test

class ServiceSpec :
    StringSpec({

        lateinit var graph: Graph

        beforeTest {
            graph = GraphFixtures.create(withTestData = false)
        }

        afterTest {
            graph.close()
        }

        "getAll" {
            graph shouldContainServicesExactly GraphFixtures.defaultServices
        }

        "create" {
            graph.testFixtures.createService(EntityName.fromOrigin("test2"))
            graph shouldContainServicesExactly GraphFixtures.defaultServices + EntityName.fromOrigin("test2")
        }

        "get" {
            graph.testFixtures.createService(EntityName.fromOrigin("test2"))

            graph.serviceDdl
                .getSingle(EntityName.fromOrigin("sys"))
                .test()
                .assertNext { entity ->
                    entity.name shouldBe EntityName.fromOrigin("sys")
                }.verifyComplete()

            graph.serviceDdl
                .getSingle(EntityName.fromOrigin("test2"))
                .test()
                .assertNext { entity ->
                    entity.name shouldBe EntityName.fromOrigin("test2")
                }.verifyComplete()
        }

        "update" {
            graph.testFixtures.createService(EntityName.fromOrigin("test2"))

            val updateRequest =
                ServiceUpdateRequest(
                    active = null,
                    desc = "updated desc",
                )

            graph.serviceDdl
                .update(EntityName.fromOrigin("test2"), updateRequest)
                .test()
                .assertNext { ddlResult ->
                    ddlResult.status shouldBe DdlStatus.Status.UPDATED
                }.verifyComplete()

            graph.serviceDdl
                .getSingle(EntityName.fromOrigin("test2"))
                .test()
                .assertNext { entity ->
                    entity.desc shouldBe updateRequest.desc
                }.verifyComplete()
        }

        "update on non-existing service should be idle" {
            val updateRequest =
                ServiceUpdateRequest(
                    active = null,
                    desc = "updated desc",
                )

            graph.serviceDdl
                .update(EntityName.fromOrigin("test2"), updateRequest)
                .test()
                .assertNext { ddlResult ->
                    ddlResult.status shouldBe DdlStatus.Status.IDLE
                }.verifyComplete()
        }

        "update should not allow mutation on LocalBackedJdbcHashLabel" {
            val updateRequest =
                ServiceUpdateRequest(
                    active = null,
                    desc = "updated desc",
                )

            graph.serviceDdl
                .update(EntityName.fromOrigin("sys"), updateRequest)
                .test()
                .assertNext { ddlResult ->
                    ddlResult.status shouldBe DdlStatus.Status.IDLE
                }.verifyComplete()
        }

        "delete" {
            graph.testFixtures.createService(EntityName.fromOrigin("test2"))

            graph shouldContainServicesExactly GraphFixtures.defaultServices + EntityName.fromOrigin("test2")

            graph.serviceDdl
                .delete(EntityName.fromOrigin("test2"), ServiceDeleteRequest())
                .test()
                .verifyError(IllegalArgumentException::class.java)

            graph shouldContainServicesExactly GraphFixtures.defaultServices + EntityName.fromOrigin("test2")
        }

        "delete on non-existing service should be idle" {
            graph.serviceDdl
                .delete(EntityName.fromOrigin("test2"), ServiceDeleteRequest())
                .test()
                .assertNext { ddlResult ->
                    ddlResult.status shouldBe DdlStatus.Status.IDLE
                }.verifyComplete()
        }

        "delete should not allow mutation on LocalBackedJdbcHashLabel" {
            graph.serviceDdl
                .delete(EntityName.fromOrigin("sys"), ServiceDeleteRequest())
                .test()
                .verifyError(IllegalArgumentException::class.java)
        }
    })
