package com.kakao.actionbase.v2.engine

import java.nio.file.Files
import java.nio.file.Paths

fun getResourceFileContent(fileName: String): String {
    val classLoader = Thread.currentThread().contextClassLoader
    val resource = classLoader.getResource(fileName) ?: throw IllegalArgumentException("File not found! $fileName")
    val path = Paths.get(resource.toURI())
    return String(Files.readAllBytes(path))
}
