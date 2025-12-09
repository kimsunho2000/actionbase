package com.kakao.actionbase.v2.engine.storage.jdbc

import com.kakao.actionbase.v2.engine.entity.StorageEntity
import com.kakao.actionbase.v2.engine.storage.Storage

class JdbcStorage(
    override val entity: StorageEntity,
) : Storage<JdbcOptions> {
    override val options: JdbcOptions = Storage.parseOptions(entity.conf)
}
