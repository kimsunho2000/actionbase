package com.kakao.actionbase.server.util

import com.kakao.actionbase.v2.engine.label.EdgeOperationStatus
import com.kakao.actionbase.v2.engine.service.ddl.DdlResult

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

fun <T> DdlResult<T>.toResponseEntity(): ResponseEntity<DdlResult<T>> =
    when (status) {
        EdgeOperationStatus.CREATED -> ResponseEntity.status(HttpStatus.CREATED).body(this)
        EdgeOperationStatus.UPDATED -> ResponseEntity.ok(this)
        EdgeOperationStatus.DELETED -> ResponseEntity.status(HttpStatus.ACCEPTED).body(this)
        else -> ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(this)
    }

fun <T> T?.toResponseEntity(): ResponseEntity<T> =
    this?.let {
        ResponseEntity.ok(it)
    } ?: ResponseEntity.notFound().build()
