package com.kakao.actionbase.core.storage

sealed class MutationRequest {
    class Put(
        val key: ByteArray,
        val value: ByteArray,
    ) : MutationRequest()

    class Delete(
        val key: ByteArray,
    ) : MutationRequest()

    class Increment(
        val key: ByteArray,
        val value: Long,
    ) : MutationRequest()
}
