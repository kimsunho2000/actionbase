package com.kakao.actionbase.v2.engine.util

import org.slf4j.Logger
import org.slf4j.LoggerFactory

inline fun <reified R : Any> R.getLogger(): Logger = LoggerFactory.getLogger(this::class.java.name.substringBefore("\$Companion"))
