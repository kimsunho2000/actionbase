package com.kakao.actionbase.core.metadata.common

import com.kakao.actionbase.core.Constants
import com.kakao.actionbase.core.codec.XXHash32Wrapper

import com.fasterxml.jackson.annotation.JsonIgnore

data class Index(
    val index: String,
    val fields: List<IndexField>,
    val comment: String = Constants.DEFAULT_COMMENT,
    val primary: Long = -1L,
    val batch: Long = 0L,
) {
    @JsonIgnore
    val code = XXHash32Wrapper.default.stringHash(index)
}
