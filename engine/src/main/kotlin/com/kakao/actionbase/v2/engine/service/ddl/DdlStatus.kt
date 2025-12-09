package com.kakao.actionbase.v2.engine.service.ddl

import com.kakao.actionbase.v2.engine.label.EdgeOperationStatus

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

@JsonInclude(JsonInclude.Include.NON_NULL)
data class DdlStatus<ResultEntity>(
    val status: Status,
    val result: ResultEntity? = null,
    val message: String? = null,
) {
    enum class Status {
        ERROR,
        IDLE,
        CREATED,
        UPDATED,
        DELETED,
        BAD_REQUEST,
    }

    override fun toString(): String {
        val mapper = jacksonObjectMapper()
        return mapper.writeValueAsString(this)
    }

    companion object {
        fun <ResultEntity> fromEdgeOperationStatus(
            status: EdgeOperationStatus,
            result: ResultEntity? = null,
            message: String? = null,
        ): DdlStatus<ResultEntity> =
            when (status) {
                EdgeOperationStatus.ERROR -> DdlStatus(Status.ERROR, result, message)
                EdgeOperationStatus.IDLE -> DdlStatus(Status.IDLE, result, message)
                EdgeOperationStatus.CREATED -> DdlStatus(Status.CREATED, result, message)
                EdgeOperationStatus.UPDATED -> DdlStatus(Status.UPDATED, result, message)
                EdgeOperationStatus.DELETED -> DdlStatus(Status.DELETED, result, message)
                else -> throw IllegalArgumentException("Unsupported ddl status: $status")
            }
    }
}
