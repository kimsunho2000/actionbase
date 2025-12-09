package com.kakao.actionbase.engine.context

data class RequestContext(
    val requestId: String,
    val actor: String,
) {
    companion object {
        val DEFAULT = RequestContext("-", "-")
    }
}
