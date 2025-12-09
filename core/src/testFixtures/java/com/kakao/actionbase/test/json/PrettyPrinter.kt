package com.kakao.actionbase.test.json

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.util.MinimalPrettyPrinter

class PrettyPrinter : MinimalPrettyPrinter() {
    override fun writeStartObject(g: JsonGenerator) {
        g.writeRaw("{")
    }

    override fun writeEndObject(
        g: JsonGenerator,
        nrOfEntries: Int,
    ) {
        g.writeRaw("}")
    }

    override fun writeStartArray(g: JsonGenerator) {
        g.writeRaw("[")
    }

    override fun writeEndArray(
        g: JsonGenerator,
        nrOfValues: Int,
    ) {
        g.writeRaw("]")
    }

    override fun writeObjectEntrySeparator(g: JsonGenerator) {
        g.writeRaw(", ")
    }

    override fun writeObjectFieldValueSeparator(g: JsonGenerator) {
        g.writeRaw(": ")
    }

    override fun writeArrayValueSeparator(g: JsonGenerator) {
        g.writeRaw(", ")
    }
}
