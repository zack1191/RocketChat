package com.hex.chattie.ui.channels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ChannelsViewModel : ViewModel()
{

    private val _text = MutableLiveData<String>().apply {
        value = "This is Channels Fragment"
    }
    val text : LiveData<String> = _text
}