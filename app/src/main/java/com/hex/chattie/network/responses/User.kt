package com.hex.chattie.network.responses

data class User(
    val _id: String,
    val active: Boolean,
    val avatarETag: Any,
    val name: String,
    val nameInsensitive: String,
    val status: String,
    val username: String?
)