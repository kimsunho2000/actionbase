package com.kakao.actionbase.core.state

data class StateValue(
    val version: Long,
    val value: Any?,
) {
    fun isDeleted(): Boolean = SpecialStateValue.DELETED.code() == value

    fun isUnset(): Boolean = SpecialStateValue.UNSET.code() == value

    fun isPresent(): Boolean = value != null && !isDeleted() && !isUnset()

    companion object {
        fun unsetOf(version: Long) = StateValue(version, SpecialStateValue.UNSET.code())

        fun deletedOf(version: Long) = StateValue(version, SpecialStateValue.DELETED.code())
    }
}
