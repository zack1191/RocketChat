package com.hex.chattie.ui_seperator.repositories

import com.hex.chattie.network.RetrofitInstance
import com.hex.chattie.network.responses.CreateDirectMessageResponse
import com.hex.chattie.network.responses.GetUserListResponse
import com.hex.chattie.network.responses.SendMessageResponse
import com.hex.chattie.network.services.MessageService

class MessagesRepository
{
    private val retrofitInstance by lazy {
        RetrofitInstance().getRetrofitInstance() !!
    }

    private val messageService by lazy {
        retrofitInstance.create(MessageService::class.java)
    }

    suspend fun sendMessage(message : String, auth : String, userId : String) : SendMessageResponse
    {
        return messageService.sendMessage(message, auth, userId)
    }

    suspend fun getUserLists(auth : String, userId : String) : GetUserListResponse
    {
        return messageService.userList(auth, userId)
    }

    suspend fun directMessage(message : String, auth : String, userid : String) : CreateDirectMessageResponse
    {
        return messageService.createDirectMessage(message, auth, userid)
    }
}