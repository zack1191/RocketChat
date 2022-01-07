package com.hex.chattie.network.responses

data class Group(
    val _id: String,
    val _updatedAt: String,
    val broadcast: Boolean,
    val customFields: CustomFields,
    val default: Boolean,
    val description: String,
    val encrypted: Boolean,
    val fname: String,
    val msgs: Int,
    val name: String,
    val ro: Boolean,
    val sysMes: Boolean,
    val t: String,
    val ts: String,
    val u: U,
    val usersCount: Int
)