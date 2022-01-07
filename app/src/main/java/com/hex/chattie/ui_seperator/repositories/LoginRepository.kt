package com.hex.chattie.ui_seperator.repositories

import androidx.lifecycle.MutableLiveData
import com.hex.chattie.network.RetrofitInstance
import com.hex.chattie.network.models.DataWrapper
import com.hex.chattie.network.responses.LoginResponse
import com.hex.chattie.network.responses.MeResponse
import com.hex.chattie.network.responses.RegisterResponse
import com.hex.chattie.network.services.LoginService

class LoginRepository
{
    private var _loadHistory = MutableLiveData<RegisterResponse?>()
    val loadHistoryLiveData : MutableLiveData<RegisterResponse?> get() = _loadHistory
    private val retrofitInstance by lazy {
        RetrofitInstance().getRetrofitInstance() !!
    }

    private val loginService by lazy {
        retrofitInstance.create(LoginService::class.java)
    }

    suspend fun login(message : String) : LoginResponse
    {
        return loginService.userLogin(message)
    }

    suspend fun register(message : String) : RegisterResponse
    {
        return loginService.userRegister(message)
    }

    suspend fun getUserNameSuggestion(message : String, auth : String, userId : String) : RegisterResponse
    {
        return loginService.getUserNameSuggestion(message, auth, userId)
    }

    suspend fun loadHistory(message : String, auth : String, userId : String) : RegisterResponse
    {
        return loginService.loadHistory(message, auth, userId)
    }

    suspend fun setUserName(message : String, auth : String, userId : String) : RegisterResponse
    {
        return loginService.setUserName(message, auth, userId)
    }

    suspend fun me(auth : String, userId : String) : MeResponse
    {
        return loginService.me(auth, userId)
    }

}