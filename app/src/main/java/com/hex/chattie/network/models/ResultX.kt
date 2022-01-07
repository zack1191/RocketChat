package com.hex.chattie.network.models

data class ResultX(
    val messages: List<Message>,
    val unreadNotLoaded: Int
)