package com.example.inspectorcomponentes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inspectorcomponentes.data.local.entity.ComponenteEntity
import com.example.inspectorcomponentes.data.repository.ComponenteRepository
import com.example.inspectorcomponentes.viewmodel.state.DetalleUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel encargado exclusivamente de gestionar la información de detalle
 * de un componente específico.
 */
class DetalleComponenteViewModel(
    private val repository: ComponenteRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<DetalleUiState>(DetalleUiState.Cargando)
    val uiState: StateFlow<DetalleUiState> = _uiState.asStateFlow()

    fun cargarComponente(id: Int) {
        viewModelScope.launch {
            _uiState.value = DetalleUiState.Cargando
            try {
                val componente = repository.obtenerPorId(id)
                if (componente != null) {
                    _uiState.value = DetalleUiState.Exito(componente)
                } else {
                    _uiState.value = DetalleUiState.Error("Componente no encontrado")
                }
            } catch (e: Exception) {
                _uiState.value = DetalleUiState.Error(e.localizedMessage ?: "Error desconocido")
            }
        }
    }

    fun eliminar(componente: ComponenteEntity) {
        viewModelScope.launch {
            repository.eliminar(componente)
        }
    }
}
