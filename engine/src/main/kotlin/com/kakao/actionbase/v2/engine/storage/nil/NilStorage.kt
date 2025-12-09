package com.kakao.actionbase.v2.engine.storage.nil

import com.kakao.actionbase.v2.engine.entity.StorageEntity
import com.kakao.actionbase.v2.engine.storage.Storage

data class NilStorage(
    override val entity: StorageEntity,
) : Storage<NilOptions> {
    override val options: NilOptions = NilOptions()
}
