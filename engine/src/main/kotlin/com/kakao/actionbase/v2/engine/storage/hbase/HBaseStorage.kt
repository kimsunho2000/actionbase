package com.kakao.actionbase.v2.engine.storage.hbase

import com.kakao.actionbase.v2.engine.entity.StorageEntity
import com.kakao.actionbase.v2.engine.storage.Storage

class HBaseStorage(
    override val entity: StorageEntity,
) : Storage<HBaseOptions> {
    override val options: HBaseOptions = Storage.parseOptions(entity.conf)
}
