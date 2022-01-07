package com.hex.chattie.network.models

data class CreateRoom(
    val extraData: ExtraData,
    val members: List<String>,
    val name: String,
    val readOnly: Boolean
)