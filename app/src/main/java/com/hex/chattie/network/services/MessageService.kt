package com.hex.chattie.network.services

import com.hex.chattie.network.responses.CheckRoomNameExistsResponse
import com.hex.chattie.network.responses.CreateDirectMessageResponse
import com.hex.chattie.network.responses.GetUserListResponse
import com.hex.chattie.network.responses.SendMessageResponse
import retrofit2.http.*

interface MessageService
{
    @FormUrlEncoded
    @POST("method.call/sendMessage")
    suspend fun sendMessage(@Field("message") message : String, @Header("X-Auth-Token") auth : String, @Header("X-User-Id") userId : String) : SendMessageResponse

    @GET("users.list")
    suspend fun userList(@Header("X-Auth-Token") auth : String, @Header("X-User-Id") userId : String) : GetUserListResponse

    @FormUrlEncoded
    @POST("method.call/createDirectMessage")
    suspend fun createDirectMessage(@Field("message") message : String, @Header("X-Auth-Token") auth : String, @Header("X-User-Id") userId : String) : CreateDirectMessageResponse


    @FormUrlEncoded
    @POST("method.call/deleteMessage")
    suspend fun deleteMessage(@Field("message") message:String  ,@Header("X-Auth-Token") auth : String, @Header("X-User-Id") userId : String)

    @FormUrlEncoded
    @POST("method.call/readMessages")
    suspend fun readMessage(@Field("message") message:String  ,@Header("X-Auth-Token") auth : String, @Header("X-User-Id") userId : String)


    @FormUrlEncoded
    @POST("method.call/updateMessage")
    suspend fun updateMessage(@Field("message")  message:String  ,@Header("X-Auth-Token") auth : String, @Header("X-User-Id") userId : String)


    @Multipart
    @POST("rooms.upload/GENERAL")
    suspend fun uploadFile(  @Header("X-Auth-Token") auth : String, @Header("X-User-Id") userId : String)


    @FormUrlEncoded
    @POST("method.call/setReaction")
    suspend fun setReaction(@Field("message")  message:String  ,@Header("X-Auth-Token") auth : String, @Header("X-User-Id") userId : String)
}