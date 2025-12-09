package com.kakao.actionbase.core.payload

import com.kakao.actionbase.core.edge.Edge
import com.kakao.actionbase.core.edge.payload.EdgeBulkMutationRequest
import com.kakao.actionbase.core.metadata.common.DirectionType
import com.kakao.actionbase.core.metadata.common.Field
import com.kakao.actionbase.core.metadata.common.ModelSchema
import com.kakao.actionbase.core.metadata.common.StructField
import com.kakao.actionbase.core.state.EventType
import com.kakao.actionbase.core.types.PrimitiveType

import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

import org.junit.jupiter.api.Test

class EdgeBulkMutationRequestTest {
    @Test
    fun `createEvent should preserve explicit null values in properties`() {
        // given
        val schema =
            ModelSchema.Edge(
                source = Field(PrimitiveType.STRING, "source"),
                target = Field(PrimitiveType.STRING, "target"),
                properties =
                    listOf(
                        StructField(name = "prop1", type = PrimitiveType.STRING, "Property 1", nullable = false),
                        StructField(name = "prop2", type = PrimitiveType.STRING, "Property 2", nullable = true),
                        StructField(name = "prop2", type = PrimitiveType.STRING, "Property 2", nullable = true),
                    ),
                direction = DirectionType.OUT,
                indexes = emptyList(),
                groups = emptyList(),
            )

        val edge =
            Edge(
                source = "sourceId",
                target = "targetId",
                version = 1L,
                properties =
                    mapOf(
                        "prop1" to "value1",
                        "prop2" to null,
                    ),
            )

        val mutationItem =
            EdgeBulkMutationRequest.MutationItem(
                type = EventType.INSERT,
                edge = edge,
            )

        // when
        val event = mutationItem.createEvent(schema)

        // then
        with(event.event.properties) {
            assertEquals(2, size)
            assertEquals("value1", this["prop1"])
            assertTrue(containsKey("prop2"))
            assertNull(this["prop2"])
        }
    }
}
