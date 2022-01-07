package com.hex.chattie.network.responses

data class CheckRoomNameExistsResponse(
    val message: String,
    val success: Boolean
)