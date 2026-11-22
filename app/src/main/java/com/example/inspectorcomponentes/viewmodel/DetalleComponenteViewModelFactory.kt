package com.example.inspectorcomponentes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.inspectorcomponentes.data.repository.ComponenteRepository

/**
 * Factory requerida para instanciar [DetalleComponenteViewModel] inyectando el repositorio.
 */
class DetalleComponenteViewModelFactory(
    private val repository: ComponenteRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetalleComponenteViewModel::class.java)) {
            return DetalleComponenteViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
