package com.example.meucep.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ClientService {
    companion object {
        const val REMOTE_API_URL = "https://viacep.com.br/ws/"
    }

    val client: Retrofit = Retrofit.Builder()
        .baseUrl(REMOTE_API_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}