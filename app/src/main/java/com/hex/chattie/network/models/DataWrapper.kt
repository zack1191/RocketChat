package com.hex.chattie.network.models

data class DataWrapper<out T>(val data : T?, val errorMessage : String?)
{

}