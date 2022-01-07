package com.hex.chattie.ui_seperator.viewmodels

import androidx.lifecycle.*
import com.hex.chattie.network.models.CreateRoom
import com.hex.chattie.network.models.DataWrapper
import com.hex.chattie.network.responses.CreateNewRoomResponse
import com.hex.chattie.network.responses.GetRoomsResponse
import com.hex.chattie.network.responses.LoginResponse
import com.hex.chattie.ui_seperator.repositories.RoomRepository
import kotlinx.coroutines.launch
import retrofit2.http.Body

class RoomViewModel : ViewModel()
{
    private var roomRepository : RoomRepository = RoomRepository()
    private val _getRoom = MutableLiveData<DataWrapper<GetRoomsResponse?>>()
    val getRoomsLiveData : MutableLiveData<DataWrapper<GetRoomsResponse?>> get() = _getRoom

    private val _createNewRoom = MutableLiveData<DataWrapper<CreateNewRoomResponse?>>()
    val createNewRoomLiveData : MutableLiveData<DataWrapper<CreateNewRoomResponse?>> get() = _createNewRoom
    fun getRooms(message : String, auth : String, userId : String)
    {
        viewModelScope.launch {
            try
            {
                _getRoom.value = DataWrapper(roomRepository.getRooms(message, auth, userId), null)
            }
            catch (e : Exception)
            {
                _getRoom.value = DataWrapper(null, e.toString())
            }
        }
    }

    fun getLiveData(message : String, auth : String, userId : String) : LiveData<GetRoomsResponse> = liveData {
        try
        {
            emit(roomRepository.getRooms(message, auth, userId))
        }
        catch (e : Exception)
        {

        }
    }

    fun createNewRoom(body:CreateRoom,auth : String, userId : String)
    {
        viewModelScope.launch {
            try
            {
                _createNewRoom.value = DataWrapper(roomRepository.createNewRoom(body,auth, userId), null)
            }
            catch (e : Exception)
            {
                _createNewRoom.value = DataWrapper(null, e.toString())
            }
        }
    }
}