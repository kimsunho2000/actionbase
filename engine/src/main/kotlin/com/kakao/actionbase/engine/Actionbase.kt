package com.kakao.actionbase.engine

import com.kakao.actionbase.engine.datastore.Datastore

import org.slf4j.LoggerFactory

class Actionbase(
    datastore: Datastore,
) {
    private val logger = LoggerFactory.getLogger(Actionbase::class.java)

    init {
        logger.info("Actionbase initialized with datastore: ${datastore::class.java.simpleName}")
    }
}
