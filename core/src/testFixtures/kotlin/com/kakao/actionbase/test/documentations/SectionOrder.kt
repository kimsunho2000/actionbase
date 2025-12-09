package com.kakao.actionbase.test.documentations

@Retention
@MustBeDocumented
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
annotation class SectionOrder(
    val value: Int = DEFAULT,
) {
    companion object {
        const val DEFAULT: Int = 1073741823
    }
}
