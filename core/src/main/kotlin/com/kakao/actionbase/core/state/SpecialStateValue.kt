package com.kakao.actionbase.core.state

enum class SpecialStateValue(
    private val stateValue: String,
) {
    DELETED("__DELETED__"),
    UNSET("__UNSET__"),
    ;

    fun code(): String = stateValue

    companion object {
        fun isSpecialStateValue(code: Any?): Boolean =
            if (code is String) {
                code == DELETED.code() || code == UNSET.code()
            } else {
                false
            }

        fun getSpecialStateValue(code: String): SpecialStateValue =
            when (code) {
                DELETED.code() -> DELETED
                UNSET.code() -> UNSET
                else -> throw IllegalArgumentException("Invalid special state value: $code")
            }
    }
}
