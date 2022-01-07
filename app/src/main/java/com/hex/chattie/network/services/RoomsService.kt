package com.hex.chattie.network.services

import com.hex.chattie.network.models.CreateRoom
import com.hex.chattie.network.responses.CheckRoomNameExistsResponse
import com.hex.chattie.network.responses.CreateNewRoomResponse
import com.hex.chattie.network.responses.GetRoomRolesResponse
import com.hex.chattie.network.responses.GetRoomsResponse
import retrofit2.http.*

interface RoomsService
{
    @Headers("Accept: application/json")

    @POST("groups.create")
    suspend fun createNewRoom(@Body body:CreateRoom, @Header("X-Auth-Token") auth : String, @Header("X-User-Id") userId : String) : CreateNewRoomResponse

    @FormUrlEncoded
    @POST("method.call/rooms:get")
    suspend fun getRooms(@Field("message") message : String, @Header("X-Auth-Token") auth : String, @Header("X-User-Id") userId : String) : GetRoomsResponse

    @FormUrlEncoded
    @POST("method.call/getRoomRoles")
    suspend fun getRoomRoles(@Header("X-Auth-Token") auth : String, @Header("X-User-Id") userId : String) : GetRoomRolesResponse

    @FormUrlEncoded
    @POST("method.call/roomNameExists")
    suspend fun checkRoomNameExists(@Header("X-Auth-Token") auth : String, @Header("X-User-Id") userId : String) : CheckRoomNameExistsResponse
}