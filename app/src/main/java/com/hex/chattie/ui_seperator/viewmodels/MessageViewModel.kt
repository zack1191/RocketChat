package com.hex.chattie.ui_seperator.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hex.chattie.network.models.DataWrapper
import com.hex.chattie.network.responses.CreateDirectMessageResponse
import com.hex.chattie.network.responses.GetUserListResponse
import com.hex.chattie.network.responses.LoginResponse
import com.hex.chattie.network.responses.SendMessageResponse
import com.hex.chattie.ui_seperator.repositories.MessagesRepository
import kotlinx.coroutines.launch

class MessageViewModel : ViewModel()
{
    private val _sendMessage = MutableLiveData<DataWrapper<SendMessageResponse?>>()
    val sendMessageLiveData : MutableLiveData<DataWrapper<SendMessageResponse?>> get() = _sendMessage
    private val _userLists = MutableLiveData<DataWrapper<GetUserListResponse?>>()
    val userListsLiveData : MutableLiveData<DataWrapper<GetUserListResponse?>> get() = _userLists
    private val _directMessage = MutableLiveData<DataWrapper<CreateDirectMessageResponse?>>()
    val directMessageLiveData : MutableLiveData<DataWrapper<CreateDirectMessageResponse?>> get() = _directMessage

    private val messageRepository by lazy {
        Log.i("tag", "init repository")
        MessagesRepository()
    }

    init
    {
        Log.i("tag", "init viewmodel")
    }

    fun sendMessage(message : String, auth : String, userId : String)
    {
        viewModelScope.launch {
            try
            {
                Log.i("tag", "send message to server")
                _sendMessage.value = DataWrapper(messageRepository.sendMessage(message, auth, userId), null)

            }
            catch (e : Exception)
            {
                _sendMessage.value = DataWrapper(null, e.toString())
            }
        }
    }

    fun getUserLists(auth : String, userId : String)
    {
        viewModelScope.launch {
            try
            {
                Log.i("tag", "get user lists")
                _userLists.value = DataWrapper(messageRepository.getUserLists(auth, userId), null)
            }
            catch (e : Exception)
            {
                _userLists.value = DataWrapper(null, e.toString())
            }
        }
    }

    fun createDirectMessage(message : String, auth : String, userId : String)
    {
        viewModelScope.launch {
            try
            {
                _directMessage.value = DataWrapper(messageRepository.directMessage(message, auth, userId), null)
            }
            catch (e : Exception)
            {
                _directMessage.value = DataWrapper(null, e.toString())
            }
        }
    }
}