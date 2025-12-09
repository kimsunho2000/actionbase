package com.kakao.actionbase.core.edge.record

sealed class EdgeRecord {
    abstract val key: Key

    val recordTypeCode: Byte
        get() = key.recordTypeCode

    sealed class Key {
        abstract val recordTypeCode: Byte

        data class CommonPrefix(
            val source: Any,
            val tableCode: Int,
            val recordTypeCode: Byte,
        ) {
            val recordType: EdgeRecordType
                get() = EdgeRecordType.of(recordTypeCode)
        }
    }
}
