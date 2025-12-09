package com.kakao.actionbase.v2.engine.service.ddl

import com.kakao.actionbase.v2.engine.entity.EntityName

object DdlExceptionMessage {
    fun serviceNotExists(service: String) = "service not exists : $service"

    fun aliasNameAlreadyExists(alias: EntityName) = "alias name already exists : ${alias.fullQualifiedName}"

    fun labelNameAlreadyExists(label: EntityName) = "label name already exists : ${label.fullQualifiedName}"

    fun targetLabelNotExists(target: EntityName) = "target label not exists : ${target.fullQualifiedName}"

    fun storageNotExists(storage: EntityName) = "storage not exists : ${storage.fullQualifiedName}"

    fun entityAlreadyExists(name: EntityName) = "entity already exists : ${name.fullQualifiedName}"

    fun entityNotDeactivate(name: EntityName) = "entity not deactivate : ${name.fullQualifiedName}"

    fun entityNotDeactivatable(name: EntityName) = "entity cannot be deactivated : ${name.fullQualifiedName}"
}
