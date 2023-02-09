package com.example.meucep.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.meucep.data.ClientService
import com.example.meucep.data.remote.CepApi
import com.example.meucep.data.repository.CepRepository
import com.example.meucep.domain.Cep
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val clientService: ClientService) : ViewModel() {
    enum class State {
        IDLE, LOADING, DONE, ERROR
    }

    val cepResponse: MutableLiveData<Cep> = MutableLiveData()
    private val state: MutableLiveData<State> = MutableLiveData(State.IDLE)

    fun getCep(cep: String) {
        state.value = State.LOADING
        try {
            CoroutineScope(Dispatchers.Main).launch {
                withContext(Dispatchers.Default) {
                     CepRepository(clientService.client.create(CepApi::class.java)).getCep(cep)
                         .enqueue(object: Callback<Cep> {
                         override fun onResponse(call: Call<Cep>, response: Response<Cep>) {
                             if (response.isSuccessful) {
                                 cepResponse.value = response.body()
                                 state.value = State.DONE
                             } else {
                                 state.value = State.ERROR
                                 Log.d("FailGetCep", "Error to load Cep ${response.code()}")
                             }
                         }

                         override fun onFailure(call: Call<Cep>, t: Throwable) {
                             state.value = State.ERROR
                             Log.d("FailGetCep", "Error to load Cep ${t.printStackTrace()}")
                         }
                     })
                }
            }
        } catch (e: Exception) {
            // state.value = State.ERROR
            Log.d("FailGetCep", "Error to load Cep ${e.printStackTrace()}")
        }
    }

    fun getState(): LiveData<State> {
        return state
    }

    class MainViewModelFatory(
        private val clientService: ClientService
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel(clientService) as T
        }
    }
}