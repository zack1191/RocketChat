package com.hex.chattie.network.responses

data class GetUserListResponse(
    val count: Int,
    val offset: Int,
    val success: Boolean,
    val total: Int,
    val users: List<User>
)