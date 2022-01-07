package com.hex.chattie.network.responses

data class MeResponse(
    val _id: String,
    val _updatedAt: String,
    val active: Boolean,
    val avatarUrl: String,
    val emails: List<Email>,
    val name: String,
    val roles: List<String>,
    val services: Services,
    val settings: Settings,
    val status: String,
    val success: Boolean,
    val username: String
                     )