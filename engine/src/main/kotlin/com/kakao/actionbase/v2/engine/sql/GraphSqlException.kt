package com.kakao.actionbase.v2.engine.sql

open class GraphSqlException(
    message: String,
) : Exception(message)

class InvalidRowDataException(
    message: String,
) : GraphSqlException(message)
