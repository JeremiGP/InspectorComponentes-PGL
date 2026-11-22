package com.example.inspectorcomponentes.ui.state

import com.example.inspectorcomponentes.data.local.entity.ComponenteEntity

/**
 * Representa los estados mutuamente excluyentes en la pantalla de detalles,
 * permitiendo a la UI reaccionar correctamente ante cargas asíncronas.
 */
sealed interface DetalleUiState {
    data object Cargando : DetalleUiState
    data class Exito(val componente: ComponenteEntity) : DetalleUiState
    data class Error(val mensaje: String) : DetalleUiState
}
