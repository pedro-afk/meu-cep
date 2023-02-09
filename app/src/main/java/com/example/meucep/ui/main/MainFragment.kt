package com.example.meucep.ui.main

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.meucep.data.ClientService
import com.example.meucep.databinding.FragmentMainBinding
import com.google.android.material.snackbar.Snackbar

class MainFragment : Fragment() {
    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(
            this,
            MainViewModel.MainViewModelFatory(ClientService()))[MainViewModel::class.java]

        stateObserver()
        binding.buttonSearchCep.setOnClickListener { getCep() }
        return binding.root
    }

    private fun getCep() {
        var textCep = ""
        binding.textFieldCep.addOnEditTextAttachedListener {
            textCep = "${it.editText?.text}"
        }

        if (!validateFieldSearch(textCep)) return

        viewModel.cepResponse.observe(viewLifecycleOwner) {
            binding.textViewCep.text = it.cep
            binding.textViewLogradouro.text = it.logradouro
            binding.textViewComplemento.text = it.complemento
            binding.textViewBairro.text = it.bairro
            binding.textViewLocalidade.text = it.localidade
            binding.textViewUf.text = it.uf
            binding.textViewIbge.text = it.ibge
            binding.textViewGia.text = it.gia
            binding.textViewDDD.text = it.ddd
            binding.textViewSiafi.text = it.siafi
        }

        viewModel.getCep(textCep)
    }

    private fun validateFieldSearch(value: String): Boolean {
        if (value.isNotEmpty()) {
            return true
        }
        Snackbar.make(binding.root.context, binding.root, "Campo CEP não pode ser vazio!",
            Snackbar.LENGTH_LONG).show()
        return false
    }

    private fun stateObserver() {

        viewModel.getState().observe(viewLifecycleOwner) {
            // TODO implements Alert Dialog
            when (it) {
                MainViewModel.State.LOADING -> Snackbar.make(binding.root.context, binding.root, "Carregando CEP...", Snackbar.LENGTH_SHORT).show()
                MainViewModel.State.DONE -> Snackbar.make(binding.root.context, binding.root, "CEP carregado com sucesso!", Snackbar.LENGTH_SHORT).show()
                MainViewModel.State.ERROR -> Snackbar.make(binding.root.context, binding.root, "Erro ao carregar CEP!", Snackbar.LENGTH_SHORT).show()
                else -> Log.d("@GET CEP", "Idle")
            }
        }
    }
}