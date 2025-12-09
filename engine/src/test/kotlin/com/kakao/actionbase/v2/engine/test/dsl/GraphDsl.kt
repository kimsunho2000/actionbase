package com.kakao.actionbase.v2.engine.test.dsl

import com.kakao.actionbase.v2.engine.Graph
import com.kakao.actionbase.v2.engine.entity.EntityName

@DslMarker
@Target(AnnotationTarget.CLASS, AnnotationTarget.TYPEALIAS, AnnotationTarget.TYPE, AnnotationTarget.FUNCTION)
annotation class GraphDsl

@GraphDsl
fun <T> Graph.dml(
    name: EntityName,
    block: DmlScope.() -> T,
): T = DmlScope(getLabel(name)).block()
