package com.hex.chattie.network.models

data class LastMessage(
    val _id: String,
    val _updatedAt: UpdatedAtX,
    val channels: List<Any>,
    val md: List<Md>,
    val mentions: List<Any>,
    val msg: String?,
    val rid: String,
    val ts: Ts,
    val u: U,
    val urls: List<Any>
)