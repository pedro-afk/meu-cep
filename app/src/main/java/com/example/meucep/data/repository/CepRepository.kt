package com.example.meucep.data.repository

import com.example.meucep.data.remote.CepApi
import com.example.meucep.domain.Cep
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call

class CepRepository(private val cepApi: CepApi) {
    suspend fun getCep(cep: String): Call<Cep> {
        return withContext(Dispatchers.Default) {
            cepApi.getCep(cep)
        }
    }
}