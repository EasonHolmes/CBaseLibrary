package com.ethan.clibrary.entity.response

data class RetrofitFlowResponse(
//    val children: MutableList<Any> = mutableListOf(),
    val courseId: Int=0,
    val id: Int=0,
    val name: String="",
    val order: Int=0,
    val parentChapterId: Int=0,
    val userControlSetTop: Boolean=false,
    val visible: Int=0
)
