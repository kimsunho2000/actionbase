package com.kakao.actionbase.server.configuration

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty

/**
 * Conditional annotation that creates beans only when HBase Datastore is enabled
 *
 * Only applies when `actionbase.datastore.type=HBASE`.
 * Prevents HBase-related beans from being created when using a Datastore type other than HBase.
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@ConditionalOnProperty(
    prefix = "actionbase.datastore",
    name = ["type"],
    havingValue = "HBASE",
    matchIfMissing = false,
)
annotation class ConditionalOnHBaseDatastore
