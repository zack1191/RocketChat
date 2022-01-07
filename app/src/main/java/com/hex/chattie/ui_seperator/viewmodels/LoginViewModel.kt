package com.hex.chattie.ui_seperator.viewmodels

import android.util.Log
import androidx.lifecycle.*
import com.hex.chattie.network.models.DataWrapper
import com.hex.chattie.network.responses.LoginResponse
import com.hex.chattie.network.responses.MeResponse
import com.hex.chattie.network.responses.RegisterResponse
import com.hex.chattie.ui_seperator.repositories.LoginRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel()
{
    private val loginRepository by lazy {
        LoginRepository()
    }

    private val _login = MutableLiveData<DataWrapper<LoginResponse?>>()
    val loginLiveData : MutableLiveData<DataWrapper<LoginResponse?>> get() = _login

    private val _register = MutableLiveData<DataWrapper<RegisterResponse?>>()
    val registerLiveData : MutableLiveData<DataWrapper<RegisterResponse?>> get() = _register

    private val _usernameSuggest = MutableLiveData<DataWrapper<RegisterResponse?>>()
    val usernameSuggestLiveData : MutableLiveData<DataWrapper<RegisterResponse?>> get() = _usernameSuggest

    private val _registerLogin = MutableLiveData<DataWrapper<LoginResponse?>>()
    val registerLoginLiveData : MutableLiveData<DataWrapper<LoginResponse?>> get() = _registerLogin

    private val _loadHistory = MutableLiveData<DataWrapper<RegisterResponse?>>()
    val loadHistoryLiveData : MutableLiveData<DataWrapper<RegisterResponse?>> get() = _loadHistory

    private val _setUserName = MutableLiveData<DataWrapper<RegisterResponse?>>()
    val setUserNameLiveData : MutableLiveData<DataWrapper<RegisterResponse?>> get() = _setUserName

    private val _me = MutableLiveData<DataWrapper<MeResponse?>>()
    val meLiveData : MutableLiveData<DataWrapper<MeResponse?>> get() = _me

    init
    {

    }

    fun login(message : String)
    {
        viewModelScope.launch {
            try
            {
                _login.value = DataWrapper(loginRepository.login(message), null)
            }
            catch (e : Exception)
            {
                _login.value = DataWrapper(null, e.toString())
            }
        }

    }

    fun registerLogin(message : String)
    {
        viewModelScope.launch {
            try
            {
                _registerLogin.value = DataWrapper(loginRepository.login(message), null)
            }
            catch (e : Exception)
            {
                _registerLogin.value = DataWrapper(null, e.toString())
            }
        }
    }

    fun register(message : String)
    {
        viewModelScope.launch {
            try
            {
                _register.value = DataWrapper(loginRepository.register(message), null)
            }
            catch (e : Exception)
            {
                _register.value = DataWrapper(null, e.toString())
            }
        }
    }

    fun getUserNameSuggestion(message : String, auth : String, userId : String)
    {
        viewModelScope.launch {
            try
            {
                _usernameSuggest.value = DataWrapper(loginRepository.getUserNameSuggestion(message, auth, userId), null)
            }
            catch (e : Exception)
            {
                _usernameSuggest.value = DataWrapper(null, e.toString())
            }
        }
    }

    fun loadHistory(message : String, auth : String, userId : String)
    {
        viewModelScope.launch {
            try
            {
                loadHistoryLiveData.value = DataWrapper(loginRepository.loadHistory(message, auth, userId), null)

            }
            catch (e : Exception)
            {
                loadHistoryLiveData.value = DataWrapper(null, e.toString())
            }
        }
    }

    fun setUserName(message : String, auth : String, userId : String)
    {
        viewModelScope.launch {
            try
            {
                _setUserName.value = DataWrapper(loginRepository.setUserName(message, auth, userId), null)

            }
            catch (e : Exception)
            {
                _setUserName.value = DataWrapper(null, e.toString())
            }
        }
    }

    fun me(auth : String, userId : String)
    {
        viewModelScope.launch {
            try
            {
                _me.value = DataWrapper(loginRepository.me(auth, userId), null)

            }
            catch (e : Exception)
            {
                _me.value = DataWrapper(null, e.toString())
            }
        }
    }
}