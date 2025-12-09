package com.kakao.actionbase.core.v2.edge

import com.kakao.actionbase.core.edge.Edge

data class V2Edge(
    val ts: Long,
    val src: Any,
    val tgt: Any,
    val props: Map<String, Any?>,
) {
    fun toV3(): Edge =
        Edge(
            version = ts,
            source = src,
            target = tgt,
            properties = props,
        )

    companion object {
        fun fromV3(edge: Edge): V2Edge =
            V2Edge(
                ts = edge.version,
                src = edge.source,
                tgt = edge.target,
                props = edge.properties,
            )
    }
}
