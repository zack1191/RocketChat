package com.hex.chattie.ui_seperator.repositories

import com.hex.chattie.network.RetrofitInstance
import com.hex.chattie.network.models.CreateRoom
import com.hex.chattie.network.responses.CreateNewRoomResponse
import com.hex.chattie.network.responses.GetRoomsResponse
import com.hex.chattie.network.services.LoginService
import com.hex.chattie.network.services.RoomsService

class RoomRepository
{
    private val retrofitInstance by lazy {
        RetrofitInstance().getRetrofitInstance() !!
    }

    private val roomsService by lazy {
        retrofitInstance.create(RoomsService::class.java)
    }

    suspend fun getRooms(message : String, auth : String, userId : String) : GetRoomsResponse
    {
        return roomsService.getRooms(message, auth, userId)
    }

    suspend fun createNewRoom(body:CreateRoom,auth : String, userId : String) : CreateNewRoomResponse
    {
        return roomsService.createNewRoom(body,auth, userId)
    }
}