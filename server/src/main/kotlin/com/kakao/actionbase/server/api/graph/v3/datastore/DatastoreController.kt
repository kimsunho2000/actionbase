package com.kakao.actionbase.server.api.graph.v3.datastore

import com.kakao.actionbase.core.metadata.DatastoreDescriptor
import com.kakao.actionbase.server.configuration.ServerProperties

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class DatastoreController(
    private val serverProperties: ServerProperties,
) {
    @GetMapping("/graph/v3/datastore")
    fun getDatastoreDescriptor(): DatastoreDescriptor = serverProperties.datastore.toDescriptor()
}
