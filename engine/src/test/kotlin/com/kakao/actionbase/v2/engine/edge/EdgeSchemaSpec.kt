package com.kakao.actionbase.v2.engine.edge

import com.kakao.actionbase.v2.core.types.DataType
import com.kakao.actionbase.v2.core.types.EdgeSchema
import com.kakao.actionbase.v2.core.types.Field
import com.kakao.actionbase.v2.core.types.VertexField
import com.kakao.actionbase.v2.core.types.VertexType

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import reactor.blockhound.BlockHound

class EdgeSchemaSpec :
    StringSpec({

        beforeTest {
            BlockHound.install()
        }

        "serialize and deserialize EdgeSchema" {
            val objectMapper = jacksonObjectMapper()
            val schemaWithAllTypes =
                EdgeSchema(
                    VertexField(VertexType.STRING),
                    VertexField(VertexType.LONG),
                    listOf(
                        Field("boolean", DataType.BOOLEAN, false),
                        Field("string", DataType.STRING, false),
                        Field("byte", DataType.BYTE, false),
                        Field("short", DataType.SHORT, false),
                        Field("int", DataType.INT, false),
                        Field("long", DataType.LONG, false),
                        Field("float", DataType.FLOAT, false),
                        Field("double", DataType.DOUBLE, false),
                        Field("decimal", DataType.DECIMAL, false),
                        Field("json", DataType.JSON, false),
                    ),
                )

            val serialized = objectMapper.writeValueAsString(schemaWithAllTypes)
            val deserialized: EdgeSchema = objectMapper.readValue(serialized)

            deserialized shouldBe schemaWithAllTypes
        }
    })
