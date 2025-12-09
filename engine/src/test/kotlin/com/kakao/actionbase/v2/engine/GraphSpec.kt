package com.kakao.actionbase.v2.engine

import com.kakao.actionbase.v2.core.metadata.DirectionType
import com.kakao.actionbase.v2.core.metadata.LabelType
import com.kakao.actionbase.v2.core.types.EdgeSchema
import com.kakao.actionbase.v2.core.types.VertexField
import com.kakao.actionbase.v2.core.types.VertexType
import com.kakao.actionbase.v2.engine.entity.EntityName
import com.kakao.actionbase.v2.engine.label.EdgeOperationStatus
import com.kakao.actionbase.v2.engine.service.ddl.AliasCreateRequest
import com.kakao.actionbase.v2.engine.service.ddl.AliasUpdateRequest
import com.kakao.actionbase.v2.engine.service.ddl.DdlStatus
import com.kakao.actionbase.v2.engine.service.ddl.LabelCreateRequest
import com.kakao.actionbase.v2.engine.test.GraphFixtures

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import reactor.kotlin.test.test

class GraphSpec :
    StringSpec({
        lateinit var graph: Graph
        beforeTest {
            graph = GraphFixtures.create()
        }

        afterTest {
            graph.close()
        }

        "updateAlias fallback test" {

            val testAliasEntityName = EntityName(GraphFixtures.serviceName, "test_alias")

            val oldLabelEntityName = EntityName(GraphFixtures.serviceName, GraphFixtures.hbaseHash)
            val newLabelEntityName = EntityName(GraphFixtures.serviceName, "test_label")

            graph.aliasDdl
                .create(
                    testAliasEntityName,
                    AliasCreateRequest("test", oldLabelEntityName.fullQualifiedName),
                ).test()
                .assertNext { result ->
                    result.status shouldBe DdlStatus.Status.CREATED
                }.verifyComplete()

            graph.labelDdl
                .createMetadataOnlyForTest(
                    newLabelEntityName,
                    LabelCreateRequest(
                        "desc",
                        LabelType.HASH,
                        EdgeSchema(
                            VertexField(VertexType.LONG),
                            VertexField(VertexType.LONG),
                            listOf(),
                        ),
                        DirectionType.OUT,
                        GraphFixtures.hbaseStorage,
                        emptyList(),
                        emptyList(),
                        event = false,
                        readOnly = false,
                    ),
                ).test()
                .assertNext { it.result.first().status shouldBe EdgeOperationStatus.CREATED }
                .verifyComplete()

            // Performs updateAliases while performing update
            graph.aliasDdl
                .update(
                    testAliasEntityName,
                    AliasUpdateRequest(active = null, desc = null, target = newLabelEntityName.fullQualifiedName),
                ).test()
                .assertNext { result ->
                    result.status shouldBe DdlStatus.Status.UPDATED
                }.verifyComplete()

            graph.getLabel(testAliasEntityName).name shouldBe oldLabelEntityName
        }
    })
