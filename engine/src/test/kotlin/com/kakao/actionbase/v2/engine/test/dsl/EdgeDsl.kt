package com.kakao.actionbase.v2.engine.test.dsl

import com.kakao.actionbase.v2.core.edge.Edge
import com.kakao.actionbase.v2.core.edge.TraceEdge

@GraphDsl
class EdgeDsl(
    val src: Any,
    val tgt: Any,
) {
    var ts: Long = System.currentTimeMillis()
    var props: Map<String, Any?> = emptyMap()

    fun build(): TraceEdge = Edge(ts, src, tgt, props).toTraceEdge()

    @GraphDsl
    fun property(
        keys: List<String>,
        vararg values: Any?,
    ) {
        props = keys.zip(values.toList()).toMap()
    }
}

@GraphDsl
fun edge(
    src: Any,
    tgt: Any,
    block: EdgeDsl.() -> Unit = {},
): EdgeDsl {
    val dsl = EdgeDsl(src, tgt)
    dsl.apply(block)
    return dsl
}
