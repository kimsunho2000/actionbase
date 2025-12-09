package com.kakao.actionbase.v2.engine.metadata.sync

import com.kakao.actionbase.v2.core.types.EdgeSchema
import com.kakao.actionbase.v2.engine.entity.EntityName
import com.kakao.actionbase.v2.engine.sql.RowWithSchema

object MetadataSyncEntity {
    data class Src(
        val phase: String,
        val metadataType: MetadataType,
    ) {
        fun toCompositeKey(): String = "$phase:$metadataType"

        companion object {
            fun of(src: String): Src {
                val (phase, metadataType) = src.split(":")
                return Src(phase, MetadataType.of(metadataType))
            }

            fun of(row: RowWithSchema): Src {
                val src = row.getString(EdgeSchema.Fields.SRC)
                return of(src)
            }
        }
    }

    data class Tgt(
        val hostName: String,
        val commitId: String,
        val entityName: EntityName,
    ) {
        fun toCompositeKey(): String = "$hostName:$commitId:$entityName"

        companion object {
            fun of(tgt: String): Tgt {
                val split = tgt.split(":")
                val (hostName, commitId, name) =
                    when (split.size) {
                        2 -> listOf(split[0], "", split[1]) // backward compatibility
                        3 -> split
                        else -> throw IllegalArgumentException("Invalid tgt: $tgt")
                    }
                val entityName =
                    EntityName.of(name).let {
                        if (it.name == null) EntityName.fromOrigin(it.service) else it
                    }
                return Tgt(hostName, commitId, entityName)
            }

            fun of(row: RowWithSchema): Tgt {
                val tgt = row.getString(EdgeSchema.Fields.TGT)
                return of(tgt)
            }
        }
    }

    data class Props(
        val hash: Int,
    ) {
        fun toMap(): Map<String, Any> = mapOf("hash" to hash)

        companion object {
            fun of(row: RowWithSchema): Props {
                val hash = row.getOrNull("hash") as? Int ?: 0
                return Props(hash)
            }
        }
    }
}
