package com.kakao.actionbase.test.documentations

import org.junit.jupiter.api.ClassDescriptor
import org.junit.jupiter.api.ClassOrderer
import org.junit.jupiter.api.ClassOrdererContext

class SectionClassOrderer : ClassOrderer {
    override fun orderClasses(context: ClassOrdererContext) {
        context.classDescriptors.sortWith(Comparator.comparingInt(Companion::getOrder))
    }

    companion object {
        private fun getOrder(descriptor: ClassDescriptor): Int = descriptor.findAnnotation(SectionOrder::class.java).map { obj: SectionOrder -> obj.value }.orElse(Int.MAX_VALUE) as Int
    }
}
