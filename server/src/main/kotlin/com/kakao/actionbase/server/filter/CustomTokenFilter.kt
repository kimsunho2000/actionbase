package com.kakao.actionbase.server.filter

interface CustomTokenFilter {
    fun isValid(token: String): Boolean

    fun warmUp()
}
