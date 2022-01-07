package com.hex.chattie.network.responses

data class SendMessageResponse(
    val message: String,
    val success: Boolean
)