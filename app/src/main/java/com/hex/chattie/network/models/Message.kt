package com.hex.chattie.network.models

import com.stfalcon.chatkit.commons.models.IMessage
import com.stfalcon.chatkit.commons.models.IUser
import java.util.*

data class Message(
    val _id: String,
    val _updatedAt: UpdatedAtXX,
    val channels: List<Any>,
    val groupable: Boolean,
    val md: List<MdX>,
    val mentions: List<Any>,
    val msg: String,
    val rid: String,
    val t: String,
    val ts: TsXX,
    val u: UXX,
    val urls: List<Any>
)