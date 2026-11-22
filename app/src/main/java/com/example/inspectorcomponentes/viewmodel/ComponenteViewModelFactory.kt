package com.example.inspectorcomponentes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.inspectorcomponentes.data.repository.ComponenteRepository

/**
 * Factory requerida para instanciar [ComponenteViewModel] de forma segura
 * inyectándole la dependencia del repositorio.
 */
class ComponenteViewModelFactory(
    private val repository: ComponenteRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ComponenteViewModel::class.java)) {
            return ComponenteViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
