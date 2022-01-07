package com.hex.chattie.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

class RetrofitInstance
{
    private var retrofit : Retrofit? = null

    //    private val API_URL = "http://13.233.138.230:80/"
    private val API_URL = "http://192.168.1.6:3000/api/v1/"

    fun getRetrofitInstance() : Retrofit?
    {
        if (retrofit == null)
        {
            val httpClient = OkHttpClient.Builder()
                    .callTimeout(2, TimeUnit.MINUTES)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
            retrofit = Retrofit.Builder().baseUrl(API_URL).addConverterFactory(GsonConverterFactory.create()).client(httpClient.build()).build()
            //            retrofit = Retrofit.Builder().baseUrl(API_URL).addCallAdapterFactory(NetworkResponseAdapterFactory()).build()
        }
        return retrofit
    }
}