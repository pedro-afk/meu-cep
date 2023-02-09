package com.example.meucep.data.remote

import com.example.meucep.domain.Cep
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface CepApi {
    @GET("{cep}/json/")
    fun getCep(@Path("cep") cep: String): Call<Cep>
}