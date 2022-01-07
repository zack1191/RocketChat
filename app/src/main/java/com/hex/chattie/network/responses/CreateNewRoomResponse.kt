package com.hex.chattie.network.responses

data class CreateNewRoomResponse(
    val details: Details?,
    val error: String?,
    val errorType: String?,
    val group: Group?,
    val success: Boolean
)