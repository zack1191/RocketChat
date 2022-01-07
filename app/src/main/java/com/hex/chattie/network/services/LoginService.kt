package com.hex.chattie.network.services

import com.hex.chattie.network.responses.LoginResponse
import com.hex.chattie.network.responses.MeResponse
import com.hex.chattie.network.responses.RegisterResponse
import retrofit2.http.*

interface LoginService
{
    @FormUrlEncoded
    @POST("method.callAnon/login")
    suspend fun userLogin(@Field("message")  message:String) : LoginResponse

    @FormUrlEncoded
    @POST("method.callAnon/registerUser")
    suspend fun userRegister(@Field("message")  message  :String)  :  RegisterResponse

    @FormUrlEncoded
    @POST("method.call/getUsernameSuggestion")
    suspend fun getUserNameSuggestion(@Field("message")  message  :String  ,@Header("X-Auth-Token")auth:String  ,@Header("X-User-Id")userId:String)  :  RegisterResponse

    @FormUrlEncoded
    @POST("method.call/setUsername")
    suspend fun setUserName(@Field("message")  message:String  ,@Header("X-Auth-Token")auth:String  ,@Header("X-User-Id")userId:String)  :RegisterResponse

    @FormUrlEncoded
    @POST("method.call/saveCustomFields")
    suspend fun saveCustomFields(@Field("message")  message:String,@Header("X-Auth-Token")auth:String,@Header("X-User-Id")userId:String)  :RegisterResponse

    @FormUrlEncoded
    @POST("method.call/loadHistory")
    suspend fun loadHistory(@Field("message")  message:String,@Header("X-Auth-Token")auth:String,@Header("X-User-Id")userId:String)  :RegisterResponse

    @GET("me")
    suspend fun me(@Header("X-Auth-Token")auth:String  ,@Header("X-User-Id")userId:String)  :MeResponse
}