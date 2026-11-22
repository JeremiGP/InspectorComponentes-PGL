package com.example.inspectorcomponentes.data.repository

import com.example.inspectorcomponentes.data.local.dao.ComponenteDao
import com.example.inspectorcomponentes.data.local.entity.ComponenteEntity
import kotlinx.coroutines.flow.Flow

/**
 * Repositorio que actúa como única fuente de verdad para los datos de componentes.
 * Oculta la implementación de acceso a datos (Room) al ViewModel.
 */
class ComponenteRepository(private val componenteDao: ComponenteDao) {

    fun obtenerTodos(): Flow<List<ComponenteEntity>> {
        return componenteDao.obtenerTodos()
    }

    suspend fun obtenerPorId(id: Int): ComponenteEntity? {
        return componenteDao.obtenerPorId(id)
    }

    suspend fun insertar(componente: ComponenteEntity) {
        componenteDao.insertar(componente)
    }

    suspend fun actualizar(componente: ComponenteEntity) {
        componenteDao.actualizar(componente)
    }

    suspend fun eliminar(componente: ComponenteEntity) {
        componenteDao.eliminar(componente)
    }
}
