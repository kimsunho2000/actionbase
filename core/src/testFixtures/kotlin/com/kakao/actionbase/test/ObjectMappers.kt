package com.kakao.actionbase.test

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule

object ObjectMappers {
    val JSON: JsonMapper =
        JsonMapper
            .builder()
            .addModule(kotlinModule())
            .build()

    val YAML: YAMLMapper =
        YAMLMapper
            .builder()
            .addModule(kotlinModule())
            .build()
}
