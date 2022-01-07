package com.hex.chattie.network.models

data class Result(
    val _id: String,
    val _updatedAt: UpdatedAt,
    val broadcast: Boolean,
    val customFields: CustomFields,
    val default: Boolean,
    val description: String,
    val encrypted: Boolean,
    val fname: String,
    val lastMessage: LastMessage?,
    val lm: Lm,
    val name: String,
    val ro: Boolean,
    val sysMes: Boolean,
    val t: String,
    val ts: TsX,
    val u: UX,
    val usernames: List<Any>?,
    val usersCount: Int
)