package com.hex.chattie.network.models

data class ChannelRoom(
    val id: String,
    val msg: String,
    val result: List<Result>
)