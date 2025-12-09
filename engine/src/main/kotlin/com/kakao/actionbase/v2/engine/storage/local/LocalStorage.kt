package com.kakao.actionbase.v2.engine.storage.local

import com.kakao.actionbase.v2.engine.entity.StorageEntity
import com.kakao.actionbase.v2.engine.storage.Storage
import com.kakao.actionbase.v2.engine.storage.Storage.Companion.parseOptions

class LocalStorage(
    override val entity: StorageEntity,
) : Storage<LocalOptions> {
    override val options: LocalOptions = parseOptions(entity.conf)
}
