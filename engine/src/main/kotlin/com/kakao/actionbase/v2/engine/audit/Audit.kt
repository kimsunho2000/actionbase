package com.kakao.actionbase.v2.engine.audit

data class Audit(
    val actor: String,
) {
    companion object {
        val default = Audit("default")
    }
}
