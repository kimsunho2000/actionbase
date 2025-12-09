package com.kakao.actionbase.core.edge.payload

data class MultiEdgeMutationResponse(
    val results: List<Item>,
) {
    data class Item(
        val id: Any,
        val status: String,
        val count: Int,
    )
}
