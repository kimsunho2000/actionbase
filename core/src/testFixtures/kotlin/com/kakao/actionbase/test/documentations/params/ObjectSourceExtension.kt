package com.kakao.actionbase.test.documentations.params

import com.kakao.actionbase.test.ObjectMappers

import java.util.stream.Stream

import kotlin.reflect.full.memberFunctions

import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.TestTemplateInvocationContext
import org.junit.jupiter.api.extension.TestTemplateInvocationContextProvider

import com.fasterxml.jackson.module.kotlin.readValue

class ObjectSourceExtension : TestTemplateInvocationContextProvider {
    override fun supportsTestTemplate(context: ExtensionContext): Boolean = context.requiredTestMethod.isAnnotationPresent(ObjectSource::class.java)

    override fun provideTestTemplateInvocationContexts(context: ExtensionContext): Stream<TestTemplateInvocationContext> {
        val annotation = context.requiredTestMethod.getAnnotation(ObjectSource::class.java)
        val testCases: List<Map<String, Any?>> = ObjectMappers.YAML.readValue(annotation.value)

        val parameterNames = getParameterNames(context.requiredTestClass, context.requiredTestMethod.name)

        return testCases
            .mapIndexed { index, testCase ->
                ObjectSourceInvocationContext(index + 1, testCases.size, parameterNames, testCase) as TestTemplateInvocationContext
            }.stream()
    }

    private fun getParameterNames(
        testClass: Class<*>,
        methodName: String,
    ): List<String> =
        testClass.kotlin.memberFunctions
            .find { it.name == methodName }
            ?.parameters
            ?.drop(1) // drop 'this'
            ?.mapNotNull { it.name }
            ?: emptyList()
}
