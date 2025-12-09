package com.kakao.actionbase.v2.engine.service.ddl

import com.kakao.actionbase.v2.core.edge.TraceEdge
import com.kakao.actionbase.v2.engine.audit.Audit
import com.kakao.actionbase.v2.engine.entity.EntityName

interface DdlRequest {
    fun toEdge(name: EntityName): TraceEdge

    val audit: Audit
}
