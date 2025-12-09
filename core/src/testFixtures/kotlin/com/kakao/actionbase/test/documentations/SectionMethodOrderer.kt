package com.kakao.actionbase.test.documentations

import org.junit.jupiter.api.MethodDescriptor
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.MethodOrdererContext

class SectionMethodOrderer : MethodOrderer {
    override fun orderMethods(context: MethodOrdererContext) {
        context.methodDescriptors.sortWith(Comparator.comparingInt(Companion::getOrder))
    }

    companion object {
        private fun getOrder(descriptor: MethodDescriptor): Int = descriptor.findAnnotation(SectionOrder::class.java).map { obj: SectionOrder -> obj.value }.orElse(Int.MAX_VALUE) as Int
    }
}
