package com.kakao.actionbase.test.documentations

import org.junit.jupiter.api.extension.ExtensionContext

fun <T : Annotation> ExtensionContext.getAnnotation(annotationClass: Class<T>): T? =
    this.testMethod
        .filter { it.isAnnotationPresent(annotationClass) }
        .map { it.getAnnotation(annotationClass) }
        .orElseGet { null }
