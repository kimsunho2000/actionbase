package com.kakao.actionbase.core.storage

import com.kakao.actionbase.core.Constants

class HBaseRecord(
    val key: ByteArray,
    val qualifier: ByteArray = Constants.DEFAULT_QUALIFIER,
    val value: ByteArray,
)
