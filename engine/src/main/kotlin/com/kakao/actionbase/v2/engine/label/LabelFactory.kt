package com.kakao.actionbase.v2.engine.label

import com.kakao.actionbase.v2.engine.GraphDefaults
import com.kakao.actionbase.v2.engine.entity.LabelEntity
import com.kakao.actionbase.v2.engine.storage.Storage

interface LabelFactory<L : Label, S : Storage<*>> {
    fun create(
        entity: LabelEntity,
        graph: GraphDefaults,
        storage: S,
        block: L.() -> Unit = {},
    ): L
}
