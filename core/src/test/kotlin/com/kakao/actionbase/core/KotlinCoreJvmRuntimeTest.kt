package com.kakao.actionbase.core

import com.kakao.actionbase.core.java.inspect.JvmRuntimeInspector

import org.junit.jupiter.api.Test

class KotlinCoreJvmRuntimeTest {
    @Test
    fun `test inspect core`() {
        val core = JvmRuntimeInspector.inspectCore()
        val kotlinCore = JvmRuntimeInspector.inspect(KotlinCoreJvmRuntimeTest::class.java)

        assert(core.isJava8) { "Core should be running on Java 8" }
        assert(kotlinCore.isJava8) { "Kotlin core should be running on Java 8" }
    }
}
