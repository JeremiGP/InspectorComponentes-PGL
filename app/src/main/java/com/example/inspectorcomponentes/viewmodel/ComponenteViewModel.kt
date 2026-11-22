package com.example.inspectorcomponentes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inspectorcomponentes.data.local.entity.ComponenteEntity
import com.example.inspectorcomponentes.data.repository.ComponenteRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel principal que expone los datos del catálogo a la interfaz de usuario.
 * Utiliza StateFlow para proveer los datos reactivos del repositorio.
 */
class ComponenteViewModel(
    private val repository: ComponenteRepository
) : ViewModel() {

    // Exponemos el Flow del DAO como StateFlow para un mejor manejo del ciclo de vida en Compose
    val componentes: StateFlow<List<ComponenteEntity>> = repository.obtenerTodos()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun agregarComponente(componente: ComponenteEntity) {
        viewModelScope.launch {
            repository.insertar(componente)
        }
    }

    fun actualizarComponente(componente: ComponenteEntity) {
        viewModelScope.launch {
            repository.actualizar(componente)
        }
    }

    fun eliminarComponente(componente: ComponenteEntity) {
        viewModelScope.launch {
            repository.eliminar(componente)
        }
    }

    /**
     * Función de prueba para pre-cargar componentes de SMD de ejemplo.
     */
    fun cargarDatosDeEjemplo() {
        val ejemplos = listOf(
            ComponenteEntity(nombre = "Resistencia 10K", tipo = "Resistor", encapsulado = "0805", valor = "10K Ω", descripcion = "Resistencia pull-up de propósito general"),
            ComponenteEntity(nombre = "Condensador 100nF", tipo = "Capacitor", encapsulado = "1206", valor = "100nF", descripcion = "Condensador de desacoplo"),
            ComponenteEntity(nombre = "NE555", tipo = "IC", encapsulado = "SOIC-8", valor = "Temporizador", descripcion = "Temporizador clásico para parpadeo")
        )
        viewModelScope.launch {
            ejemplos.forEach { repository.insertar(it) }
        }
    }
}
