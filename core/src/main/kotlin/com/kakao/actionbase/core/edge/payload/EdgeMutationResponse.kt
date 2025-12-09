package com.kakao.actionbase.core.edge.payload

data class EdgeMutationResponse(
    val results: List<Item>,
) {
    data class Item(
        val source: Any,
        val target: Any,
        val status: String,
        val count: Int,
    )
}
