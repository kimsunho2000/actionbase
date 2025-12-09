package com.kakao.actionbase.server.payload

import com.kakao.actionbase.v2.core.edge.Edge
import com.kakao.actionbase.v2.core.edge.EdgeValue
import com.kakao.actionbase.v2.engine.audit.Audit

data class EdgesRequest(
    val edges: List<Edge>,
    val audit: Audit = Audit.default,
)

/**
 * This is the request for [com.kakao.actionbase.server.edge.EdgeValue]
 */
data class EdgeValueRequest(
    val ts: Long,
    val props: Map<String, Any>?,
    val audit: Audit = Audit.default,
) {
    fun toEdgeValue(): EdgeValue = EdgeValue(ts, props)
}
