package com.kakao.actionbase.core.edge.mutation

import com.kakao.actionbase.core.edge.record.EdgeCountRecord
import com.kakao.actionbase.core.edge.record.EdgeGroupRecord
import com.kakao.actionbase.core.edge.record.EdgeIndexRecord
import com.kakao.actionbase.core.edge.record.EdgeStateRecord

data class EdgeMutationRecords(
    val status: String,
    val acc: Long,
    val stateRecord: EdgeStateRecord,
    val createIndexRecords: List<EdgeIndexRecord>,
    val deleteIndexRecordKeys: List<EdgeIndexRecord.Key>,
    val countRecords: List<EdgeCountRecord>,
    val groupRecords: List<EdgeGroupRecord>,
)
