package com.kakao.actionbase.core.edge.record

class EdgeInvalidRecord(
    recordTypeCode: Byte,
) : EdgeRecord() {
    override val key = Key(recordTypeCode)

    class Key(
        override val recordTypeCode: Byte,
    ) : EdgeRecord.Key()
}
