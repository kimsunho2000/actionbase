package com.kakao.actionbase.v2.engine.storage

import com.kakao.actionbase.v2.engine.entity.StorageEntity

object DatastoreStorage : Storage<Unit> {
    override val entity: StorageEntity
        get() = throw IllegalAccessException()
    override val options: Unit
        get() = throw IllegalAccessException()
}
