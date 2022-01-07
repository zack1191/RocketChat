package com.hex.chattie.network.responses

data class CreateDirectMessageResponse(
    val message: String,
    val success: Boolean
)